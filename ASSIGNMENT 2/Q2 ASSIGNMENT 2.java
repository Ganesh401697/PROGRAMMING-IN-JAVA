import java.util.*;

public class Q2ListBenchmark {
    static volatile long sink;

    static long time(String label, Runnable action) {
        long start = System.nanoTime();
        action.run();
        long elapsed = System.nanoTime() - start;
        System.out.printf("%-28s %.3f ms%n", label, elapsed / 1_000_000.0);
        return elapsed;
    }

    static void runSize(final int n) {
        System.out.println("\n--- Playlist size: " + n + " ---");
        ArrayList<Integer> array = new ArrayList<Integer>();
        LinkedList<Integer> linked = new LinkedList<Integer>();
        for (int i = 0; i < n; i++) { array.add(i); linked.add(i); }

        time("ArrayList add at end", () -> {
            ArrayList<Integer> x = new ArrayList<Integer>(array);
            for (int i = 0; i < 1000; i++) x.add(i);
            sink += x.size();
        });
        time("LinkedList add at end", () -> {
            LinkedList<Integer> x = new LinkedList<Integer>(linked);
            for (int i = 0; i < 1000; i++) x.add(i);
            sink += x.size();
        });
        time("ArrayList insert front x100", () -> {
            ArrayList<Integer> x = new ArrayList<Integer>(array);
            for (int i = 0; i < 100; i++) x.add(0, i);
            sink += x.size();
        });
        time("LinkedList insert front x100", () -> {
            LinkedList<Integer> x = new LinkedList<Integer>(linked);
            for (int i = 0; i < 100; i++) x.addFirst(i);
            sink += x.size();
        });
        time("ArrayList middle insert x100", () -> {
            ArrayList<Integer> x = new ArrayList<Integer>(array);
            for (int i = 0; i < 100; i++) x.add(x.size() / 2, i);
            sink += x.size();
        });
        time("LinkedList middle insert x100", () -> {
            LinkedList<Integer> x = new LinkedList<Integer>(linked);
            for (int i = 0; i < 100; i++) x.add(x.size() / 2, i);
            sink += x.size();
        });
        time("ArrayList get k x10000", () -> {
            long s = 0;
            for (int i = 0; i < 10000; i++) s += array.get(n / 2);
            sink += s;
        });
        time("LinkedList get k x10000", () -> {
            long s = 0;
            for (int i = 0; i < 10000; i++) s += linked.get(n / 2);
            sink += s;
        });
        time("ArrayList full iteration", () -> {
            long s = 0; for (int v : array) s += v; sink += s;
        });
        time("LinkedList full iteration", () -> {
            long s = 0; for (int v : linked) s += v; sink += s;
        });

        time("ArrayDeque add/remove ends", () -> {
            ArrayDeque<Integer> d = new ArrayDeque<Integer>();
            for (int i = 0; i < n; i++) d.addLast(i);
            for (int i = 0; i < 1000 && !d.isEmpty(); i++) {
                d.addFirst(i); d.removeFirst(); d.addLast(i); d.removeFirst();
            }
            sink += d.size();
        });
    }

    public static void main(String[] args) {
        runSize(1000);
        runSize(10000);
        runSize(50000);
        System.out.println("\nIgnore small timing variations; run more times for formal evidence.");
    }
}
