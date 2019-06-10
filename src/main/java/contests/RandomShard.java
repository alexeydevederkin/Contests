import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;


/*
    There are N shards. Each shard has weight.

    Return shard i such that
        sum of weights [shard_0, ..., shard_i] <= "weight"
            and
        sum of weights [shard_0, ..., shard_i, shard_i+1] > "weight".

    If sum of all shard weights <= "weight", return n-1.
    If weight of 0-th shard > "weight", return -1.

    Types of queries:
    [+ shard weight] - increase shard's weight.
    [? weight] - return i - the number of shard satisfying the condition.

    input:
        1 5
        + 0 2
        + 0 1
        + 0 3
        ? 6
        ? 5

    output:
        0
        -1
*/
public class RandomShard {

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

    static class ShardStorage {
        int n;
        long sum;
        long[] shards;
        int bucketSize;
        int bucketsNum;
        long[] buckets;

        ShardStorage(int n) {
            this.n = n;
            this.shards = new long[n];
            this.bucketSize = (int) Math.pow(n, 0.42);
            this.bucketsNum = (int) Math.ceil(n / (double) bucketSize);
            this.buckets = new long[bucketsNum];
        }

        private int getBiggestBucket(long sumWeight) {
            int left = 0, right = bucketsNum - 1;

            while (left < right) {
                int mid = left + (right - left) / 2;
                long sumInBucket = buckets[mid];
                if (sumInBucket <= sumWeight) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            return left - 1;
        }

        void increaseWeight(int shard, long weight) {
            shards[shard] += weight;
            sum += weight;

            for (int i = shard / bucketSize; i < bucketsNum; i++) {
                buckets[i] += weight;
            }
        }

        int getShardNumber(long sumWeight) {
            if (sum <= sumWeight) {
                return n - 1;
            }

            if (shards[0] > sumWeight) {
                return -1;
            }


            int biggestBucket = getBiggestBucket(sumWeight);

            long sum = biggestBucket >= 0 ? buckets[biggestBucket] : 0;

            int nextBucketBegin = (biggestBucket + 1) * bucketSize;
            int nextBucketEnd = (biggestBucket + 2) * bucketSize;
            for (int i = nextBucketBegin; i < nextBucketEnd; i++) {
                if (sum + shards[i] > sumWeight) {
                    return i-1;
                }
                sum += shards[i];
            }

            return n - 1;
        }
    }

    public static void main(String[] args) {
        FastReader reader = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int N = reader.nextInt();
        int Q = reader.nextInt();

        ShardStorage shardStorage = new ShardStorage(N);

        for (int i = 0; i < Q; i++) {
            String[] line = reader.nextLine().split(" ");
            if (line[0].equals("+")) {
                int shard = Integer.parseInt(line[1]);
                long weight = Long.parseLong(line[2]);
                shardStorage.increaseWeight(shard, weight);
            } else if (line[0].equals("?")) {
                long weight = Long.parseLong(line[1]);
                out.println(shardStorage.getShardNumber(weight));
            }
        }

        out.flush();
    }
}