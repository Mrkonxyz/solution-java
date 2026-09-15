package win.everything.solution.lengthOfLongestSubstring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @ParameterizedTest(name = "input = {0} -> expect = {1}")
    @MethodSource("testData")
    void testAllCases(String input, int expect) {
        var actual = solution.lengthOfLongestSubstring(input);
        assertEquals(expect , actual);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
//                Arguments.of("abcabcbb", 3),
//                Arguments.of("bbbbb", 1),
//                Arguments.of("pwwkew", 3),
//                Arguments.of("S", 1),
                Arguments.of("1R1T7", 4)
        );
    }

}