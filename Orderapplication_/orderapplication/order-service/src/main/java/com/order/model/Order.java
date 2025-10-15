package com.order.model;

import java.util.Objects;

public class Order {
    private String orderId;
    private String product;
    private int quantity;

    public Order() {}

    public Order(String orderId, String product, int quantity) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;
        return quantity == order.quantity &&
                Objects.equals(orderId, order.orderId) &&
                Objects.equals(product, order.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, product, quantity);
    }
}
