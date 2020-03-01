package com.example.demo.model;

import java.io.Serializable;

public class NewOrderDTO implements Serializable {
    private Integer itemId;
    private Integer userId;
    private Integer quantity;

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
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
