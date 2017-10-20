package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Sang on 15/10/21.
 */
@Entity
public class PrintPrice {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer startingPrice;
    private Integer unitPrice;
    private String machine;
    private String colorNum;
    private Integer divideNum;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Integer startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getColorNum() {
        return colorNum;
    }

    public void setColorNum(String colorNum) {
        this.colorNum = colorNum;
    }

    public Integer getDivideNum() {
        return divideNum;
    }

    public void setDivideNum(Integer divideNum) {
        this.divideNum = divideNum;
    }

    @Override
    public String toString() {
        return "PrintPrice{" +
                "id=" + id +
                ", startingPrice=" + startingPrice +
                ", unitPrice=" + unitPrice +
                ", machine='" + machine + '\'' +
                ", colorNum='" + colorNum + '\'' +
                ", divideNum=" + divideNum +
                '}';
    }
}
