package contests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

/*
    Calculate the sum of keystrokes with the help of autocomplete.

    If previous words were "a" an "bcd", then
    to type "bcd" again we need only one keystroke "b" and use autocomplete.

    length   keystrokes
       3          3         app
       3          1         app (-2, autocomplete after "a")
       5          5         apple
       5          5         appzz
      10         10         appzzappzz
       5          4         apple (-1, autocomplete after "appl")
       3          3         app
      10          6         appzzappzz (-4, autocomplete after "appzza")

    sum of keystrokes = 37

    input:
        8
        app
        app
        apple
        appzz
        appzzappzz
        apple
        app
        appzzappzz

    output:
        37
 */
public class Autocomplete {

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


    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        int childrenCount = 0;
        boolean isLeaf = false;
    }

    static class Trie {
        private TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        int insert(String word) {
            int autocomplete = 0;

            TrieNode p = root;

            for(int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                int idx = c - 'a';
                if (p.children[idx] == null) {
                    TrieNode child = new TrieNode();
                    p.children[idx] = child;
                    p.childrenCount++;
                    p = child;
                    autocomplete = 0;
                } else {
                    p = p.children[idx];
                    if (p.isLeaf) {
                        if (p.childrenCount == 0)
                            autocomplete++;
                        else
                            autocomplete = 0;
                    } else {
                        if (p.childrenCount == 1)
                            autocomplete++;
                        else
                            autocomplete = 0;
                    }
                }
            }

            p.isLeaf = true;

            if (p.childrenCount > 0)
                autocomplete = 0;

            // need one keystroke for autocomplete to appear
            // "apple" and "appzz" -> autocomplete after "appl" -> = 1, not 2
            autocomplete = autocomplete == 0 ? 0 : autocomplete - 1;

            return autocomplete;
        }
    }


    public static void main(String[] args) {
        FastReader reader = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int n = reader.nextInt();

        Trie trie = new Trie();

        int strokes = 0;

        for (int i = 0; i < n; i++) {
            String word = reader.next();
            strokes += word.length() - trie.insert(word);
        }

        out.println(strokes);

        out.flush();
    }
}