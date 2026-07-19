package com.ust.tripStack.tests;

import com.ust.tripStack.builder.BookingRowBuilder;
import com.ust.tripStack.config.DatabaseConfig;
import com.ust.tripStack.containers.MySQLTestContainer;
import com.ust.tripStack.factory.BookingFactory;
import com.ust.tripStack.model.BookingRow;
import com.ust.tripStack.support.DbSupport;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
@Epic("tripStack Journeys")
@Feature("DbSupport - MySQL Testcontainer")
@Owner("Shahbaz Ahmad")
class DbSupportContainerTest {

    private static DbSupport dbSupport;

    @BeforeAll
    static void setUp() {
        DatabaseConfig config = new DatabaseConfig(
                MySQLTestContainer.jdbcUrl(),
                MySQLTestContainer.username(),
                MySQLTestContainer.password()
        );
        dbSupport = new DbSupport(config);
    }

    @BeforeEach
    void checkReachable() throws SQLException {
        assertTrue(dbSupport.isReachable(), "MySQL Testcontainer should be reachable before each test");
    }

    @Test
    @DisplayName("Confirmed booking dummy data can be saved and read back")
    void confirmedBookingRoundTrips() {
        BookingRow booking = BookingFactory.aConfirmedBooking();
        dbSupport.saveBooking(booking);

        BookingRow found = dbSupport.findBookingByPnr(booking.pnr());

        assertNotNull(found);
        assertEquals(booking.id(), found.id());
        assertEquals("CONFIRMED", found.state());
        assertTrue(found.amountPaise() > 0);
        assertTrue(found.refundable());
        assertEquals(booking.seatIds(), found.seatIds());

        dbSupport.deleteBooking(booking.pnr());
    }

    @Test
    @DisplayName("Held booking dummy data can be saved and read back")
    void heldBookingRoundTrips() {
        BookingRow booking = BookingFactory.aHeldBooking();
        dbSupport.saveBooking(booking);

        BookingRow found = dbSupport.findBookingByPnr(booking.pnr());

        assertNotNull(found);
        assertEquals("HELD", found.state());
        assertNotNull(found.holdExpiresAt());

        dbSupport.deleteBooking(booking.pnr());
    }

    @Test
    @DisplayName("Multi-seat booking preserves seat list ordering")
    void multiSeatBookingRoundTrips() {
        BookingRow booking = BookingFactory.aMultiSeatBooking(List.of("12A", "12B", "12C"));
        dbSupport.saveBooking(booking);

        BookingRow found = dbSupport.findBookingByPnr(booking.pnr());

        assertNotNull(found);
        assertEquals(List.of("12A", "12B", "12C"), found.seatIds());
        assertEquals(450000L, found.amountPaise());

        dbSupport.deleteBooking(booking.pnr());
    }

    @Test
    @DisplayName("bookingExists reflects saved/deleted state")
    void bookingExistsReflectsState() {
        BookingRow booking = BookingFactory.aConfirmedBooking();

        assertFalse(dbSupport.bookingExists(booking.pnr()));

        dbSupport.saveBooking(booking);
        assertTrue(dbSupport.bookingExists(booking.pnr()));

        dbSupport.deleteBooking(booking.pnr());
        assertFalse(dbSupport.bookingExists(booking.pnr()));
    }

    @Test
    @DisplayName("Duplicate PNR is rejected by the unique constraint")
    void duplicatePnrIsRejected() {
        BookingRow original = BookingFactory.aConfirmedBooking();
        dbSupport.saveBooking(original);

        BookingRow duplicatePnr = BookingRowBuilder.from(BookingFactory.aConfirmedBooking())
                .pnr(original.pnr())
                .build();

        assertThrows(RuntimeException.class, () -> dbSupport.saveBooking(duplicatePnr));

        dbSupport.deleteBooking(original.pnr());
    }

    @Test
    @DisplayName("Lookup for a PNR that does not exist returns null")
    void missingPnrReturnsNull() {
        assertNull(dbSupport.findBookingByPnr("TS-000000-0000"));
    }
}