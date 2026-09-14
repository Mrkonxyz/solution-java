package win.everything.solution.splittwolisteq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SolutionV2Test {

    private SolutionV2 solution;

    @BeforeEach
    void setUp() {
        solution = new SolutionV2();
    }

    private int sum(List<Integer> list) {
        if (list == null) return 0;
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    private void assertValidEqualSumSplit(List<Integer> original, SolutionV2.TwoList result) {
        assertNotNull(result, "Result TwoList should not be null");
        assertNotNull(result.l1(), "l1 should not be null");
        assertNotNull(result.l2(), "l2 should not be null");

        // 1. Both sublists must have the same sum
        int sum1 = sum(result.l1());
        int sum2 = sum(result.l2());
        assertEquals(sum1, sum2, String.format("Sum of l1 (%d) must equal sum of l2 (%d)", sum1, sum2));

        // 2. Both sublists together must contain all original elements (order-independent multiset check)
        List<Integer> combined = new ArrayList<>(result.l1());
        combined.addAll(result.l2());

        List<Integer> sortedOriginal = new ArrayList<>(original);
        List<Integer> sortedCombined = new ArrayList<>(combined);
        Collections.sort(sortedOriginal);
        Collections.sort(sortedCombined);

        assertEquals(sortedOriginal, sortedCombined,
                "l1 and l2 combined must contain all elements of the original list with exact frequencies");
    }

    @ParameterizedTest(name = "input = {0} -> canSplit = {1}")
    @MethodSource("testData")
    void testAllCases(List<Integer> input, boolean canSplit) {
        SolutionV2.TwoList actual = solution.splitTwoListEq(input);
        if (canSplit) {
            assertValidEqualSumSplit(input, actual);
        } else {
            assertNull(actual, "Expected null when list cannot be split equally");
        }
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(List.of(1, 1), true),
                Arguments.of(List.of(5, 5), true),
                Arguments.of(List.of(0, 0), true),
                Arguments.of(List.of(-5, -5), true),
                Arguments.of(List.of(-5, 5), true),
                Arguments.of(List.of(1, 2, 3), true),
                Arguments.of(List.of(1, 2, 3, 4), true),
                Arguments.of(List.of(1, 5, 11, 5), true),
                Arguments.of(List.of(1, 1, 1, 2, 1), true),
                Arguments.of(List.of(10, 20, 30, 40), true),
                Arguments.of(List.of(2, 2, 2, 2), true),
                Arguments.of(List.of(0, 0, 0, 0), true),
                Arguments.of(List.of(2, 2, 2), false),
                Arguments.of(List.of(3, 3, 3), false),
                Arguments.of(List.of(0, 1, 1), true),
                Arguments.of(List.of(0, 0, 2, 2), true),
                Arguments.of(List.of(0, 0, 0, 0, 0), true),
                Arguments.of(List.of(0, 2, 4), false),
                Arguments.of(List.of(-1, 1, 0), true),
                Arguments.of(List.of(1, -1, 1, -1), true),
                Arguments.of(List.of(-10, -20, -30, -40), true),
                Arguments.of(List.of(-10, 30, 20, -40, -20, 60), true),
                Arguments.of(List.of(-2, -2, -2, -2), true),
                Arguments.of(List.of(-1, -3, -4), true),
                Arguments.of(List.of(1, 2, 4), false),
                Arguments.of(List.of(1, 2, 5), false),
                Arguments.of(List.of(2, 1, 1, 2, 1), false),
                Arguments.of(List.of(100, 1, 2, 3), false),
                Arguments.of(List.of(5), false),               // ตัวเดียว
                Arguments.of(List.of(0), true),               // ตัวเดียวเป็น 0
                Arguments.of(List.of(), false),                // ว่าง
                Arguments.of(null, false),                     // null
                Arguments.of(List.of(-1, -2), false),
                Arguments.of(List.of(-2, -5), false),
                Arguments.of(List.of(6, 5, 3, 2, 2, 2), true),
                Arguments.of(List.of(4, 3, 3, 2, 2, 2), true),
                Arguments.of(List.of(4, 3, 3, 2, 2, 1, 1), true),
                Arguments.of(List.of(7, 1, 3, 2, 3, 6, 7, 1), true),
                Arguments.of(List.of(2, 1, 1, 2, 1, 5), true),
                Arguments.of(List.of(1, 9, 2, 8, 3, 7, 4, 6, 5, 5), true),
                Arguments.of(List.of(-1, -9, -2,-8, -3, -7, -4, -6, -5, -5), true)
        );
    }
}
