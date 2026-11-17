import java.util.*;

class Patient {
    final String name;
    final int severity;
    final long arrival;

    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }
    @Override public String toString() { return name + "(S" + severity + ")"; }
}

public class Q6HospitalTriage {
    private long counter = 0;
    private final PriorityQueue<Patient> queue = new PriorityQueue<Patient>(
        Comparator.comparingInt((Patient p) -> p.severity)
                  .thenComparingLong(p -> p.arrival)
    );

    void admit(String name, int severity) {
        if (severity < 1 || severity > 5)
            throw new IllegalArgumentException("Severity must be from 1 to 5");
        queue.add(new Patient(name, severity, counter++));
    }

    Patient next() { return queue.poll(); }

    void displayWaitingOrder() {
        PriorityQueue<Patient> copy = new PriorityQueue<Patient>(queue);
        System.out.print("Waiting treatment order: ");
        while (!copy.isEmpty()) System.out.print(copy.poll() + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        Q6HospitalTriage er = new Q6HospitalTriage();
        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);
        er.admit("Nila", 4);
        er.admit("Arun", 2);

        er.displayWaitingOrder();
        StringBuilder order = new StringBuilder();
        Patient p;
        while ((p = er.next()) != null) order.append(p).append(' ');
        System.out.println("Treatment order: " + order.toString().trim());

        // Growth experiment: insertion and removal for three queue sizes.
        for (int n : new int[]{1000, 10000, 50000}) {
            PriorityQueue<Patient> q = new PriorityQueue<Patient>(
                Comparator.comparingInt((Patient x) -> x.severity)
                          .thenComparingLong(x -> x.arrival));
            long start = System.nanoTime();
            for (int i = 0; i < n; i++)
                q.add(new Patient("P" + i, (i % 5) + 1, i));
            long addTime = System.nanoTime() - start;
            start = System.nanoTime();
            while (!q.isEmpty()) q.poll();
            long removeTime = System.nanoTime() - start;
            System.out.printf("n=%d add=%.3f ms remove-all=%.3f ms%n",
                n, addTime / 1e6, removeTime / 1e6);
        }
    }
}
