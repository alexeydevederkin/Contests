package contests;

import java.io.PrintWriter;
import java.util.*;

public class FindMinimumGrouping {

    /*
    Given positions of elements, find such grouping by 2 or more elements
    that sum of inner distances is minimal.

    Example 1:
    positions: 2 7 9 10 13 15
    groupings: 2-7, 9-10, 13-15
    distance: 5 + 1 + 2 = 8

    input:
    6
    2 7 9 10 13 15
    output:
    8

    Example 2:
    positions: 1 2 4 5
    groupings: 1-2, 4-5
    distance: 1 + 1 = 2

    input:
    4
    1 2 4 5
    output:
    2
    */

    private static class Interval {
        private final int from;
        private final int to;
        private final int length;

        Interval(int from, int to, int length) {
            this.from = from;
            this.to = to;
            this.length = length;
        }

        int getFrom() {
            return from;
        }

        int getTo() {
            return to;
        }

        int getLength() {
            return length;
        }
    }

    private static int findMin(int[] chairs) {
        int res = 0;

        PriorityQueue<Interval> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.length));

        Set<Integer> selected = new HashSet<>();

        for (int i = 0; i < chairs.length-1; i++) {
            priorityQueue.add(new Interval(i, i+1, (chairs[i+1] - chairs[i])));
        }

        while (priorityQueue.size() > 0) {
            Interval interval = priorityQueue.poll();
            if (interval != null && (!selected.contains(interval.getFrom()) || !selected.contains(interval.getTo()))) {
                res += interval.getLength();
                selected.add(interval.getFrom());
                selected.add(interval.getTo());
            }
            if (selected.size() == chairs.length - 2) {
                break;  // for checking last 2 elements
            }
        }

        // Checking the last 2 elements: combine them or add by one
        int combineLength = 0;
        int addByOneLength = 0;
        for (Interval interval : priorityQueue) {
            if (!selected.contains(interval.getFrom()) && !selected.contains(interval.getTo())) {
                combineLength = interval.getLength();
            }
            if (!selected.contains(interval.getFrom()) || !selected.contains(interval.getTo())) {
                addByOneLength += interval.getLength();
            }
        }

        res += Math.min(combineLength, addByOneLength);

        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);

        int N = in.nextInt();

        int[] chairs = new int[N];
        for (int i = 0; i < N; i++) {
            chairs[i] = in.nextInt();
        }

        out.println(findMin(chairs));
        out.flush();
    }
}