import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TSP_Dynamic {

    private final int N, start;
    private final double[][] distance;
    private List<Integer> tour = new ArrayList<>();
    private double minTourCost = Double.POSITIVE_INFINITY;
    private boolean ranSolver = false;

    public TSP_Dynamic(double[][] distance) {
        this(0, distance);
    }

    public TSP_Dynamic(int start, double[][] distance) {
        N = distance.length;

        this.start = start;
        this.distance = distance;
    }

    // optimal tour
    public List<Integer> getTour() {
        if (!ranSolver) solve();
        return tour;
    }

    // minimal tour cost
    public double getTourCost() {
        if (!ranSolver) solve();
        return minTourCost;
    }

    // solutions of  the traveling salesman problem
    public void solve() {

        if (ranSolver) return;

        final int END_STATE = (1 << N) - 1;
        Double[][] memo = new Double[N][1 << N];

        // Add all outgoing edges from the starting node to memo table.
        for (int end = 0; end < N; end++) {
            if (end == start) continue;
            memo[end][(1 << start) | (1 << end)] = distance[start][end];
        }

        for (int r = 3; r <= N; r++) {
            for (int subset : combinations(r, N)) {
                if (notIn(start, subset)) continue;
                for (int next = 0; next < N; next++) {
                    if (next == start || notIn(next, subset)) continue;
                    int subsetWithoutNext = subset ^ (1 << next);
                    double minDist = Double.POSITIVE_INFINITY;
                    for (int end = 0; end < N; end++) {
                        if (end == start || end == next || notIn(end, subset)) continue;
                        double newDistance = memo[end][subsetWithoutNext] + distance[end][next];
                        if (newDistance < minDist) {
                            minDist = newDistance;
                        }
                    }
                    memo[next][subset] = minDist;
                }
            }
        }

        // Connect tour back to starting node and minimize cost.
        for (int i = 0; i < N; i++) {
            if (i == start) continue;
            double tourCost = memo[i][END_STATE] + distance[i][start];
            if (tourCost < minTourCost) {
                minTourCost = tourCost;
            }
        }

        int lastIndex = start;
        int state = END_STATE;
        tour.add(start);

        // Reconstruct TSP path from memo table.
        for (int i = 1; i < N; i++) {

            int index = -1;
            for (int j = 0; j < N; j++) {
                if (j == start || notIn(j, state)) continue;
                if (index == -1) index = j;
                double prevDist = memo[index][state] + distance[index][lastIndex];
                double newDist  = memo[j][state] + distance[j][lastIndex];
                if (newDist < prevDist) {
                    index = j;
                }
            }

            tour.add(index);
            state = state ^ (1 << index);
            lastIndex = index;
        }

        tour.add(start);
        Collections.reverse(tour);

        ranSolver = true;
    }

    private static boolean notIn(int elem, int subset) {
        return ((1 << elem) & subset) == 0;
    }

    public static List<Integer> combinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();
        combinations(0, 0, r, n, subsets);
        return subsets;
    }

    private static void combinations(int set, int at, int r, int n, List<Integer> subsets) {

        // Return early if there are more elements left to select than what is available.
        int elementsLeftToPick = n - at;
        if (elementsLeftToPick < r) return;

        if (r == 0) {
            subsets.add(set);
        } else {
            for (int i = at; i < n; i++) {
                // Try to include this element
                set |= 1 << i;

                combinations(set, i + 1, r - 1, n, subsets);

                //  try with Backtrack
                set &= ~(1 << i);
            }
        }
    }

    public static void main(String[] args) {
        // Create distance weight matrix
        int n = 9;
        double[][] distanceMatrix = {
                { 0, 225, 304, 236, 213, 339, 187, 197, 226 },
                { 225, 0, 140, 153, 15, 175, 84, 160, 110 },
                { 304, 140, 0, 152, 132, 41, 121, 190, 108 },
                { 236, 153, 152, 0, 143, 188, 70, 73, 63 },
                { 213, 15, 132, 143, 0, 166, 74, 145, 102 },
                { 339, 175, 41, 188, 166, 0, 157, 226, 144 },
                { 187, 84, 121, 70, 74, 157, 0, 81, 43 },
                { 197, 160, 190, 73, 145, 226, 81, 0, 90 },
                { 226, 110, 108, 63, 102, 144, 43, 90, 0 },
        };

        int startNode = 0;
        TSP_Dynamic solution = new TSP_Dynamic(startNode, distanceMatrix);

        System.out.print("Path:\t" + solution.getTour());
        System.out.print("\nPath cost:\t" + solution.getTourCost());
    }
}