package ru.sberbank.multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        int messageCountLimit = 10_000;
        for (int numThreads = 2; numThreads < 32; numThreads++) {
            for (int numMessages = 2; numMessages < 40; numMessages++) {
                List<Message> messages = new TokenRing(numThreads).run(messageCountLimit * numMessages, numMessages);
                System.out.println(String.format("numThreads = %d numMessages= %d",numThreads,numMessages));
                System.out.println(messages.stream().mapToDouble(message -> calculateLatency(message.getCheckpoints())).average().getAsDouble());
                System.out.println(calculateThroughput(messages,  messageCountLimit));
            }
        }
    }
    public static double calculateLatency(List<Checkpoint> allCheckpoints){
        Checkpoint[] checkpoints = getCheckpointsWithId(allCheckpoints, 0);
        ArrayList<Long> latencies = new ArrayList<>();
        for (int i = 1; i < checkpoints.length; i++) {
            latencies.add(checkpoints[i].getTimestamp() - checkpoints[i-1].getTimestamp());
        }
        return latencies.stream().mapToLong(Long::longValue).average().getAsDouble()/1_000_000_000d;
    }

    public static double calculateThroughput(List<Message> messages, int numDeltas){
        Object[] checkpointArray = messages.stream().map(message -> getCheckpointsWithId(message.getCheckpoints(), 1)).toArray();
        Checkpoint[][] checkpoints = Arrays.copyOf(checkpointArray,checkpointArray.length, Checkpoint[][].class);
        ArrayList<Long> deltas = new ArrayList<>();

        for (int j = 0; j < numDeltas; j++) {
            for (int i = 1; i < checkpoints.length; i++) {
                deltas.add(checkpoints[i][j].getTimestamp() - checkpoints[i-1][j].getTimestamp());
            }
        }

        return 1_000_000_000d/deltas.stream().mapToLong(Long::longValue).average().getAsDouble();
    }


    private static Checkpoint[] getCheckpointsWithId(List<Checkpoint> allCheckpoints, Integer id) {
        Object[] filteredCheckpoints = allCheckpoints.stream()
                .filter(checkpoint -> checkpoint.getId() == id).toArray();
        return Arrays.copyOf(filteredCheckpoints,filteredCheckpoints.length, Checkpoint[].class);
    }

}
