package com.aliaksei.darapiyevich.dstreams.task.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Tweet {
    private Date createdAt;
    private Place place;
    private List<HashtagEntity> hashtagEntities;

    public Tweet() {
    }

    public Tweet(Date createdAt, Place place, List<HashtagEntity> hashtagEntities) {
        this.createdAt = createdAt;
        this.place = place;
        this.hashtagEntities = hashtagEntities;
    }

    public Place getPlace() {
        return place;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<HashtagEntity> getHashtagEntities() {
        return hashtagEntities;
    }

    public void setHashtagEntities(List<HashtagEntity> hashtagEntities) {
        this.hashtagEntities = hashtagEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(createdAt, tweet.createdAt) &&
                Objects.equals(place, tweet.place) &&
                Objects.equals(hashtagEntities, tweet.hashtagEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, place, hashtagEntities);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "createdAt=" + createdAt +
                ", place=" + place +
                ", hashtagEntities=" + hashtagEntities +
                '}';
    }
}
