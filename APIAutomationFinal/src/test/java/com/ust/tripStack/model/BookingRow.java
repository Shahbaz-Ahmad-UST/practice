package com.ust.tripStack.model;

import java.time.Instant;
import java.util.List;

public final class BookingRow {

    private final String id;
    private final String pnr;
    private final String empId;
    private final String journeyType;
    private final String inventoryId;
    private final String state;
    private final List<String> seatIds;
    private final long amountPaise;
    private final boolean refundable;
    private final Instant holdExpiresAt;

    public BookingRow(String id, String pnr, String empId, String journeyType,
                      String inventoryId, String state, List<String> seatIds,
                      long amountPaise, boolean refundable, Instant holdExpiresAt) {
        this.id = id;
        this.pnr = pnr;
        this.empId = empId;
        this.journeyType = journeyType;
        this.inventoryId = inventoryId;
        this.state = state;
        this.seatIds = seatIds;
        this.amountPaise = amountPaise;
        this.refundable = refundable;
        this.holdExpiresAt = holdExpiresAt;
    }

    public String id() { return id; }
    public String pnr() { return pnr; }
    public String empId() { return empId; }
    public String journeyType() { return journeyType; }
    public String inventoryId() { return inventoryId; }
    public String state() { return state; }
    public List<String> seatIds() { return seatIds; }
    public long amountPaise() { return amountPaise; }
    public boolean refundable() { return refundable; }
    public Instant holdExpiresAt() { return holdExpiresAt; }

    @Override
    public String toString() {
        return "BookingRow{id='" + id + "', pnr='" + pnr + "', state='" + state + "'}";
    }
}