package com.example.appinternship;

import java.util.Objects;

public class Currency {
    private String id;
    private String numCode;
    private String charCode;
    private String nominal;
    private String name;
    private String value;
    private String preValue;

    public Currency(){
    }

    public Currency(String id, String numCode, String charCode, String nominal, String name, String value, String preValue){
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.preValue = preValue;
    }

    public String getId() {
        return id;
    }

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPreValue() {
        return preValue;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPreValue(String preValue) {
        this.preValue = preValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return getId().equals(currency.getId()) && getNumCode().equals(currency.getNumCode()) && getCharCode().equals(currency.getCharCode()) && getNominal().equals(currency.getNominal()) && getName().equals(currency.getName()) && getValue().equals(currency.getValue()) && getPreValue().equals(currency.getPreValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumCode(), getCharCode(), getNominal(), getName(), getValue(), getPreValue());
    }
}
