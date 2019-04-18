package com.aliaksei.darapiyevich.dstreams.task.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlattenHashtagTweet {
    private final LocalDateTime createdAt;
    private final String countryCode;
    private final String hashtag;

    public FlattenHashtagTweet(LocalDateTime createdAt, String countryCode, String hashtag) {
        this.createdAt = createdAt;
        this.countryCode = countryCode;
        this.hashtag = hashtag;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getHashtag() {
        return hashtag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlattenHashtagTweet that = (FlattenHashtagTweet) o;
        return Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(hashtag, that.hashtag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, countryCode, hashtag);
    }

    @Override
    public String toString() {
        return "FlattenHashtagTweet{" +
                "createdAt=" + createdAt +
                ", countryCode='" + countryCode + '\'' +
                ", hashtag='" + hashtag + '\'' +
                '}';
    }
}
