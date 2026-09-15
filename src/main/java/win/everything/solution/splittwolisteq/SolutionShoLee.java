package win.everything.solution.splittwolisteq;

import java.util.Arrays;
import java.util.List;

public class SolutionShoLee {


    public boolean splitTwoListEq(List<Integer> org) {
        if (org == null || org.isEmpty()) {
            return false;
        }

        return splitTwoListEq(org, 0 , 0);
    }

    public boolean splitTwoListEq(List<Integer> org, int l, int r) {
        if (org.isEmpty()) {
            return l == r;
        }
        return splitTwoListEq(org.subList(1 , org.size()), l, r + org.getFirst()) || splitTwoListEq(org.subList(1 , org.size()), l + org.getFirst(), r );
    }
}
