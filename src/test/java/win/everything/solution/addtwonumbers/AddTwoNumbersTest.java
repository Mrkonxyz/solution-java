package win.everything.solution.addtwonumbers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddTwoNumbersTest {

    private AddTwoNumbers solution;

    @BeforeEach
    void setUp() {
        solution = new AddTwoNumbers();
    }

    @Nested
    @DisplayName("Step 1: Single digits without carry")
    class SingleDigitNoCarry {

        @Test
        @DisplayName("[0] + [0] = [0] (Example 2)")
        void shouldAddZeroAndZero() {
            ListNode l1 = ListNode.of(0);
            ListNode l2 = ListNode.of(0);
            ListNode expected = ListNode.of(0);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("[2] + [3] = [5]")
        void shouldAddSingleDigitsWithoutCarry() {
            ListNode l1 = ListNode.of(2);
            ListNode l2 = ListNode.of(3);
            ListNode expected = ListNode.of(5);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Step 2: Single digits with carry (creates extra node)")
    class SingleDigitWithCarry {

        @Test
        @DisplayName("[5] + [5] = [0, 1] (5 + 5 = 10)")
        void shouldHandleCarryCreatingNewNode() {
            ListNode l1 = ListNode.of(5);
            ListNode l2 = ListNode.of(5);
            ListNode expected = ListNode.of(0, 1);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("[9] + [9] = [8, 1] (9 + 9 = 18)")
        void shouldHandleLargestSingleDigitCarry() {
            ListNode l1 = ListNode.of(9);
            ListNode l2 = ListNode.of(9);
            ListNode expected = ListNode.of(8, 1);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Step 3: Same length multiple digits")
    class SameLengthMultipleDigits {

        @Test
        @DisplayName("[2, 4, 3] + [5, 6, 4] = [7, 0, 8] (342 + 465 = 807) (Example 1)")
        void shouldAddSameLengthWithMiddleCarry() {
            ListNode l1 = ListNode.of(2, 4, 3);
            ListNode l2 = ListNode.of(5, 6, 4);
            ListNode expected = ListNode.of(7, 0, 8);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("[5, 5] + [5, 5] = [0, 1, 1] (55 + 55 = 110)")
        void shouldHandleSameLengthEndingWithCarry() {
            ListNode l1 = ListNode.of(5, 5);
            ListNode l2 = ListNode.of(5, 5);
            ListNode expected = ListNode.of(0, 1, 1);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Step 4: Different lengths")
    class DifferentLengths {

        @Test
        @DisplayName("[1, 2] + [3] = [4, 2] (21 + 3 = 24)")
        void shouldAddDifferentLengthsWithoutCarry() {
            ListNode l1 = ListNode.of(1, 2);
            ListNode l2 = ListNode.of(3);
            ListNode expected = ListNode.of(4, 2);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("[1] + [9, 9] = [0, 0, 1] (1 + 99 = 100)")
        void shouldAddDifferentLengthsWithChainedCarry() {
            ListNode l1 = ListNode.of(1);
            ListNode l2 = ListNode.of(9, 9);
            ListNode expected = ListNode.of(0, 0, 1);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("[9,1,6] + [0] = [9, 1, 6] (1 + 99 = 100)")
        void shouldAddDifferentLengthsWithChainedCarry2() {
            ListNode l1 = ListNode.of(9, 1, 6);
            ListNode l2 = ListNode.of(0);
            ListNode expected = ListNode.of(9, 1, 6);

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Step 5: Complex chained carry & large lists")
    class ComplexChainedCarry {

        @Test
        @DisplayName("[9,9,9,9,9,9,9] + [9,9,9,9] = [8,9,9,9,0,0,0,1] (Example 3)")
        void shouldHandleExample3ChainedCarries() {
            ListNode l1 = ListNode.of(9, 9, 9, 9, 9, 9, 9);
            ListNode l2 = ListNode.of(9, 9, 9, 9);
            int[] expectedArray = {8, 9, 9, 9, 0, 0, 0, 1};

            ListNode result = solution.addTwoNumbers(l1, l2);

            assertNotNull(result);
            assertArrayEquals(expectedArray, result.toArray());
        }
    }
}
