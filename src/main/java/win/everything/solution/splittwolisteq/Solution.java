package win.everything.solution.splittwolisteq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Solution {

    public record TwoList(List<Integer> l1, List<Integer> l2) {
    }

    public TwoList splitTwoListEq(List<Integer> org) {

        return Optional.ofNullable(org)
                .filter(this::isNotEmpty)
                .map(this::sumValueInList)
                .filter(this::isEven)
                .map(this::minusTwo)
                .map(mid -> findCorrectList(org, mid, initialFirstMap(org)))
                .map(toResult(org))
                .orElse(null);
    }

    private  Map<Integer, ArrayList<Integer>> initialFirstMap(List<Integer> org) {
          Map<Integer, ArrayList<Integer>> initialPossibleList = new HashMap<>();
          initialPossibleList.put(sumValueInList(List.of(org.getFirst())), new ArrayList<>(List.of(org.getFirst())));
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

    private  List<Integer> findCorrectList(List<Integer> org, int target, Map<Integer, ArrayList<Integer>> mapPossibleList) {
       return mapPossibleList.isEmpty() ? null
               : Optional.ofNullable(getCorrectList(target, mapPossibleList))
                .orElse(findCorrectList(org, target, generatePossibleList(org, target, mapPossibleList)));
    }

    private List<Integer> getCorrectList(int target, Map<Integer,  ArrayList<Integer>> mapPossibleList) {
        return mapPossibleList.getOrDefault(target, null);
    }

    private Map<Integer, ArrayList<Integer>> generatePossibleList(List<Integer> org, int target,  Map<Integer, ArrayList<Integer>> possibleList) {
        return possibleList
                .values()
                .stream()
                .map(baseList -> findPossibleList(baseList, differentList(org, baseList), target))
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing , replacement) -> replacement));
    }

    private Map<Integer, ArrayList<Integer>> findPossibleList(List<Integer> baseList, List<Integer> remainingVal, int target) {
        var newAndAdd = newArrayAndAndVal(baseList);
        var isPossible = possibleListCondition(target);
        return remainingVal
                .stream()
                .map(newAndAdd)
                .filter(isPossible)
                .collect(Collectors.toMap(
                        this::sumValueInList,
                        v-> v,
                        (existing, replacement) -> replacement)
                );
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
