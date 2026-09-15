package win.everything.solution.lengthOfLongestSubstring;

import java.util.Optional;
import java.util.function.Predicate;

public class Solution {

    public int lengthOfLongestSubstring(String s) {

        return Optional.ofNullable(s)
                .filter(Predicate.not(String::isEmpty))
                .map(v -> recursion(v.substring(1), String.valueOf(s.charAt(0)), 1, 1))
                .orElse(0);
    }

    private int recursion(String s, String i, int m, int c) {
        if (s == null || s.isBlank()) {
            return m;
        }
        if (i.indexOf(s.charAt(0)) != -1) {
            i = String.valueOf(s.charAt(0));
            c = 1;
        } else {
            i = i.concat(String.valueOf(s.charAt(0)));
            c++;
        }
        return recursion(s.substring(1), i, Math.max(c, m),  c);
    }
}
