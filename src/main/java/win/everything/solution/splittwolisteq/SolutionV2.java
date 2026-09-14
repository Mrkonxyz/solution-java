package win.everything.solution.splittwolisteq;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class SolutionV2 {

    public record TwoList(List<Integer> l1,List<Integer> l2) {
    }

    public TwoList splitTwoListEq(List<Integer> org) {

        if (Objects.isNull(org) || org.isEmpty()) {
            return null;
        }

        int target = sumValueInList(org) / 2;
        var correctList = recursion(target, new ArrayList<>(org), new ArrayList<>());
        if (correctList == null) {
            return null;
        }

        return new TwoList(correctList, differentList(org, correctList));
    }

    private List<Integer> recursion(int target, ArrayList<Integer> list, ArrayList<Integer> correctList) {
         if (sumValueInList(correctList) == target) {
             return correctList;
         }

        var first = list.getFirst();
        target = target - first;
        int finalTarget = target;
        var val = list
                .stream()
                .filter(l -> l <= finalTarget)
                .findFirst()
                .orElse(null);

        if (val == null) {
            return null;
        }
        correctList.add(val);
        list.remove(val);
        return recursion(target, list ,correctList);
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
