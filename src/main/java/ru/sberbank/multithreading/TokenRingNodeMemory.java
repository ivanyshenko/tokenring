package ru.sberbank.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class TokenRingNodeMemory {
    private List<LinkedBlockingQueue<Message>> registers;
    private int numThreads;

    public TokenRingNodeMemory(int numThreads) {
        this.registers = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            registers.add(new LinkedBlockingQueue<Message>());
        }
        this.numThreads = numThreads;
    }

    public Message getNextMessage(int id){
        try {
            return registers.get(id).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void passMessage(int id, Message message){
        message.checkIn(id);
        registers.get((id + 1) % numThreads).offer(message);
    }


}
