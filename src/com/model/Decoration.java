package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sang on 15/11/26.
 */
@Entity
public class Decoration {
    @Id
    private int id;
    private String type;
    private int startValue;
    private Double ratio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
