package com.lca.phoenix.entities;

import com.lca.phoenix.queries.OrderStatus;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Product implements Serializable {

    @Id
    private String orderId;
    private String product;
    private OrderStatus orderStatus;

    public Product(String orderId, String product, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.product = product;
        this.orderStatus = orderStatus;
    }

    public Product() {
        super();
    }

    public Product(String orderId, String product) {
        this.orderId = orderId;
        this.product = product;
        setOrderStatus(OrderStatus.PLACED);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

