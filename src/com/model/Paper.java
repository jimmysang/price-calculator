package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sang on 15/10/21.
 */
@Entity
public class Paper {
    @Id
    private Integer id;
    private Integer weight;
    private String name;
    private Integer weightPrice;
    private Double areaPrice;
    private Integer thickness;
    private String special;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeightPrice() {
        return weightPrice;
    }

    public void setWeightPrice(Integer weightPrice) {
        this.weightPrice = weightPrice;
    }

    public Double getAreaPrice() {
        return areaPrice;
    }

    public void setAreaPrice(Double areaPrice) {
        this.areaPrice = areaPrice;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}
