package com.ust.tripStack.model;

import java.sql.Timestamp;

public class OrderRow {
    public String id;
    public String pnr;
    public String empId;
    public String journeyType;
    public String inventoryId;
    public String state;
    public String seatIds;
    public long amountPaise;
    public boolean refundable;
    public Timestamp holdExpiresAt;

    @Override
    public String toString() {
        return "OrderRow{id='" + id + "', pnr='" + pnr + "', state='" + state + "'}";
    }
}