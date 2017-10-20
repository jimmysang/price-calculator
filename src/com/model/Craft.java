package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sang on 15/10/22.
 */
@Entity
public class Craft {
    @Id
    private Integer id;
    private String name;
    private String key1;
    private String key2;
    private String key3;
    private Double plateUnitPrice;
    private Double plateStarting;
    private Double materialUnitPrice;
    private Double materialStarting;
    private Double manualUnitPrice;
    private Double manualStarting;
    private String note;

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

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public Double getPlateUnitPrice() {
        return plateUnitPrice;
    }

    public void setPlateUnitPrice(Double plateUnitPrice) {
        this.plateUnitPrice = plateUnitPrice;
    }

    public Double getPlateStarting() {
        return plateStarting;
    }

    public void setPlateStarting(Double plateStarting) {
        this.plateStarting = plateStarting;
    }

    public Double getMaterialUnitPrice() {
        return materialUnitPrice;
    }

    public void setMaterialUnitPrice(Double materialUnitPrice) {
        this.materialUnitPrice = materialUnitPrice;
    }

    public Double getMaterialStarting() {
        return materialStarting;
    }

    public void setMaterialStarting(Double materialStarting) {
        this.materialStarting = materialStarting;
    }

    public Double getManualUnitPrice() {
        return manualUnitPrice;
    }

    public void setManualUnitPrice(Double manualUnitPrice) {
        this.manualUnitPrice = manualUnitPrice;
    }

    public Double getManualStarting() {
        return manualStarting;
    }

    public void setManualStarting(Double manualStarting) {
        this.manualStarting = manualStarting;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
