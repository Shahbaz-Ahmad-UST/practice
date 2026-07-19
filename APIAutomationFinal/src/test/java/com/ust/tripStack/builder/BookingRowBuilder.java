package com.ust.tripStack.builder;

import com.ust.tripStack.model.BookingRow;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingRowBuilder {

    private String id = "BKG-" + UUID.randomUUID();
    private String pnr = randomPnr();
    private String empId = "EMP-" + (1000 + (int) (Math.random() * 9000));
    private String journeyType = "ONE_WAY";
    private String inventoryId = "FL-BLRCCU-51";
    private String state = "CONFIRMED";
    private List<String> seatIds = new ArrayList<>(List.of("12A"));
    private long amountPaise = 450000L;
    private boolean refundable = true;
    private Instant holdExpiresAt = null;

    private BookingRowBuilder() {
    }

    public static BookingRowBuilder aBooking() {
        return new BookingRowBuilder();
    }

    public static BookingRowBuilder from(BookingRow row) {
        return new BookingRowBuilder()
                .id(row.id())
                .pnr(row.pnr())
                .empId(row.empId())
                .journeyType(row.journeyType())
                .inventoryId(row.inventoryId())
                .state(row.state())
                .seatIds(row.seatIds())
                .amountPaise(row.amountPaise())
                .refundable(row.refundable())
                .holdExpiresAt(row.holdExpiresAt());
    }

    public BookingRowBuilder id(String id) { this.id = id; return this; }
    public BookingRowBuilder pnr(String pnr) { this.pnr = pnr; return this; }
    public BookingRowBuilder empId(String empId) { this.empId = empId; return this; }
    public BookingRowBuilder journeyType(String journeyType) { this.journeyType = journeyType; return this; }
    public BookingRowBuilder inventoryId(String inventoryId) { this.inventoryId = inventoryId; return this; }
    public BookingRowBuilder state(String state) { this.state = state; return this; }
    public BookingRowBuilder seatIds(List<String> seatIds) { this.seatIds = new ArrayList<>(seatIds); return this; }
    public BookingRowBuilder amountPaise(long amountPaise) { this.amountPaise = amountPaise; return this; }
    public BookingRowBuilder refundable(boolean refundable) { this.refundable = refundable; return this; }
    public BookingRowBuilder holdExpiresAt(Instant holdExpiresAt) { this.holdExpiresAt = holdExpiresAt; return this; }

    public BookingRow build() {
        return new BookingRow(id, pnr, empId, journeyType, inventoryId, state,
                seatIds, amountPaise, refundable, holdExpiresAt);
    }

    private static String randomPnr() {
        long ts = System.currentTimeMillis() % 1_000_000;
        int rand = 1000 + (int) (Math.random() * 9000);
        return "TS-" + ts + "-" + rand;
    }
}