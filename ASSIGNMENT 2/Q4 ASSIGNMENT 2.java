import java.util.*;

public class Q4MapComparison {
    static volatile int sink;

    static Map<String, Integer> makeHash(int n) {
        Map<String, Integer> m = new HashMap<String, Integer>();
        for (int i = 0; i < n; i++) m.put(String.format("M%07d", i), i);
        return m;
    }
    static Map<String, Integer> makeTree(int n) {
        Map<String, Integer> m = new TreeMap<String, Integer>();
        for (int i = 0; i < n; i++) m.put(String.format("M%07d", i), i);
        return m;
    }
    static Map<String, Integer> makeLinked(int n) {
        Map<String, Integer> m = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < n; i++) m.put(String.format("M%07d", i), i);
        return m;
    }

    static void test(String name, Map<String, Integer> map, int n) {
        String key = String.format("M%07d", n / 2);
        long start = System.nanoTime();
        int total = 0;
        for (int i = 0; i < 100000; i++) total += map.get(key);
        long lookup = System.nanoTime() - start;
        sink = total;

        start = System.nanoTime();
        String first = "";
        for (String k : map.keySet()) { first = k; }
        long iteration = System.nanoTime() - start;

        System.out.printf("%-14s lookup 100,000: %.3f ms | iteration: %.3f ms | " +
                "first key in iteration: %s%n", name,
                lookup / 1e6, iteration / 1e6,
                map.keySet().iterator().next());
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 10000, 50000};
        for (int n : sizes) {
            System.out.println("\n--- Medicines: " + n + " ---");
            test("HashMap", makeHash(n), n);
            test("TreeMap", makeTree(n), n);
            test("LinkedHashMap", makeLinked(n), n);
        }
    }
}
