package com.ust.tripStack.factory;

import com.ust.tripStack.builder.BookingRowBuilder;
import com.ust.tripStack.model.BookingRow;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class BookingFactory {

    private BookingFactory() {
    }

    public static BookingRow aConfirmedBooking() {
        return BookingRowBuilder.aBooking()
                .state("CONFIRMED")
                .refundable(true)
                .holdExpiresAt(null)
                .build();
    }

    public static BookingRow aHeldBooking() {
        Instant holdExpiry = Instant.now().plus(15, ChronoUnit.MINUTES);
        return BookingRowBuilder.aBooking()
                .state("HELD")
                .refundable(false)
                .holdExpiresAt(holdExpiry)
                .build();
    }


    public static BookingRow aMultiSeatBooking(List<String> seatIds) {
        return BookingRowBuilder.aBooking()
                .seatIds(seatIds)
                .amountPaise(150000L * seatIds.size())
                .build();
    }

}