package ru.sberbank.multithreading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {
    private List<Checkpoint> checkpoints;

    public Message() {
        checkpoints = Collections.synchronizedList(new ArrayList<Checkpoint>());
    }

    public void checkIn(Integer id){
        checkpoints.add(new Checkpoint(id, System.nanoTime()));
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
}
