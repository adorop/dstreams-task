package com.aliaksei.darapiyevich.dstreams.task.model;

import java.util.Objects;

public class Place {
    private String countryCode;

    public Place() {
    }

    public Place(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(countryCode, place.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode);
    }

    @Override
    public String toString() {
        return "Place{" +
                "countryCode='" + countryCode + '\'' +
                '}';
    }
}
