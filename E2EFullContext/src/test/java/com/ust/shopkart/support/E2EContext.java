package com.ust.shopkart.support;

public class E2EContext {

    private String customerEmail;
    private String customerPassword;
    private String token;

    private String sku;
    private String productName;

    private long cartId;
    private long orderId;

    private int expectedTotalPaise;
    private String shippingAddress;

    public E2EContext withCredentials(String email, String password) {
        this.customerEmail = email;
        this.customerPassword = password;
        return this;
    }

    public E2EContext withToken(String token) {
        this.token = token;
        return this;
    }

    public E2EContext withProduct(String sku, String productName) {
        this.sku = sku;
        this.productName = productName;
        return this;
    }

    public E2EContext withCartId(long cartId) {
        this.cartId = cartId;
        return this;
    }

    public E2EContext withOrderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public E2EContext withExpectedTotalPaise(int expectedTotalPaise) {
        this.expectedTotalPaise = expectedTotalPaise;
        return this;
    }

    public E2EContext withShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public String customerEmail() {
        return customerEmail;
    }

    public String customerPassword() {
        return customerPassword;
    }

    public String token() {
        return token;
    }

    public String sku() {
        return sku;
    }

    public String productName() {
        return productName;
    }

    public long cartId() {
        return cartId;
    }

    public long orderId() {
        return orderId;
    }

    public int expectedTotalPaise() {
        return expectedTotalPaise;
    }

    public String shippingAddress() {
        return shippingAddress;
    }
}