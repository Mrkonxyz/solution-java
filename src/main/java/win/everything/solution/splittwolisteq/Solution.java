package win.everything.solution.splittwolisteq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Solution {

    public record TwoList(List<Integer> l1,List<Integer> l2) {
    }

    public TwoList splitTwoListEq(List<Integer> org) {

        if (Objects.isNull(org) || org.isEmpty()) {
            return null;
        }

        int sum = sumValueInList(org);
        if (org.size() == 1 && sum != 0) {
            return null;
        }


        if (isOdd(sum)) {
            return null;
        }

        var mid = sum / 2;
        var initialPossibleList = new ArrayList<ArrayList<Integer>>();
        var initialList = new ArrayList<Integer>();
        initialList.add(org.getFirst());
        initialPossibleList.add(initialList);

        return Optional.ofNullable(findCorrectList(org, mid, initialPossibleList))
                .map(correctList -> new TwoList(correctList, differentList(org, correctList)))
                .orElse(null);
    }

    private List<Integer> findCorrectList(List<Integer> org, int target, ArrayList<ArrayList<Integer>> possibleList) {
        if (possibleList.isEmpty()) {
            return null;
        }

       return Optional.ofNullable(getCorrectList(target, possibleList))
                .orElse(findCorrectList(org, target, generatePossibleList(org, target, possibleList)));
    }

    private List<Integer> getCorrectList( int target, ArrayList<ArrayList<Integer>> possibleList) {
        return possibleList.stream()
                .filter(v -> sumValueInList(v) == target)
                .findFirst()
                .orElse(null);
    }

    private ArrayList<ArrayList<Integer>> generatePossibleList(List<Integer> org, int target, ArrayList<ArrayList<Integer>> possibleList) {
        return possibleList
                .stream()
                .map(currentList -> findPossibleList(currentList, differentList(org, currentList), target))
                .flatMap(List::stream)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<ArrayList<Integer>> findPossibleList(List<Integer> baseList, List<Integer> possibleValue, int target) {
        return possibleValue
                .stream()
                .map(v -> {
                    var possibleList = new ArrayList<>(baseList);
                    possibleList.add(v);
                    return possibleList;
                })
                .filter( possibleList -> sumValueInList(possibleList) <= target)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    private boolean isOdd(int n) {
        n = n < 0 ? n * -1 : n;
        return n % 2 == 1;
    }

    private int sumValueInList(List<Integer> l1) {
        return sumValueInList(l1 , 0);
    }

    private int sumValueInList(List<Integer> l1, int sum) {
        if (l1.isEmpty()) {
            return sum;
        }
        return sumValueInList(l1.subList(1, l1.size()), sum + l1.getFirst());
    }

    private <T> ArrayList<T> differentList(List<T> l1, List<T> l2) {
        ArrayList<T> result = new ArrayList<>(l1);
        for (T i: l2) {
            result.remove(i);
        }
        return result;
    }
}
