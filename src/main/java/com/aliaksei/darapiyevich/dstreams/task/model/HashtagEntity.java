package com.aliaksei.darapiyevich.dstreams.task.model;

import java.util.Objects;

public class HashtagEntity {
    private String text;

    public HashtagEntity() {
    }

    public HashtagEntity(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashtagEntity that = (HashtagEntity) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return "HashtagEntity{" +
                "text='" + text + '\'' +
                '}';
    }
}
