package com.aliaksei.darapiyevich.dstreams.task.model;

import java.io.Serializable;
import java.util.Objects;

public class TweetsCount implements Serializable {
    private TweetsCountKey key;
    private Long count;

    public TweetsCount() {
    }

    public TweetsCount(TweetsCountKey key, Long count) {
        this.key = key;
        this.count = count;
    }

    public TweetsCountKey getKey() {
        return key;
    }

    public void setKey(TweetsCountKey key) {
        this.key = key;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetsCount that = (TweetsCount) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, count);
    }

    @Override
    public String toString() {
        return "TweetsCount{" +
                "key=" + key +
                ", count=" + count +
                '}';
    }
}
