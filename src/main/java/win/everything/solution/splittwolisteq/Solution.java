package win.everything.solution.splittwolisteq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Solution {

    public record TwoList(List<Integer> l1, List<Integer> l2) {
    }

    public TwoList splitTwoListEq(List<Integer> org) {
        var toResult = toResult(org);
        return Optional.ofNullable(org)
                .filter(this::isNotEmpty)
                .map(this::sumValueInList)
                .filter(this::isEven)
                .map(this::minusTwo)
                .map(mid -> findCorrectList(org, mid, initialPossibleList(org.getFirst())))
                .map(toResult)
                .orElse(null);
    }

    private ArrayList<ArrayList<Integer>> initialPossibleList(Integer firstElement) {
        var initialPossibleList = new ArrayList<ArrayList<Integer>>();
        var initialList = new ArrayList<Integer>();
        initialList.add(firstElement);
        initialPossibleList.add(initialList);
        return initialPossibleList;
    }

    private Function<List<Integer>, TwoList> toResult(List<Integer> org) {
        return correctList -> new TwoList(correctList, differentList(org, correctList));
    }

    private Integer minusTwo(Integer n) {
        return n / 2;
    }

    private boolean isNotEmpty(List<Integer> input) {
        return input != null && !input.isEmpty();
    }

    private  List<Integer> findCorrectList(List<Integer> org, int target, ArrayList<ArrayList<Integer>> possibleList) {
       return possibleList.isEmpty() ? null
               : Optional.ofNullable(getCorrectList(target, possibleList))
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
        var newAndAdd = newArrayAndAndVal(baseList);
        var isPossible = possibleListCondition(target);
        return possibleValue
                .stream()
                .map(newAndAdd)
                .filter(isPossible)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Predicate<List<Integer>> possibleListCondition(int target) {
        return possibleList -> target > 0 ? sumValueInList(possibleList) <= target : sumValueInList(possibleList) >= target;
    }

    private Function<Integer  , ArrayList<Integer>> newArrayAndAndVal(List<Integer> baseList) {
        return v -> {
            var newList = new ArrayList<>(baseList);
            newList.add(v);
            return newList;
        };
    }

    private boolean isEven(int n) {
        n = n < 0 ? n * -1 : n;
        return n % 2 == 0;
    }

    private int sumValueInList(List<Integer> l1) {
        return sumValueInList(l1 , 0);
    }

    private int sumValueInList(List<Integer> l1, int sum) {
        return l1.isEmpty() ? sum : sumValueInList(l1.subList(1, l1.size()), sum + l1.getFirst());
    }

    private <T> ArrayList<T> differentList(List<T> l1, List<T> l2) {
        ArrayList<T> result = new ArrayList<>(l1);
        for (T i: l2) {
            result.remove(i);
        }
        return result;
    }
}
