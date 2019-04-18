package com.aliaksei.darapiyevich.dstreams.task.model;

import java.io.Serializable;
import java.util.Objects;

public class TweetsCountKey implements Serializable {
    private final String country;
    private final String hour;
    private final String hashtag;

    public TweetsCountKey(String country, String hour, String hashtag) {
        this.country = country;
        this.hour = hour;
        this.hashtag = hashtag;
    }

    public String getCountry() {
        return country;
    }

    public String getHour() {
        return hour;
    }

    public String getHashtag() {
        return hashtag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetsCountKey that = (TweetsCountKey) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(hour, that.hour) &&
                Objects.equals(hashtag, that.hashtag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, hour, hashtag);
    }

    @Override
    public String toString() {
        return "TweetsCountKey{" +
                "country='" + country + '\'' +
                ", hour='" + hour + '\'' +
                ", hashtag='" + hashtag + '\'' +
                '}';
    }
}
