package ru.sberbank.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TokenRing {

    private int numThreads;
    private TokenRingNodeMemory memory;

    public TokenRing(int numThreads) {
        this.numThreads = numThreads;
        this.memory = new TokenRingNodeMemory(numThreads);
    }

    public List<Message> run(int messageCountLimit, int numMessages) {
        CyclicBarrier startGate = new CyclicBarrier(numThreads);
        CyclicBarrier finishGate = new CyclicBarrier(numThreads+1);
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            new Thread(new TokenRingNode(i, memory, startGate, finishGate, messageCountLimit, numMessages, messages)).start();
        }
        try {
            finishGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
