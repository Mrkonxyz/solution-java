package org.example.splittwolisteq;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Solution {

    public record TwoList(List<Integer> l1,List<Integer> l2) {
    }

    public TwoList splitTwoListEq(List<Integer> org) {

        if (Objects.isNull(org) || org.isEmpty()) {
            return null;
        }

        if (org.size() == 1) {
            return null;
        }

        int sum = sumValueInList(org, 0);
        if (isOdd(sum)) {
            return null;
        }

        var l1 = new ArrayList<Integer>();
        var l2 = new ArrayList<Integer>();
        var temp = new ArrayList<Integer>(org);

        return recursion(temp, l1, l2, sumValueInList(temp, 0) / 2, sumValueInList(temp, 0) / 2 );
    }

    private TwoList recursion(ArrayList<Integer> temp, ArrayList<Integer> l1, ArrayList<Integer> l2, Integer  targetNumber, Integer  orgTargetNumber) {
        if (targetNumber < 0 && orgTargetNumber >= 0) {
            return null;
        }
        if (targetNumber > 0 && orgTargetNumber < 0) {
            return null;
        }
        if (targetNumber == 0) {
            l2.addAll(temp);
            return new TwoList(l1, l2);
        }

        var maxNearbyTarget = findMaxNearbyTarget(targetNumber, temp, orgTargetNumber);
        if (maxNearbyTarget == null) {
            return null;
        }

        temp.remove(maxNearbyTarget);
        l1.add(maxNearbyTarget);

        return recursion(temp, l1, l2, targetNumber - maxNearbyTarget, orgTargetNumber);
    }

    private Integer findMaxNearbyTarget(Integer target, List<Integer> list , Integer  orgTargetNumber) {
        if (orgTargetNumber < 0 ) {
            return list
                    .stream()
                    .filter(v -> v >= target)
                    .min(Comparator.naturalOrder())
                    .orElse(null);
        }
        return list
                .stream()
                .filter(v -> v <= target)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    private boolean isOdd(int n) {
        n = n < 0 ? n * -1 : n;
        return n % 2 == 1;
    }

    private int sumValueInList(List<Integer> l1, int sum) {
        if (l1.isEmpty()) {
            return sum;
        }
        return sumValueInList(l1.subList(1, l1.size()), sum + l1.getFirst());
    }
}
