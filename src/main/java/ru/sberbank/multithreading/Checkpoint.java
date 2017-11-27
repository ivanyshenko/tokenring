package ru.sberbank.multithreading;

public class Checkpoint {
    private Integer id;
    private Long timestamp;

    public Checkpoint(Integer id, Long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Integer getId() {
        return id;
    }
}
