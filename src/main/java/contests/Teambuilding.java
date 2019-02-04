package contests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;


/*
    Check if people can be divided into two groups so
    that in each group everyone knows everyone.


    5 persons:

     1 --- 2     4
           |     |
           3     5

    Cannot be divided to two groups, because person "1" does not know person "3".

    input:
        5 3
        1 2
        2 3
        4 5

    output:
        -1


    7 persons:

    1, 2, 3, 4 know each other and 5, 6, 7 know each other:

    input:
        7 9
        1 2
        1 3
        1 4
        2 3
        2 4
        3 4
        5 6
        5 7
        6 7

    output (size of first group, first group, second group):
        4
        1 2 3 4
        5 6 7
 */
public class Teambuilding {

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }


    private static boolean checkBipartite(int[][] edges, int[] verticesColors) {
        // checking if the graph is bipartite
        for (int i = 0; i < edges.length; i++) {
            if (verticesColors[i] == 0) {
                if (!dfs(edges, verticesColors, i)) {
                    return false;
                }
            }
        }
        return true;
    }


    private static boolean dfs(int[][] edges, int[] verticesColors, int vertex) {
        int ownColor = verticesColors[vertex];

        if (ownColor == 0) {
            ownColor = 1;
            verticesColors[vertex] = ownColor;
        }

        int oppositeColor = (verticesColors[vertex] == 1) ? 2 : 1;

        for (int i = 0; i < edges[vertex].length; i++) {
            if (edges[vertex][i] == 1 && i != vertex) {
                if (verticesColors[i] == ownColor) {
                    return false;
                }
                if (verticesColors[i] == oppositeColor) {
                    continue;
                }
                verticesColors[i] = oppositeColor;
                if (!dfs(edges, verticesColors, i)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        FastReader reader = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int vCnt = reader.nextInt();
        int eCnt = reader.nextInt();

        int[][] edges = new int[vCnt][vCnt];

        for (int i = 0; i < eCnt; i++) {
            String[] edgeString = reader.nextLine().split(" ");
            int from = Integer.parseInt(edgeString[0]) - 1;
            int to = Integer.parseInt(edgeString[1]) - 1;
            edges[from][to] = 1;
            edges[to][from] = 1;
        }

        // reversing graph: edges[i][j] = 1 if person "i" does not know person "j"
        for (int i = 0; i < vCnt; i++) {
            for (int j = 0; j < vCnt; j++) {
                edges[i][j] = (edges[i][j] == 1) ? 0 : 1;
            }
        }

        int[] verticesColors = new int[vCnt];
        // 0 - not visited, 1 - visited and "left side", 2 - visited and "right side"

        if (checkBipartite(edges, verticesColors)) {

            boolean hasElementsInSecondGroup = Arrays.stream(verticesColors).anyMatch(value -> value == 2);

            // When everyone knows each other, make second group from 2 persons
            if (!hasElementsInSecondGroup) {
                verticesColors[vCnt-1] = 2;
                verticesColors[vCnt-2] = 2;
            }

            int leftCnt = 0;
            StringJoiner leftVertices = new StringJoiner(" ");
            StringJoiner rightVertices = new StringJoiner(" ");

            for (int i = 0; i < vCnt; i++) {
                if (verticesColors[i] == 1) {
                    leftVertices.add(String.valueOf(i + 1));
                    leftCnt++;
                } else {
                    rightVertices.add(String.valueOf(i + 1));
                }
            }

            out.println(leftCnt);
            out.println(leftVertices.toString());
            out.println(rightVertices.toString());
        } else {
            out.println("-1");
        }

        out.flush();
    }
}