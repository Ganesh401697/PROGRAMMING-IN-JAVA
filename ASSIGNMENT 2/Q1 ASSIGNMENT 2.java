import java.util.*;

public class Q1StringBenchmark {
    static final int FIELDS = 50;
    static final int TICKETS = 1000;
    static final int RUNS = 5;

    static String[] fields(int ticket) {
        String[] a = new String[FIELDS];
        for (int i = 0; i < FIELDS; i++) a[i] = "Field" + i + "=" + ticket + ";";
        return a;
    }

    static String usingPlus(String[] a) {
        String s = "";
        for (String x : a) s = s + x;
        return s;
    }

    static String usingBuilder(String[] a) {
        StringBuilder b = new StringBuilder();
        for (String x : a) b.append(x);
        return b.toString();
    }

    static String usingBuffer(String[] a) {
        StringBuffer b = new StringBuffer();
        for (String x : a) b.append(x);
        return b.toString();
    }

    interface Maker { String make(String[] a); }

    static long measure(Maker maker) {
        long start = System.nanoTime();
        long checksum = 0;
        for (int t = 0; t < TICKETS; t++) checksum += maker.make(fields(t)).length();
        long elapsed = System.nanoTime() - start;
        if (checksum == 0) System.out.println(checksum);
        return elapsed;
    }

    static void test(String name, Maker maker) {
        // Warm-up
        for (int i = 0; i < 2; i++) measure(maker);
        long total = 0;
        for (int i = 0; i < RUNS; i++) total += measure(maker);
        System.out.printf("%-14s average: %.3f ms%n", name,
                total / (double) RUNS / 1_000_000.0);
    }

    public static void main(String[] args) {
        System.out.println("Q1: " + TICKETS + " tickets, " + FIELDS + " fields each");
        test("String +", Q1StringBenchmark::usingPlus);
        test("StringBuilder", Q1StringBenchmark::usingBuilder);
        test("StringBuffer", Q1StringBenchmark::usingBuffer);

        String[] sample = fields(1);
        System.out.println("Sample ticket using StringBuilder: " + usingBuilder(sample));
        Runtime r = Runtime.getRuntime();
        System.gc();
        long before = r.totalMemory() - r.freeMemory();
        String keep = usingBuilder(sample);
        long after = r.totalMemory() - r.freeMemory();
        System.out.println("Approx. memory change for one retained ticket: " +
                Math.max(0, after - before) + " bytes (rough estimate)");
        System.out.println("Estimated intermediate strings: '+' creates repeated intermediate strings; " +
                "Builder/Buffer use a mutable buffer and one final String.");
    }
}
