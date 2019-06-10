import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


/*
    Find optimal prize fund distribution between teams so that
    SUM( ABS( Team_i_Player_j - Prize_Team_i ) ) would be minimal.

    Input format:
        [number of teams]
        [team_i size] [team_i player_1 earned] [team_i player_2 earned] ...
        [team_j size] [team_j player_1 earned] [team_j player_2 earned] ...
        [prize fund]

    Output format:
        [team_i prize] [team_j prize]

    input:
        2
        3 5 4 1
        3 1 2 3
        6

    output:
        4 2


    input:
        2
        1 0
        2 0 1
        3

    output:
        2 1
 */
public class PrizeDistribution {

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

    static class Team implements Comparable<Team> {
        int index;
        int teamSize;
        long prize;
        long currentPenalty;
        long currentShift;
        long[] earned;
        int pointer;

        Team(int index, int teamSize, long[] earned) {
            this.index = index;
            this.teamSize = teamSize;
            this.earned = earned;
            this.prize = 0;
            this.pointer = -1;
            updatePenalty(0);
        }

        void updatePenalty(long addedPrize) {
            prize += addedPrize;

            while (pointer < earned.length-1 && earned[pointer+1] == prize) {
                pointer++;
            }

            currentShift = Integer.MAX_VALUE;
            if (pointer < teamSize - 1) {
                currentShift = earned[pointer + 1] - prize;
            }

            int elementsLessOrEqual = pointer + 1;
            int elementsBigger = teamSize - pointer - 1;

            currentPenalty = elementsLessOrEqual - elementsBigger;
        }

        @Override
        public String toString() {
            return String.valueOf(prize);
        }

        @Override
        public int compareTo(Team o) {
            if (this.currentPenalty < o.currentPenalty) {
                return -1;
            } else if (this.currentPenalty > o.currentPenalty) {
                return 1;
            }

            if (this.currentShift > o.currentShift) {
                return -1;
            } else if (this.currentShift < o.currentShift) {
                return 1;
            }

            return 0;
        }
    }

    public static void main(String[] args) {
        FastReader reader = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int N = reader.nextInt();

        List<Team> teams = new ArrayList<>(N);
        long prizesGiven = 0;

        for (int i = 0; i < N; i++) {
            String[] line = reader.nextLine().split(" ");

            int teamSize = Integer.parseInt(line[0]);
            long[] playersEarned = new long[teamSize];

            for (int j = 1; j < line.length; j++) {
                playersEarned[j-1] = Long.parseLong(line[j]);
            }

            Arrays.sort(playersEarned);

            teams.add(new Team(i, teamSize, playersEarned));
        }

        long T = reader.nextLong();

        PriorityQueue<Team> pq = new PriorityQueue<>(teams);

        while (prizesGiven < T) {
            Team team = pq.poll();

            long prizeAdded = team.currentShift;
            if (prizeAdded > (T - prizesGiven)) {
                prizeAdded = T - prizesGiven;
            }

            team.updatePenalty(prizeAdded);
            pq.add(team);

            prizesGiven += prizeAdded;
        }

        teams.sort(Comparator.comparingInt(o -> o.index));

        out.println(teams.stream().map(Team::toString).collect(Collectors.joining(" ")));

        out.flush();
    }
}