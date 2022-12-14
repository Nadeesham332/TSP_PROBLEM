import java.util.ArrayList;
import java.util.List;

public class TSPGreedy {
    // Function to find the minimum cost path for all the paths
    static void findMinRoute(int[][] tsp)
    {
        int sum = 0; // sum of the costs in min path
        int counter = 0;
        int j = 0, i = 0;
        int min = Integer.MAX_VALUE;
        List<Integer> visitedRouteList = new ArrayList<>();
        int startingNode=0;

        // Starting from the 0th indexed, city 1
        visitedRouteList.add(startingNode);
        int[] route = new int[tsp.length];

        // Traverse the adjacency
        // matrix tsp[][]
        while (i < tsp.length && j < tsp[i].length) {

            // Corner of the Matrix
            if (counter >= tsp[i].length - 1) {
                break;
            }

            // If this path is unvisited then
            // and if the cost is less then
            // update the cost
            if (j != i && !(visitedRouteList.contains(j))) {
                if (tsp[i][j] < min) {
                    min = tsp[i][j];
                    route[counter] = j + 1;
                }
            }
            j++;

            // Check all paths from the
            // ith indexed city
            if (j == tsp[i].length) {
                sum += min;
                min = Integer.MAX_VALUE;
                visitedRouteList.add(route[counter] - 1);
                j = 0;
                i = route[counter] - 1;
                counter++;
            }
        }

        // Update the ending city in array
        // from city which was last visited
        i = route[counter - 1] - 1;

        for (j = 0; j < tsp.length; j++) {

            if ((i != j) && tsp[i][j] < min) {
                min = tsp[i][j];
                route[counter] = j + 1;
            }
        }
        sum += min;


        // Print path from starting node to final node
        System.out.print("Shortest Path is : ");
        for (Integer visited : visitedRouteList) {
            System.out.print(visited+1+" -> ");
        }

        // Started from the node where we finished as well.
        System.out.print(startingNode+1);

        System.out.print("\nMinimum Cost is : ");
        System.out.println(sum);
    }

    // Driver Code
    public static void main(String[] args)
    {
        // Input Matrix
        int[][] tsp = {
                {-1, 225, 304, 236, 213, 339, 187, 197, 226},
                {225, -1, 140, 153, 15, 175, 84, 160, 110},
                {304, 140, -1, 152, 132, 41, 121, 190, 108},
                {236, 153, 152, -1, 143, 188, 70, 73, 63},
                {213, 15, 132, 143, -1, 166, 74, 145, 102},
                {339, 175, 41, 188, 166, -1, 157, 226, 144},
                {187, 84, 121, 70, 74, 157, -1, 81, 43},
                {97, 160, 190, 73, 145, 226, 81, -1, 90},
                {226, 110, 108, 63, 102, 144, 43, 90, -1}
        };

        // Function Call
        findMinRoute(tsp);
    }
}
