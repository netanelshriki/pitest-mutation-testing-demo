package com.example.pitdemo.service;

import com.example.pitdemo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WEAK TESTS for UserService - demonstrating poor mutation testing coverage.
 * 
 * These tests achieve decent line coverage but will have poor mutation coverage,
 * showing the difference between traditional coverage and mutation testing.
 */
@DisplayName("UserService Tests (Weak Version)")
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        // Only tests successful creation, no edge cases
        User user = userService.createUser("testuser", "test@example.com", 
                                         LocalDate.of(1990, 1, 1));
        
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        
        // Doesn't test:
        // - Invalid usernames
        // - Duplicate usernames
        // - Invalid emails
        // - Future birth dates
        // - Status assignment logic
    }

    // Added after running mutation testing
//    @Test
//    @DisplayName("Should validate user input and throw exception for invalid data")
//    void shouldValidateUserInputAndThrowExceptionForInvalidData() {
//        // Test that validateUserInput actually gets called and works
//        // This will kill the "removed call to validateUserInput" mutation
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            userService.createUser(null, "test@example.com", LocalDate.of(1990, 1, 1));
//        }, "Should throw exception for null username");
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            userService.createUser("testuser", null, LocalDate.of(1990, 1, 1));
//        }, "Should throw exception for null email");
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            userService.createUser("testuser", "test@example.com", null);
//        }, "Should throw exception for null birthDate");
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            userService.createUser("", "test@example.com", LocalDate.of(1990, 1, 1));
//        }, "Should throw exception for empty username");
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            userService.createUser("testuser", "", LocalDate.of(1990, 1, 1));
//        }, "Should throw exception for empty email");
//    }

    @Test
    @DisplayName("Should update user score")
    void shouldUpdateUserScore() {
        userService.createUser("testuser", "test@example.com", 
                             LocalDate.of(1990, 1, 1));
        
        // Only tests one valid score update - but this will trigger bonus logic!
        // Since oldScore=0 < 50 and newScore=75 >= 50, bonus of 10 will be added
        userService.updateUserScore("testuser", 75);
        
        User user = userService.getAllUsers().get(0);
        assertEquals(85, user.getScore()); // 75 + 10 bonus = 85
        
        // Doesn't test:
        // - Score boundaries (49, 50, 51)
        // - Bonus calculations
        // - Status changes
        // - Invalid scores
        // - Non-existent users
    }

    @Test
    @DisplayName("Should calculate user ranking")
    void shouldCalculateUserRanking() {
        // Create user with a score > 0 to ensure ranking > 0
        User user = userService.createUser("testuser", "test@example.com", 
                                         LocalDate.of(1990, 1, 1));
        // Set score to ensure ranking is > 0
        user.setScore(50); // This will make ranking > 0
        
        // Now calculate ranking
        double ranking = userService.calculateUserRanking("testuser");
        assertTrue(ranking > 0);
        
        // Doesn't test:
        // - Different user statuses
        // - Different experience levels
        // - Adult vs non-adult
        // - Mathematical calculations
        // - Non-existent users
    }

    @Test
    @DisplayName("Should find users by age range")
    void shouldFindUsersByAgeRange() {
        userService.createUser("user1", "user1@example.com", 
                             LocalDate.of(1990, 1, 1));
        userService.createUser("user2", "user2@example.com", 
                             LocalDate.of(2010, 1, 1));
        
        // Only tests one range
        var users = userService.findUsersByAgeRange(20, 40);
        assertEquals(1, users.size());
        
        // Doesn't test:
        // - Invalid ranges (min > max)
        // - Negative ages
        // - Boundary conditions
        // - Empty results
    }

    @Test
    @DisplayName("Should generate statistics")
    void shouldGenerateStatistics() {
        userService.createUser("user1", "user1@example.com", 
                             LocalDate.of(1990, 1, 1));
        
        // Only tests with one user
        var stats = userService.generateStatistics();
        assertEquals(1, stats.getTotalUsers());
        
        // Doesn't test:
        // - Empty user list
        // - Multiple users with different statuses
        // - Average calculations
        // - Different scenarios
    }

    @Test
    @DisplayName("Should validate username")
    void shouldValidateUsername() {
        // Only tests one valid username
        assertTrue(userService.isValidUsername("validuser"));
        
        // Doesn't test:
        // - Invalid usernames
        // - Boundary lengths
        // - Special characters
        // - Null/empty values
        // - Character composition rules
    }

    @Test
    @DisplayName("Should generate user ID")
    void shouldGenerateUserId() {
        // Only tests one successful generation
        String id = userService.generateUserId("testuser", "test@example.com");
        assertNotNull(id);
        assertTrue(id.startsWith("USR"));
        
        // Doesn't test:
        // - Null inputs
        // - Empty strings
        // - ID uniqueness
        // - Mathematical operations
    }

    @Test
    @DisplayName("Should check premium access")
    void shouldCheckPremiumAccess() {
        userService.createUser("testuser", "test@example.com", 
                             LocalDate.of(1990, 1, 1));
        userService.updateUserScore("testuser", 85);
        
        // Only tests one scenario
        boolean canAccess = userService.canAccessPremiumFeatures("testuser");
        assertTrue(canAccess);
        
        // Doesn't test:
        // - Different age groups
        // - Different score requirements
        // - Non-active users
        // - Prime number logic
        // - Boundary conditions
    }

    @Test
    @DisplayName("Should handle duplicate username")
    void shouldHandleDuplicateUsername() {
        userService.createUser("testuser", "test1@example.com", 
                             LocalDate.of(1990, 1, 1));
        
        // Only tests duplicate username exception
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("testuser", "test2@example.com", 
                                 LocalDate.of(1990, 1, 1));
        });
        
        // Doesn't test duplicate email scenario
    }

    @Test
    @DisplayName("Should handle invalid score update")
    void shouldHandleInvalidScoreUpdate() {
        userService.createUser("testuser", "test@example.com", 
                             LocalDate.of(1990, 1, 1));
        
        // Only tests one invalid score
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserScore("testuser", 150);
        });
        
        // Doesn't test:
        // - Negative scores
        // - Boundary values (100, 101)
        // - Non-existent users
    }

    @Test
    @DisplayName("Should handle score update without bonus")
    void shouldHandleScoreUpdateWithoutBonus() {
        userService.createUser("testuser", "test@example.com", 
                             LocalDate.of(1990, 1, 1));
        
        // First update to 60 (triggers bonus: 60 + 10 = 70)
        userService.updateUserScore("testuser", 60);
        User user = userService.getAllUsers().get(0);
        assertEquals(70, user.getScore());
        
        // Second update to 80 (no bonus since oldScore >= 50)
        userService.updateUserScore("testuser", 80);
        assertEquals(80, user.getScore());
    }
}

/**
 * PROBLEMS WITH THESE TESTS (revealed by mutation testing):
 * 
 * 1. SINGLE SCENARIO TESTING: Only test one happy path scenario
 * 2. WEAK ASSERTIONS: Use assertTrue instead of exact value comparisons
 * 3. MISSING BOUNDARY TESTS: Don't test edge values and boundaries
 * 4. INCOMPLETE EXCEPTION TESTING: Only test one exception case per method
 * 5. NO COMPLEX LOGIC TESTING: Don't verify mathematical calculations
 * 6. MISSING STATE VALIDATION: Don't verify state changes properly
 * 7. NO COMBINATION TESTING: Don't test different combinations of conditions
 * 
 * Mutation testing will reveal that these tests allow many mutants to survive:
 * - Conditional boundary mutations (>= vs >, < vs <=)
 * - Mathematical operator mutations (+, -, *, /)
 * - Boolean operator mutations (&& vs ||)
 * - Return value mutations
 * - Conditional negation mutations
 * 
 * This demonstrates why high line coverage doesn't guarantee good tests!
 */
