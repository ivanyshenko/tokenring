package ru.sberbank.multithreading;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TokenRingNode implements Runnable {

    private CyclicBarrier startGate;
    private CyclicBarrier finishGate;
    private Integer messagePassLimit;
    private Integer numMessages;
    private Integer id;
    private TokenRingNodeMemory memory;

    public TokenRingNode(int id, TokenRingNodeMemory memory, CyclicBarrier startGate, CyclicBarrier finishGate, int messagePassLimit, int numMessages, List<Message> messages) {
        this.startGate = startGate;
        this.finishGate = finishGate;
        this.messagePassLimit = messagePassLimit;
        this.id = id;
        this.memory = memory;
        this.numMessages = numMessages;

        if (id == 0){
            for (int i = 0; i < numMessages; i++) {
                Message msg = new Message();
                messages.add(msg);
                memory.passMessage(-1, msg);
            }
        }
    }

    public void run() {
        try {
            startGate.await();
            int messagePassed = 0;
            while (messagePassed < messagePassLimit){
                Message message = memory.getNextMessage(id);
//                Iterator<Checkpoint> it = message.checkpoints.iterator();
//                while(it.hasNext()){
//                    Checkpoint checkpoint = it.next();
//                    System.out.println(checkpoint.getId() + " " + checkpoint.getTimestamp());
//                }
                memory.passMessage(id, message);
                messagePassed++;
            }
            finishGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
