import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;


/*
    Given recorded games (in no particular order) between chess players (knock-out system),
    find semifinalists if it is possible.

    input:
        7
        GORBOVSKII ABALKIN
        SIKORSKI KAMMERER
        SIKORSKI GORBOVSKII
        BYKOV IURKOVSKII
        PRIVALOV BYKOV
        GORBOVSKII IURKOVSKII
        IURKOVSKII KIVRIN

    output:
        BYKOV SIKORSKI

    input:
        3
        IVANOV PETROV
        PETROV BOSHIROV
        BOSHIROV IVANOV

    output:
        NO SOLUTION
*/
public class Tournament {

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

    public static int log2(int n) {
        return Math.round((float)(Math.log(n) / Math.log(2)));
    }

    public static void main(String[] args) {
        FastReader reader = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int n = reader.nextInt();

        if (n < 3) {
            out.println("NO SOLUTION");
            out.flush();
            return;
        }

        Map<String, Integer> playerGames = new HashMap<>();
        Map<String, Set<String>> playerRivals = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String game = reader.nextLine();
            String[] players = game.split(" ");

            if (players.length != 2) {
                out.println("NO SOLUTION");
                out.flush();
                return;
            }

            for (String player : players) {
                int games = playerGames.getOrDefault(player, 0);
                playerGames.put(player, games + 1);
            }

            Set<String> rivals0 = playerRivals.getOrDefault(players[0], new HashSet<>());
            if (rivals0.contains(players[1])) {
                out.println("NO SOLUTION");
                out.flush();
                return;
            } else {
                rivals0.add(players[1]);
                playerRivals.put(players[0], rivals0);
            }

            Set<String> rivals1 = playerRivals.getOrDefault(players[1], new HashSet<>());
            if (rivals1.contains(players[0])) {
                out.println("NO SOLUTION");
                out.flush();
                return;
            } else {
                rivals1.add(players[0]);
                playerRivals.put(players[1], rivals1);
            }
        }

        int gamesForRound = log2(n + 1);
        int roundNumberOfPlayers = 2;
        List<String> roundParticipants = new ArrayList<>();

        for (Map.Entry<String, Integer> player : playerGames.entrySet()) {
            int gamesPlayed = player.getValue();

            if (gamesPlayed == gamesForRound) {
                roundParticipants.add(player.getKey());
            }

            if (gamesPlayed > gamesForRound) {
                out.println("NO SOLUTION");
                out.flush();
                return;
            }
        }


        List<String> bronzeMatchParticipants = new ArrayList<>();


        while (gamesForRound >= 1) {

            if (roundParticipants.size() != roundNumberOfPlayers) {
                out.println("NO SOLUTION");
                out.flush();
                return;
            }

            gamesForRound--;
            if (gamesForRound == 0) {
                break;
            }

            roundNumberOfPlayers *= 2;
            List<String> nextRoundParticipants = new ArrayList<>();

            for (String participant : roundParticipants) {
                Set<String> rivals = playerRivals.get(participant);
                boolean found = false;
                for (String rival : rivals) {
                    if (playerGames.get(rival) == gamesForRound) {
                        if (found) {
                            out.println("NO SOLUTION");
                            out.flush();
                            return;
                        }
                        found = true;
                        nextRoundParticipants.add(rival);
                    }
                }
                if (!found) {
                    out.println("NO SOLUTION");
                    out.flush();
                    return;
                }
            }

            if (gamesForRound == log2(n + 1) - 1) {
                bronzeMatchParticipants.addAll(nextRoundParticipants);
            }

            nextRoundParticipants.addAll(roundParticipants);

            roundParticipants = nextRoundParticipants;
        }

        out.println(bronzeMatchParticipants.get(0) + " " + bronzeMatchParticipants.get(1));
        out.flush();
    }
}