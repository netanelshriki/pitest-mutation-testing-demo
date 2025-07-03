package com.example.pitdemo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IMPROVED TESTS for User model - demonstrating good mutation testing coverage.
 * 
 * These tests are designed to kill mutants and achieve high mutation coverage.
 * They test boundaries, edge cases, and verify exact behavior.
 * 
 * Compare these with UserTest.java to see the difference!
 */
@DisplayName("User Model Tests (Improved Version)")
class UserImprovedTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "test@example.com", LocalDate.of(1990, 1, 1));
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create user with default constructor")
        void shouldCreateUserWithDefaultConstructor() {
            User newUser = new User();
            
            assertNull(newUser.getId());
            assertNull(newUser.getUsername());
            assertNull(newUser.getEmail());
            assertNull(newUser.getBirthDate());
            assertEquals(0, newUser.getScore());
            assertEquals(User.UserStatus.INACTIVE, newUser.getStatus());
        }

        @Test
        @DisplayName("Should create user with parameterized constructor")
        void shouldCreateUserWithParameterizedConstructor() {
            LocalDate birthDate = LocalDate.of(1985, 5, 15);
            User user = new User("john", "john@test.com", birthDate);
            
            assertEquals("john", user.getUsername());
            assertEquals("john@test.com", user.getEmail());
            assertEquals(birthDate, user.getBirthDate());
            assertEquals(0, user.getScore());
            assertEquals(User.UserStatus.INACTIVE, user.getStatus());
        }
    }

    @Nested
    @DisplayName("Age Calculation Tests")
    class AgeCalculationTests {
        
        @Test
        @DisplayName("Should return 0 for null birth date")
        void shouldReturn0ForNullBirthDate() {
            user.setBirthDate(null);
            assertEquals(0, user.getAge());
        }

        @Test
        @DisplayName("Should calculate correct age for various birth dates")
        void shouldCalculateCorrectAge() {
            // Test exact age calculation
            LocalDate today = LocalDate.now();
            LocalDate birthDate = today.minusYears(25);
            user.setBirthDate(birthDate);
            
            assertEquals(25, user.getAge());
        }

        @Test
        @DisplayName("Should handle birthday not yet occurred this year")
        void shouldHandleBirthdayNotYetOccurred() {
            LocalDate today = LocalDate.now();
            LocalDate birthDate = today.minusYears(25).plusDays(1); // Birthday tomorrow
            user.setBirthDate(birthDate);
            
            assertEquals(24, user.getAge()); // Still 24 until birthday
        }
    }

    @Nested
    @DisplayName("Adult Status Tests")
    class AdultStatusTests {
        
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10, 17})
        @DisplayName("Should return false for non-adults")
        void shouldReturnFalseForNonAdults(int yearsAgo) {
            user.setBirthDate(LocalDate.now().minusYears(yearsAgo));
            assertFalse(user.isAdult());
        }

        @ParameterizedTest
        @ValueSource(ints = {18, 19, 25, 50, 100})
        @DisplayName("Should return true for adults")
        void shouldReturnTrueForAdults(int yearsAgo) {
            user.setBirthDate(LocalDate.now().minusYears(yearsAgo));
            assertTrue(user.isAdult());
        }

        @Test
        @DisplayName("Should return false for null birth date")
        void shouldReturnFalseForNullBirthDate() {
            user.setBirthDate(null);
            assertFalse(user.isAdult());
        }
    }

    @Nested
    @DisplayName("Experience Level Tests")
    class ExperienceLevelTests {
        
        @ParameterizedTest
        @CsvSource({
            "0, Novice",
            "10, Novice", 
            "29, Novice",
            "30, Beginner",
            "40, Beginner",
            "49, Beginner",
            "50, Intermediate",
            "60, Intermediate",
            "69, Intermediate",
            "70, Advanced",
            "80, Advanced",
            "89, Advanced",
            "90, Expert",
            "95, Expert",
            "100, Expert"
        })
        @DisplayName("Should return correct experience level for score")
        void shouldReturnCorrectExperienceLevel(int score, String expectedLevel) {
            user.setScore(score);
            assertEquals(expectedLevel, user.getExperienceLevel());
        }

        @Test
        @DisplayName("Should throw exception for negative score")
        void shouldThrowExceptionForNegativeScore() {
            user.setScore(-1);
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, 
                () -> user.getExperienceLevel()
            );
            assertEquals("Score cannot be negative", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Score Update Tests")
    class ScoreUpdateTests {
        
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 25, 49, 50, 51, 75, 99, 100})
        @DisplayName("Should accept valid scores")
        void shouldAcceptValidScores(int score) {
            assertDoesNotThrow(() -> user.updateScore(score));
            assertEquals(score, user.getScore());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -10, 101, 150})
        @DisplayName("Should reject invalid scores")
        void shouldRejectInvalidScores(int score) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> user.updateScore(score)
            );
            assertEquals("Score must be between 0 and 100", exception.getMessage());
        }

        @Test
        @DisplayName("Should activate user when score reaches 50")
        void shouldActivateUserWhenScoreReaches50() {
            user.setStatus(User.UserStatus.INACTIVE);
            user.updateScore(50);
            
            assertEquals(User.UserStatus.ACTIVE, user.getStatus());
        }

        @Test
        @DisplayName("Should not change status if already active")
        void shouldNotChangeStatusIfAlreadyActive() {
            user.setStatus(User.UserStatus.ACTIVE);
            user.updateScore(75);
            
            assertEquals(User.UserStatus.ACTIVE, user.getStatus());
        }

        @Test
        @DisplayName("Should not activate suspended or deleted users")
        void shouldNotActivateSuspendedOrDeletedUsers() {
            user.setStatus(User.UserStatus.SUSPENDED);
            user.updateScore(75);
            assertEquals(User.UserStatus.SUSPENDED, user.getStatus());

            user.setStatus(User.UserStatus.DELETED);
            user.updateScore(75);
            assertEquals(User.UserStatus.DELETED, user.getStatus());
        }
    }

    @Nested
    @DisplayName("Promotion Eligibility Tests")
    class PromotionEligibilityTests {
        
        @Test
        @DisplayName("Should allow promotion when all conditions met")
        void shouldAllowPromotionWhenAllConditionsMet() {
            user.setStatus(User.UserStatus.ACTIVE);
            user.setScore(75);
            user.setBirthDate(LocalDate.now().minusYears(25)); // Adult
            
            assertTrue(user.canBePromoted());
        }

        @Test
        @DisplayName("Should not allow promotion for inactive users")
        void shouldNotAllowPromotionForInactiveUsers() {
            user.setStatus(User.UserStatus.INACTIVE);
            user.setScore(75);
            user.setBirthDate(LocalDate.now().minusYears(25));
            
            assertFalse(user.canBePromoted());
        }

        @Test
        @DisplayName("Should not allow promotion for low scores")
        void shouldNotAllowPromotionForLowScores() {
            user.setStatus(User.UserStatus.ACTIVE);
            user.setScore(74); // Just below threshold
            user.setBirthDate(LocalDate.now().minusYears(25));
            
            assertFalse(user.canBePromoted());
        }

        @Test
        @DisplayName("Should not allow promotion for non-adults")
        void shouldNotAllowPromotionForNonAdults() {
            user.setStatus(User.UserStatus.ACTIVE);
            user.setScore(75);
            user.setBirthDate(LocalDate.now().minusYears(17)); // Not adult
            
            assertFalse(user.canBePromoted());
        }
    }

    @Nested
    @DisplayName("Bonus Calculation Tests")
    class BonusCalculationTests {
        
        @Test
        @DisplayName("Should calculate bonus for inactive non-adult with low score")
        void shouldCalculateBonusForInactiveNonAdultWithLowScore() {
            user.setStatus(User.UserStatus.INACTIVE);
            user.setBirthDate(LocalDate.now().minusYears(16)); // Not adult
            user.setScore(30);
            
            assertEquals(0, user.calculateBonus());
        }

        @Test
        @DisplayName("Should calculate bonus for active adult with high score")
        void shouldCalculateBonusForActiveAdultWithHighScore() {
            user.setStatus(User.UserStatus.ACTIVE);
            user.setBirthDate(LocalDate.now().minusYears(25)); // Adult
            user.setScore(85);
            
            // Expected: (10 + 85*2) * 2 = 360
            assertEquals(360, user.calculateBonus());
        }

        @Test
        @DisplayName("Should calculate bonus for active adult with medium score")
        void shouldCalculateBonusForActiveAdultWithMediumScore() {
            user.setStatus(User.UserStatus.ACTIVE);
            user.setBirthDate(LocalDate.now().minusYears(25)); // Adult
            user.setScore(60);
            
            // Expected: (10 + 60) * 2 = 140
            assertEquals(140, user.calculateBonus());
        }

        @Test
        @DisplayName("Should test score boundary conditions for bonus")
        void shouldTestScoreBoundaryConditionsForBonus() {
            user.setStatus(User.UserStatus.ACTIVE);
            user.setBirthDate(LocalDate.now().minusYears(25));
            
            // Test score = 80 (boundary)
            user.setScore(80);
            assertEquals(160, user.calculateBonus()); // (10 + 80) * 2
            
            // Test score = 81 (just above boundary)
            user.setScore(81);
            assertEquals(344, user.calculateBonus()); // (10 + 81*2) * 2
            
            // Test score = 50 (boundary)
            user.setScore(50);
            assertEquals(120, user.calculateBonus()); // (10 + 50) * 2
            
            // Test score = 49 (just below boundary)
            user.setScore(49);
            assertEquals(20, user.calculateBonus()); // (10 + 0) * 2
        }
    }

    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {
        
        @ParameterizedTest
        @ValueSource(strings = {
            "test@example.com",
            "user.name@domain.org",
            "test123@test.co.uk",
            "a@b.c"
        })
        @DisplayName("Should validate correct email formats")
        void shouldValidateCorrectEmailFormats(String email) {
            user.setEmail(email);
            assertTrue(user.hasValidEmailFormat());
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "",
            "   ",
            "invalid",
            "@domain.com",
            "user@",
            "user.domain.com",
            "user@domain",
            ".@domain.com",
            "user@.com"
        })
        @DisplayName("Should reject invalid email formats")
        void shouldRejectInvalidEmailFormats(String email) {
            user.setEmail(email);
            assertFalse(user.hasValidEmailFormat());
        }

        @Test
        @DisplayName("Should handle null email")
        void shouldHandleNullEmail() {
            user.setEmail(null);
            assertFalse(user.hasValidEmailFormat());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {
        
        @Test
        @DisplayName("Should be equal when id and username are same")
        void shouldBeEqualWhenIdAndUsernameAreSame() {
            User user1 = new User("test", "test1@example.com", LocalDate.of(1990, 1, 1));
            User user2 = new User("test", "test2@example.com", LocalDate.of(1985, 1, 1));
            user1.setId(1L);
            user2.setId(1L);
            
            assertEquals(user1, user2);
            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when username differs")
        void shouldNotBeEqualWhenUsernameDiffers() {
            User user1 = new User("test1", "test@example.com", LocalDate.of(1990, 1, 1));
            User user2 = new User("test2", "test@example.com", LocalDate.of(1990, 1, 1));
            
            assertNotEquals(user1, user2);
        }

        @Test
        @DisplayName("Should handle null comparisons")
        void shouldHandleNullComparisons() {
            assertNotEquals(user, null);
            assertEquals(user, user); // self-equality
        }
    }
}

/**
 * IMPROVEMENTS IN THESE TESTS:
 * 
 * 1. BOUNDARY TESTING: Test exact boundaries (17/18, 49/50, 74/75, etc.)
 * 2. COMPREHENSIVE SCENARIOS: Test all combinations of conditions
 * 3. EXACT ASSERTIONS: Use assertEquals instead of assertTrue
 * 4. EDGE CASE TESTING: Test null values, empty strings, extremes
 * 5. PARAMETERIZED TESTS: Test multiple values efficiently
 * 6. MATHEMATICAL VERIFICATION: Verify exact calculations
 * 7. STATE VALIDATION: Verify all state changes
 * 8. EXCEPTION TESTING: Test all exception scenarios with message validation
 * 
 * These tests should achieve much higher mutation coverage!
 */
