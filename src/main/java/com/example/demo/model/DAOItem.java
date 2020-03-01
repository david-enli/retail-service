package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "Inventory")
public class DAOItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String itemName;
    @Column
    private String sellerName;
    @Column
    private Double price;
    @Column
    private String description;
    @Column
    private Integer quantity;
    @Column
    private String category;

    public long getId() {
        return this.id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public Integer getQuantity() { return quantity;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
