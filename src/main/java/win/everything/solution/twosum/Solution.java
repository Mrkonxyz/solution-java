package win.everything.solution.twosum;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    public int[] twoSum(int[] nums, int target) {

        for (int i = 0 ; i < nums.length ; i++) {
            var targetNumber = target - nums[i];
            var index = findIndexNotEq(nums, targetNumber , i);
            if (index == -1) {
                continue;
            }
            return new int[]{i , index};
        }
        return new int[]{};
    }

    private int findIndexNotEq(int[] arr, int target, int ignore) {
        for (int i = 0; i < arr.length; i++ ) {
            if (i == ignore) {
                continue;
            }
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }


}
