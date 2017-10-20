package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sang on 15/10/27.
 */
@Entity
public class StickerPrice {
    @Id
    private Integer id;
    private String type;
    private Integer amount;
    private Float price;
    private String fmType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getFmType() {
        return fmType;
    }

    public void setFmType(String fmType) {
        this.fmType = fmType;
    }
}
