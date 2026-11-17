import java.util.*;

class Team {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {
    private final List<T> items = new ArrayList<T>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) { this.order = order; }
    void add(T item) { items.add(item); }

    List<T> ranking() {
        List<T> copy = new ArrayList<T>(items);
        copy.sort(order);
        return copy;
    }

    // Accepts a list of T or any subtype of T.
    void addAll(List<? extends T> moreItems) {
        items.addAll(moreItems);
    }
}

public class Q5GenericLeaderboard {
    public static void main(String[] args) {
        Comparator<Team> byRank =
            Comparator.comparingInt((Team t) -> t.points).reversed()
                .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
                .thenComparing(t -> t.name);

        Leaderboard<Team> board = new Leaderboard<Team>(byRank);
        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));
        System.out.println(board.ranking());

        // Second test data set: several ties at points and goal difference.
        List<Team> extra = Arrays.asList(
            new Team("Zebras", 12, 3),
            new Team("Aardvarks", 12, 3),
            new Team("Lions", 12, 8),
            new Team("Tigers", 8, 0)
        );
        board.addAll(extra);
        System.out.println("Second test: " + board.ranking());
    }
}
