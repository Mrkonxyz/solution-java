package win.everything.solution.countCommas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CountCommasTest {

    private CountCommas countCommas;

    @BeforeEach
    void setUp() {
        countCommas = new CountCommas();
    }

    @Nested
    @DisplayName("Step 1: Numbers under 1,000 (0 commas)")
    class NumbersUnder1000 {

        @Test
        @DisplayName("n = 1 should return 0")
        void shouldReturnZeroForOne() {
            assertEquals(0, countCommas.count(1));
        }

        @Test
        @DisplayName("n = 998 should return 0 (Example 2)")
        void shouldReturnZeroFor998() {
            assertEquals(0, countCommas.count(998));
        }

        @Test
        @DisplayName("n = 999 should return 0 (Boundary)")
        void shouldReturnZeroFor999() {
            assertEquals(0, countCommas.count(999));
        }
    }

    @Nested
    @DisplayName("Step 2: First comma boundary (1,000 to 9,999)")
    class FirstCommaBoundary {

        @Test
        @DisplayName("n = 1000 should return 1 (First number with comma: '1,000')")
        void shouldReturnOneFor1000() {
            assertEquals(1, countCommas.count(1000));
        }

        @Test
        @DisplayName("n = 1002 should return 3 ('1,000', '1,001', '1,002') (Example 1)")
        void shouldReturnThreeFor1002() {
            assertEquals(3, countCommas.count(1002));
        }

        @Test
        @DisplayName("n = 9999 should return 9000 (1000 to 9999 has 9000 numbers)")
        void shouldReturn9000For9999() {
            assertEquals(9000, countCommas.count(9999));
        }
    }

    @Nested
    @DisplayName("Step 3: 5-digit & 6-digit numbers (1 comma per number)")
    class LargerSingleCommaNumbers {

        @Test
        @DisplayName("n = 100000 should return 99001 (Constraint: 10^5)")
        void shouldReturn99001For100000() {
            // 100,000 - 1,000 + 1 = 99,001
            assertEquals(99001, countCommas.count(100000));
        }

        @Test
        @DisplayName("n = 999999 should return 999000 (Boundary before 1,000,000)")
        void shouldReturn999000For999999() {
            // 999,999 - 1,000 + 1 = 999,000
            assertEquals(999000, countCommas.count(999999));
        }
    }

    @Nested
    @DisplayName("Step 4: Millions and above (2+ commas)")
    class MillionsBoundary {

        @Test
        @DisplayName("n = 1000000 should return 999002 (999000 with 1 comma + '1,000,000' with 2 commas)")
        void shouldReturn999002For1000000() {
            assertEquals(999002, countCommas.count(1000000));
        }

        @Test
        @DisplayName("n = 1000002 should return 999006")
        void shouldReturn999006For1000002() {
            assertEquals(999006, countCommas.count(1000002));
        }
    }

    @ParameterizedTest(name = "n = {0} -> expected commas = {1}")
    @CsvSource({
        "1, 0",
        "998, 0",
        "999, 0",
        "1000, 1",
        "1002, 3",
        "9999, 9000",
        "100000, 99001",
        "999999, 999000",
        "1000000, 999002",
        "1000002, 999006"
    })
    @DisplayName("Regression / Parameterized Test")
    void testAllCases(int n, int expected) {
        assertEquals(expected, countCommas.count(n));
    }
}
