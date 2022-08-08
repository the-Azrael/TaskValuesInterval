import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final int SIZE = 25;
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static String[] getStrings() {
        String[] strings = new String[SIZE];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = generateText("aab", 30_000);
        }
        return strings;
    }

    public static int getMax(String text) {
        int maxSize = 0;
        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < text.length(); j++) {
                if (i >= j) {
                    continue;
                }
                boolean bFound = false;
                for (int k = i; k < j; k++) {
                    if (text.charAt(k) == 'b') {
                        bFound = true;
                        break;
                    }
                }
                if (!bFound && maxSize < j - i) {
                    maxSize = j - i;
                }
            }
        }
        return maxSize;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(SIZE);
        List<Future> futures = new ArrayList<>();
        long startTs = System.currentTimeMillis(); // start time
        String[] texts = getStrings();
        for (String text : texts) {
            Callable<String> myCallable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return text.substring(1, 100) + " -> " + getMax(text);
                }
            };
            Future<String> future = es.submit(myCallable);
            futures.add(future);
        }
        for (Future<String> future : futures) {
            String line = future.get();
            System.out.println(line);
        }
        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");
        es.shutdown();
    }
}