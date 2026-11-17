public class Q7RedesignStringBuilding {
    static final int TICKETS = 3000;
    static final int FIELDS = 80;
    static volatile long sink;

    static String original(int ticket) {
        String text = "";
        for (int i = 0; i < FIELDS; i++)
            text = text + "field" + i + "=" + ticket + ";";
        return text;
    }

    // Redesign: build in one mutable buffer, then create the final String once.
    static String redesigned(int ticket) {
        StringBuilder text = new StringBuilder(FIELDS * 16);
        for (int i = 0; i < FIELDS; i++)
            text.append("field").append(i).append('=').append(ticket).append(';');
        return text.toString();
    }

    static long measure(boolean useRedesign) {
        long start = System.nanoTime();
        long chars = 0;
        for (int t = 0; t < TICKETS; t++) {
            String result = useRedesign ? redesigned(t) : original(t);
            chars += result.length();
        }
        sink = chars;
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        // Warm-up
        measure(false);
        measure(true);

        long oldTotal = 0, newTotal = 0;
        int runs = 5;
        for (int i = 0; i < runs; i++) {
            oldTotal += measure(false);
            newTotal += measure(true);
        }
        System.out.printf("Original repeated + average: %.3f ms%n",
            oldTotal / (double) runs / 1e6);
        System.out.printf("Redesigned StringBuilder average: %.3f ms%n",
            newTotal / (double) runs / 1e6);
        System.out.println("Both methods generate the same ticket text: " +
            original(1).equals(redesigned(1)));
    }
}
