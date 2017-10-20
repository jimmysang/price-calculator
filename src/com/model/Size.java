package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sang on 15/10/22.
 */
@Entity
public class Size {
    @Id
    private Integer id;
    private String name;
    private Float size;
    private Integer side;
    private Integer divideNum;
    private Boolean M2able;
    private Integer M2printDivide;
    private String M2method;
    private Boolean M4able;
    private Integer M4printDivide;
    private String M4method;
    private Boolean M8able;
    private Integer M8printDivide;
    private String M8method;

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

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Integer getSide() {
        return side;
    }

    public void setSide(Integer side) {
        this.side = side;
    }

    public Integer getDivideNum() {
        return divideNum;
    }

    public void setDivideNum(Integer divideNum) {
        this.divideNum = divideNum;
    }

    public Boolean isM2able() {
        return M2able;
    }

    public void setM2able(Boolean m2able) {
        M2able = m2able;
    }

    public Integer getM2printDivide() {
        return M2printDivide;
    }

    public void setM2printDivide(Integer m2printDivide) {
        M2printDivide = m2printDivide;
    }

    public String getM2method() {
        return M2method;
    }

    public void setM2method(String m2method) {
        M2method = m2method;
    }

    public Boolean isM4able() {
        return M4able;
    }

    public void setM4able(Boolean m4able) {
        M4able = m4able;
    }

    public Integer getM4printDivide() {
        return M4printDivide;
    }

    public void setM4printDivide(Integer m4printDivide) {
        M4printDivide = m4printDivide;
    }

    public String getM4method() {
        return M4method;
    }

    public void setM4method(String m4method) {
        M4method = m4method;
    }

    public Boolean isM8able() {
        return M8able;
    }

    public void setM8able(Boolean m8able) {
        M8able = m8able;
    }

    public Integer getM8printDivide() {
        return M8printDivide;
    }

    public void setM8printDivide(Integer m8printDivide) {
        M8printDivide = m8printDivide;
    }

    public String getM8method() {
        return M8method;
    }

    public void setM8method(String m8method) {
        M8method = m8method;
    }

    public boolean isThisSizeAble(int divideNum){
        switch (divideNum){
            case 2:return isM2able();
            case 4:return isM4able();
            case 8:return isM8able();
            default: return false;
        }
    }

    public String getMethod(int divideNum){
        switch (divideNum){
            case 2:return getM2method();
            case 4:return getM4method();
            case 8:return getM8method();
            default: return "错误尺寸";
        }
    }

    public int getPrintDivide(int divideNum){
        switch (divideNum){
            case 2:return getM2printDivide();
            case 4:return getM4printDivide();
            case 8:return getM8printDivide();
            default: return 0;
        }
    }

}
