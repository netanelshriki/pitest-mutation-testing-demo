package com.example.pitdemo.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Utility class demonstrating various algorithms and operations
 * that are excellent candidates for mutation testing.
 * 
 * Each method here contains logic that can be mutated to test
 * the effectiveness of our test suite.
 */
public class MathUtils {

    /**
     * Checks if a string is a palindrome.
     * Classic example for mutation testing - demonstrates string operations
     * and recursive logic.
     */
    public static boolean isPalindrome(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        
        String cleanInput = input.toLowerCase().replaceAll("[^a-z0-9]", "");
        
        if (cleanInput.length() <= 1) {
            return true;
        }
        
        char first = cleanInput.charAt(0);
        char last = cleanInput.charAt(cleanInput.length() - 1);
        
        if (first != last) {
            return false;
        }
        
        String middle = cleanInput.substring(1, cleanInput.length() - 1);
        return isPalindrome(middle);
    }

    /**
     * Calculates the factorial of a number.
     * Demonstrates recursive logic and boundary conditions.
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        
        if (n == 0 || n == 1) {
            return 1;
        }
        
        return n * factorial(n - 1);
    }

    /**
     * Calculates the greatest common divisor using Euclidean algorithm.
     * Demonstrates iterative logic and mathematical operations.
     */
    public static int gcd(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("GCD is not defined for negative numbers");
        }
        
        if (b == 0) {
            return a;
        }
        
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        
        return a;
    }

    /**
     * Checks if a number is prime.
     * Demonstrates optimization logic and boundary conditions.
     */
    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        
        if (number == 2) {
            return true;
        }
        
        if (number % 2 == 0) {
            return false;
        }
        
        // Check odd divisors up to sqrt(number)
        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Finds the maximum value in an array.
     * Simple but effective for demonstrating array operations.
     */
    public static int findMax(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        
        return max;
    }

    /**
     * Calculates the average of an array with validation.
     * Demonstrates floating-point operations and validation.
     */
    public static double calculateAverage(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        
        long sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        
        return (double) sum / numbers.length;
    }

    /**
     * Performs binary search on a sorted array.
     * Classic algorithm demonstrating index operations and comparisons.
     */
    public static int binarySearch(int[] sortedArray, int target) {
        if (sortedArray == null) {
            return -1;
        }
        
        int left = 0;
        int right = sortedArray.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (sortedArray[mid] == target) {
                return mid;
            } else if (sortedArray[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1; // Not found
    }

    /**
     * Bubble sort implementation.
     * Demonstrates nested loops and comparison/swap operations.
     */
    public static void bubbleSort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    // Swap elements
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            // If no swapping occurred, array is already sorted
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Calculates Fibonacci number at given position.
     * Demonstrates recursive and mathematical operations.
     */
    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Fibonacci is not defined for negative numbers");
        }
        
        if (n == 0) {
            return 0;
        }
        
        if (n == 1 || n == 2) {
            return 1;
        }
        
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * Checks if a year is a leap year.
     * Demonstrates complex boolean logic with multiple conditions.
     */
    public static boolean isLeapYear(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Year must be positive");
        }
        
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Calculates the distance between two points.
     * Demonstrates mathematical operations and square root.
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Generates prime numbers up to a given limit using Sieve of Eratosthenes.
     * Complex algorithm demonstrating array operations and optimization.
     */
    public static List<Integer> sieveOfEratosthenes(int limit) {
        if (limit < 2) {
            return new ArrayList<>();
        }
        
        boolean[] isPrime = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            isPrime[i] = true;
        }
        
        for (int i = 2; i * i <= limit; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
        
        return primes;
    }

    /**
     * Validates credit card number using Luhn algorithm.
     * Real-world example demonstrating string processing and mathematical validation.
     */
    public static boolean isValidCreditCard(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        
        // Remove spaces and non-digits
        String digits = cardNumber.replaceAll("[^0-9]", "");
        
        if (digits.length() < 13 || digits.length() > 19) {
            return false;
        }
        
        int sum = 0;
        boolean alternate = false;
        
        // Process digits from right to left
        for (int i = digits.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(digits.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
}
