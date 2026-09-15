package win.everything.solution.splittwolisteq;

import win.everything.solution.splittwolisteq.Solution.TwoList;
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

class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    private int sum(List<Integer> list) {
        if (list == null) return 0;
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    private void assertValidEqualSumSplit(List<Integer> original, TwoList result) {
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
        var actual = solution.splitTwoListEq(input);
        if (canSplit) {
            assertValidEqualSumSplit(input, actual);
        } else {
            assertNull(actual, "Expected null when list cannot be split equally");
        }
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                // -------------------------------------------------------------
                // หมวด 1: Minimal cases (2 elements)
                // -------------------------------------------------------------
                Arguments.of(List.of(-1, -9, -2,-8, -3, -7, -4, -6, -5, -5), true),
                Arguments.of(List.of(1, 1), true),
                Arguments.of(List.of(5, 5), true),
                Arguments.of(List.of(0, 0), true),
                Arguments.of(List.of(-5, -5), true),
                Arguments.of(List.of(-5, 5), true),   // ผลรวมเป็น 0: {-5, 5} -> sum 0

                // -------------------------------------------------------------
                // หมวด 2: 3 & 4 elements
                // -------------------------------------------------------------
                Arguments.of(List.of(1, 2, 3), true),
                Arguments.of(List.of(1, 2, 3, 4), true),
                Arguments.of(List.of(1, 5, 11, 5), true),

                // -------------------------------------------------------------
                // หมวด 3: Multi-element lists & Non-contiguous subsets
                // -------------------------------------------------------------
                Arguments.of(List.of(1, 1, 1, 2, 1), true),
                Arguments.of(List.of(10, 20, 30, 40), true),
                Arguments.of(List.of(2, 1, 1, 2, 1, 5), true),
                Arguments.of(List.of(1, 9, 2, 8, 3, 7, 4, 6, 5, 5), true),

                // -------------------------------------------------------------
                // หมวด 4: สมาชิกซ้ำกันทั้งหมด (All Same Elements)
                // -------------------------------------------------------------
                Arguments.of(List.of(2, 2, 2, 2), true),       // จำนวนคู่: แบ่งครึ่งได้ {2, 2}, {2, 2}
                Arguments.of(List.of(0, 0, 0, 0), true),
                Arguments.of(List.of(2, 2, 2), false),         // จำนวนคี่แม้ผลรวมเป็นเลขคู่ (sum=6, target=3 แต่ไม่มีตัวไหนรวมได้ 3)
                Arguments.of(List.of(3, 3, 3), false),         // ผลรวมเป็นเลขคี่ (sum=9)

                // -------------------------------------------------------------
                // หมวด 5: มี 0 ปนกับตัวเลขอื่น (Zeros mixed with numbers)
                // -------------------------------------------------------------
                Arguments.of(List.of(0, 1, 1), true),          // {1} และ {0, 1}
                Arguments.of(List.of(0, 0, 2, 2), true),
                Arguments.of(List.of(0, 0, 0, 0, 0), true),
                Arguments.of(List.of(0, 2, 4), false),         // sum=6, target=3 ไม่มีคู่ที่รวมได้ 3

                // -------------------------------------------------------------
                // หมวด 6: Negative numbers and mixed signs
                // -------------------------------------------------------------
                Arguments.of(List.of(-1, 1, 0), true),
                Arguments.of(List.of(1, -1, 1, -1), true),
                Arguments.of(List.of(-10, -20, -30, -40), true),
                Arguments.of(List.of(-10, 30, 20, -40, -20, 60), true),
                Arguments.of(List.of(-2, -2, -2, -2), true),
                Arguments.of(List.of(-1, -3, -4), true),       // sum=-8, target=-4: {-4} และ {-1, -3}

                // -------------------------------------------------------------
                // หมวด 7: Edge cases ที่เป็นไปไม่ได้ (Expected: null)
                // -------------------------------------------------------------
                Arguments.of(List.of(1, 2, 4), false),         // sum=7 (odd)
                Arguments.of(List.of(1, 2, 5), false),         // sum=8 (even but impossible)
                Arguments.of(List.of(2, 1, 1, 2, 1), false),   // sum=7
                Arguments.of(List.of(100, 1, 2, 3), false),    // ตัวเลขตัวหนึ่งใหญ่กว่าผลรวมตัวที่เหลือทั้งหมด
                Arguments.of(List.of(5), false),               // ตัวเดียว
                Arguments.of(List.of(0), true),               // ตัวเดียวเป็น 0
                Arguments.of(List.of(), false),                // ว่าง
                Arguments.of(null, false),                     // null

                // -------------------------------------------------------------
                // หมวด 8: ท้าทาย Bug ของ Negative Odd Sum (เช่น -3, -7)
                // (ใน Java: -3 % 2 == -1 ซึ่งไม่ใช่ 1 ทำให้ if (isOdd(sum)) มองว่าเป็นเลขคู่)
                // -------------------------------------------------------------
                Arguments.of(List.of(-1, -2), false),
                Arguments.of(List.of(-2, -5), false),

                // -------------------------------------------------------------
                // หมวด 9: ท้าทาย Greedy vs Backtracking (Partition Problem)
                // เคสที่แบ่งได้จริง แต่ Greedy จะเลือกตัวมากสุดก่อนแล้วทางตัน
                // -------------------------------------------------------------
                Arguments.of(List.of(6, 5, 3, 2, 2, 2), true),
                Arguments.of(List.of(4, 3, 3, 2, 2, 2), true),
                // -------------------------------------------------------------
                // หมวด : ท้าทาย Greedy vs Backtracking (Partition Problem)
                // เคสที่แบ่งได้จริง แต่ Greedy จะเลือกตัวมากสุดก่อนแล้วทางตัน
                // -------------------------------------------------------------
                Arguments.of(List.of(4, 3, 3, 2, 2, 1, 1), true),
                Arguments.of(List.of(7, 1, 3, 2, 3, 6, 7, 1), true),
                Arguments.of(List.of(-7, -1, -3, -2, -3, -6, -7, -1), true)
        );
    }
}
