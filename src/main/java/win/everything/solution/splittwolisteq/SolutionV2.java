package win.everything.solution.splittwolisteq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SolutionV2 {

    public record TwoList(List<Integer> l1,List<Integer> l2) {
    }

    public TwoList splitTwoListEq(List<Integer> org) {
        return Optional.ofNullable(org)
                .filter(this::isNotEmpty)
                .map(this::sumValueInList)
                .filter(this::isEven)
                .map(this::minusTwo)
                .map( v -> recursion(v - org.getFirst(), new ArrayList<>(org.subList(1, org.size())), new ArrayList<>(List.of(org.getFirst()))))
                .map(v -> new TwoList(v, differentList(org, v)) )
                .orElse(null);
    }

    private Integer minusTwo(Integer n) {
        return n / 2;
    }

    private List<Integer> recursion(int target, ArrayList<Integer> list, ArrayList<Integer> correctList) {
         if (target == 0) {
             return correctList;
         }

        var val = list
                .stream()
                .filter(v -> {
                    var temp = target - v;
                    var nl =  new ArrayList<>(list);
                    nl.remove(v);
                   return exitsInList(nl, temp) || temp == 0;
                })
                .findFirst()
                .orElse(null);

        if (val == null) {
            return null;
        }
        correctList.add(val);
        list.remove(val);
        return recursion(target - val, list ,correctList);
    }

    private boolean exitsInList(List<Integer> list , int target) {
        return Optional.of(list)
                .map(l -> l
                        .stream()
                        .filter(v -> v == target)
                        .toList()
                )
                .map(l -> !l.isEmpty())
                .orElse(false);
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

    private boolean isEven(int n) {
        n = n < 0 ? n * -1 : n;
        return n % 2 == 0;
    }

    private boolean isNotEmpty(List<Integer> l) {
        return Optional.of(l)
                .map(v -> !v.isEmpty())
                .orElse(false);
    }

}
