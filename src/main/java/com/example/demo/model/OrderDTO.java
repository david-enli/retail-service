package com.example.demo.model;

import org.apache.catalina.User;

import java.io.Serializable;

public class OrderDTO implements Serializable {
    private Integer itemId;
    private Integer userId;
    private String orderName;
    private String status;
    private Double unitPrice;
    private Integer quantity;
    private String category;


    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public Integer getUserId() {return this.userId;}
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getItemName() {
        return this.orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {return category;}

    public void setCategory(String category) {
        this.category = category;
    }

}
