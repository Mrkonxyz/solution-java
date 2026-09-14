package org.example.splittwolisteq;

import java.util.ArrayList;
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

        var mid = sum / 2;
        var initialList = new ArrayList<ArrayList<Integer>>();
        var l = new ArrayList<Integer>();
        l.add(org.getFirst());
        if (sumValueInList(l, 0) == mid) {
            var l2 = new ArrayList<>(org);
            for (var i : l) {
                l2.remove(i);
            }
            return new TwoList(l, l2);
        }

        initialList.add(l);
        var correctList = findCorrectList(org, mid, initialList);
        if (correctList == null) {
            return null;
        }

        var l1 = new ArrayList<>(correctList);
        var l2 = new ArrayList<>(org);
        for (var i : l1) {
            l2.remove(i);
        }

        return new TwoList(l1, l2);
    }

    private List<Integer> findCorrectList(List<Integer> org, int target, ArrayList<ArrayList<Integer>> possibleList) {
        if (possibleList.isEmpty()) {
            return null;
        }

        ArrayList<ArrayList<Integer>> nextPossible = new ArrayList<>();
        for (var x: possibleList) {
            var temp = new ArrayList<>(org);
            for (var y : x) {
                temp.remove(y);
            }
            nextPossible.addAll(findPossibleList(x ,temp , target));
        }

        var correctList = nextPossible.stream()
                .filter(v -> sumValueInList(v, 0) == target)
                .findFirst()
                .orElse(null);

        if (correctList == null) {
            return findCorrectList(org, target, nextPossible);
        }

        return correctList;
    }

    private ArrayList<ArrayList<Integer>> findPossibleList(List<Integer> l1, List<Integer> l2, int target) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        for (int i : l2) {
            var possibleList = new ArrayList<Integer>(l1);
            possibleList.add(i);
            if (sumValueInList(possibleList, 0) <= target) {
                result.add(possibleList);
            }
        }
        return result;
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
