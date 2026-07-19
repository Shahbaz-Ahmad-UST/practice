package com.ust.tripStack.support;

import com.ust.tripStack.config.DatabaseConfig;
import com.ust.tripStack.model.BookingRow;
import com.ust.tripStack.model.OrderRow;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class DbSupport {

    private final DatabaseConfig config;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public DbSupport(DatabaseConfig config) {
        this.config = config;
    }

    public boolean isReachable() throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT 1");
             ResultSet result = statement.executeQuery()) {
            return result.next() && result.getInt(1) == 1;
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(config.jdbcUrl(), config.username(), config.password());
    }

    public BookingRow findBookingByPnr(String pnr) {

        try (Connection connection = openConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             "SELECT id, pnr, emp_id, journey_type, inventory_id, state, " +
                                     "seat_ids, amount_paise, refundable, hold_expires_at " +
                                     "FROM bookings WHERE pnr = ?")) {

            statement.setString(1, pnr);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    List<String> seatIds = parseSeatIds(result.getString("seat_ids"));

                    Timestamp holdExpiresAt = result.getTimestamp("hold_expires_at");

                    return new BookingRow(
                            result.getString("id"),
                            result.getString("pnr"),
                            result.getString("emp_id"),
                            result.getString("journey_type"),
                            result.getString("inventory_id"),
                            result.getString("state"),
                            seatIds,
                            result.getLong("amount_paise"),
                            result.getBoolean("refundable"),
                            holdExpiresAt != null ? holdExpiresAt.toInstant() : null
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to find booking with pnr: " + pnr, e);
        }
    }

    public boolean bookingExists(String pnr) {

        try (Connection connection = openConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             "SELECT COUNT(*) FROM bookings WHERE pnr = ?")) {

            statement.setString(1, pnr);

            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt(1) == 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBooking(String pnr) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM bookings WHERE pnr = ?")) {

            statement.setString(1, pnr);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Booking not found with pnr: " + pnr);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete booking with pnr: " + pnr, e);
        }
    }

    private List<String> parseSeatIds(String rawJson) {
        try {
            return Arrays.asList(MAPPER.readValue(rawJson, String[].class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse seat_ids JSON: " + rawJson, e);
        }
    }


    public void saveBooking(BookingRow booking) {

        String sql = "INSERT INTO bookings " +
                "(id, pnr, emp_id, journey_type, inventory_id, state, seat_ids, " +
                "amount_paise, refundable, hold_expires_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, booking.id());
            statement.setString(2, booking.pnr());
            statement.setString(3, booking.empId());
            statement.setString(4, booking.journeyType());
            statement.setString(5, booking.inventoryId());
            statement.setString(6, booking.state());
            statement.setString(7, toJson(booking.seatIds()));
            statement.setLong(8, booking.amountPaise());
            statement.setBoolean(9, booking.refundable());

            if (booking.holdExpiresAt() != null) {
                statement.setTimestamp(10, Timestamp.from(booking.holdExpiresAt()));
            } else {
                statement.setNull(10, Types.TIMESTAMP);
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to insert booking with pnr: " + booking.pnr());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save booking with pnr: " + booking.pnr(), e);
        }
    }

    private String toJson(List<String> seatIds) {
        try {
            return MAPPER.writeValueAsString(seatIds);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize seat_ids", e);
        }
    }


}