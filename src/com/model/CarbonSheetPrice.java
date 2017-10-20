package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sang on 15/10/26.
 */
@Entity
public class CarbonSheetPrice {
    @Id
    private Integer id;
    private String size;
    private Integer lianAmount;
    private Integer taoAmount;
    private Integer amount;
    private Float price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getLianAmount() {
        return lianAmount;
    }

    public void setLianAmount(Integer lianAmount) {
        this.lianAmount = lianAmount;
    }

    public Integer getTaoAmount() {
        return taoAmount;
    }

    public void setTaoAmount(Integer taoAmount) {
        this.taoAmount = taoAmount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
