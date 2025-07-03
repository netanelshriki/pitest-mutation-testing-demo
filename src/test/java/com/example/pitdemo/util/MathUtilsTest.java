package com.example.pitdemo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * WEAK TESTS for MathUtils - demonstrating poor mutation testing coverage.
 * 
 * These tests achieve good line coverage but terrible mutation coverage.
 * They demonstrate common testing mistakes that mutation testing reveals.
 */
@DisplayName("MathUtils Tests (Weak Version)")
class MathUtilsTest {

    @Test
    @DisplayName("Should check palindrome")
    void shouldCheckPalindrome() {
        // Only tests one positive case, no edge cases
        assertTrue(MathUtils.isPalindrome("racecar"));
        
        // Doesn't test:
        // - Non-palindromes
        // - Empty strings
        // - Single characters
        // - Case sensitivity
        // - Special characters
        // - Null values
    }

    @Test
    @DisplayName("Should calculate factorial")
    void shouldCalculateFactorial() {
        // Only tests one case
        assertEquals(120, MathUtils.factorial(5));
        
        // Doesn't test:
        // - Factorial of 0 and 1 (boundary conditions)
        // - Negative numbers
        // - Large numbers
    }

    @Test
    @DisplayName("Should find GCD")
    void shouldFindGCD() {
        // Only tests one simple case
        assertEquals(6, MathUtils.gcd(12, 18));
        
        // Doesn't test:
        // - When one number is 0
        // - When numbers are equal
        // - When one number divides the other
        // - Edge cases
    }

    @Test
    @DisplayName("Should check if number is prime")
    void shouldCheckIfNumberIsPrime() {
        // Only tests one prime number
        assertTrue(MathUtils.isPrime(7));
        
        // Doesn't test:
        // - Non-prime numbers
        // - Edge cases (0, 1, 2)
        // - Negative numbers
        // - Large primes
    }

    @Test
    @DisplayName("Should find maximum in array")
    void shouldFindMaximumInArray() {
        int[] array = {1, 3, 2, 5, 4};
        
        // Only tests one scenario
        assertEquals(5, MathUtils.findMax(array));
        
        // Doesn't test:
        // - Single element array
        // - Array with duplicates
        // - Array with negative numbers
        // - Array with same elements
    }

    @Test
    @DisplayName("Should calculate average")
    void shouldCalculateAverage() {
        int[] numbers = {2, 4, 6};
        
        // Only tests simple case
        assertEquals(4.0, MathUtils.calculateAverage(numbers));
        
        // Doesn't test:
        // - Single element
        // - Negative numbers
        // - Large numbers that might cause overflow
        // - Precision issues
    }

    @Test
    @DisplayName("Should perform binary search")
    void shouldPerformBinarySearch() {
        int[] array = {1, 2, 3, 4, 5};
        
        // Only tests finding an element that exists
        assertEquals(2, MathUtils.binarySearch(array, 3));
        
        // Doesn't test:
        // - Element not found
        // - First element
        // - Last element
        // - Empty array
        // - Single element array
    }

    @Test
    @DisplayName("Should sort array using bubble sort")
    void shouldSortArrayUsingBubbleSort() {
        int[] array = {3, 1, 4, 1, 5};
        
        MathUtils.bubbleSort(array);
        
        // Weak verification - only checks first element
        assertEquals(1, array[0]);
        
        // Doesn't verify:
        // - Complete sort order
        // - All elements are present
        // - Already sorted array
        // - Empty array
        // - Single element
    }

    @Test
    @DisplayName("Should calculate Fibonacci number")
    void shouldCalculateFibonacci() {
        // Only tests one case
        assertEquals(8, MathUtils.fibonacci(6));
        
        // Doesn't test:
        // - Base cases (0, 1, 2)
        // - Larger numbers
        // - Negative numbers
    }

    @Test
    @DisplayName("Should check leap year")
    void shouldCheckLeapYear() {
        // Only tests one leap year
        assertTrue(MathUtils.isLeapYear(2000));
        
        // Doesn't test:
        // - Non-leap years
        // - Century years that are not leap years
        // - Boundary cases
        // - Invalid years
    }

    @Test
    @DisplayName("Should calculate distance")
    void shouldCalculateDistance() {
        // Only tests simple case
        double distance = MathUtils.calculateDistance(0, 0, 3, 4);
        assertEquals(5.0, distance, 0.01);
        
        // Doesn't test:
        // - Same points (distance 0)
        // - Negative coordinates
        // - Large coordinates
        // - Precision issues
    }

    @Test
    @DisplayName("Should generate primes with sieve")
    void shouldGeneratePrimesWithSieve() {
        var primes = MathUtils.sieveOfEratosthenes(10);
        
        // Only checks size, not content
        assertEquals(4, primes.size());
        
        // Doesn't verify:
        // - Actual prime numbers generated
        // - Order of primes
        // - Edge cases (limit < 2)
        // - Larger limits
    }

    @Test
    @DisplayName("Should validate credit card")
    void shouldValidateCreditCard() {
        // Only tests one valid card
        assertTrue(MathUtils.isValidCreditCard("4532015112830366"));
        
        // Doesn't test:
        // - Invalid cards
        // - Cards with different lengths
        // - Cards with spaces or dashes
        // - Null or empty strings
        // - Non-numeric characters
    }

    @Test
    @DisplayName("Should handle exceptions")
    void shouldHandleExceptions() {
        // Only tests one exception case for factorial
        assertThrows(IllegalArgumentException.class, () -> {
            MathUtils.factorial(-1);
        });
        
        // Doesn't test exceptions for other methods
        // Doesn't test boundary conditions for exceptions
    }
}

/**
 * PROBLEMS WITH THESE TESTS (revealed by mutation testing):
 * 
 * 1. HAPPY PATH ONLY: Only test scenarios where methods work correctly
 * 2. NO BOUNDARY TESTING: Miss edge cases like 0, 1, -1, empty arrays
 * 3. WEAK ASSERTIONS: Use assertTrue/assertFalse instead of exact values
 * 4. INCOMPLETE VERIFICATION: Don't verify all aspects of the result
 * 5. MISSING NEGATIVE CASES: Don't test invalid inputs
 * 6. NO EDGE CASE TESTING: Miss null, empty, single element scenarios
 * 7. POOR MATHEMATICAL VERIFICATION: Don't test mathematical correctness
 * 
 * When you run mutation testing, you'll see:
 * - Conditional boundary mutations survive (>= vs >)
 * - Mathematical operator mutations survive (+, -, *, /)
 * - Return value mutations survive
 * - Conditional mutations survive (&& vs ||)
 * 
 * These weak tests might give you 90%+ line coverage but poor mutation coverage!
 */
