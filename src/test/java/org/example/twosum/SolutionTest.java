package org.example.twosum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    private Solution solution;

    @BeforeEach
     void setUp() {
        solution = new Solution();
    }

    @ParameterizedTest(name = "n = {0} -> expected commas = {1}" )
    @MethodSource("testData")
    void testAllCases(int[] input, int target, int[] expected) {
        var actual = solution.twoSum(input, target);
        assertEquals(expected[0],actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(new int[] {2,7,11,15} , 9 , new int[]{0 ,1}),
                Arguments.of(new int[] {3,2,4} , 6 , new int[]{1 ,2}),
                Arguments.of(new int[] {3 , 3} , 6 , new int[]{0 ,1}),
                Arguments.of(new int[] {-1,-2,-3,-4,-5} , -8 , new int[]{2 ,4}),
                Arguments.of(new int[] {-3 , 4, 3, 90} , 0 , new int[]{0 , 2})
        );
    }
}