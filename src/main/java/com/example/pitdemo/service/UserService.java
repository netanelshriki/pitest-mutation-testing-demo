package com.example.pitdemo.service;

import com.example.pitdemo.model.User;
import com.example.pitdemo.util.MathUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * User service demonstrating complex business logic for mutation testing.
 * 
 * This service combines various operations and validations that make
 * excellent candidates for revealing weak spots in our test suite.
 */
@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    /**
     * Creates a new user with validation.
     * Demonstrates parameter validation and business rules.
     */
    public User createUser(String username, String email, LocalDate birthDate) {
        validateUserInput(username, email, birthDate);
        
        if (usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        
        User user = new User(username, email, birthDate);
        
        // Auto-activate adults with valid email
        if (user.isAdult() && user.hasValidEmailFormat()) {
            user.setStatus(User.UserStatus.ACTIVE);
        }
        
        users.add(user);
        return user;
    }

    /**
     * Updates user score with business rules.
     * Demonstrates state transitions and complex validation.
     */
    public void updateUserScore(String username, int newScore) {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        
        int oldScore = user.getScore();
        user.updateScore(newScore);
        
        // Apply business rules based on score changes
        if (oldScore < 50 && newScore >= 50) {
            // Newly qualified user gets bonus points
            int bonus = calculateQualificationBonus(user);
            if (newScore + bonus <= 100) {
                user.setScore(newScore + bonus);
            }
        }
        
        // Suspend users with very low scores
        if (newScore < 10 && user.getStatus() == User.UserStatus.ACTIVE) {
            user.setStatus(User.UserStatus.SUSPENDED);
        }
    }

    /**
     * Calculates a complex user ranking based on multiple factors.
     * Demonstrates mathematical operations and conditional logic.
     */
    public double calculateUserRanking(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            return 0.0;
        }
        
        double ranking = user.getScore();
        
        // Age factor - adults get bonus
        if (user.isAdult()) {
            ranking *= 1.2;
        } else {
            ranking *= 0.8;
        }
        
        // Status factor
        switch (user.getStatus()) {
            case ACTIVE:
                ranking *= 1.5;
                break;
            case INACTIVE:
                ranking *= 0.5;
                break;
            case SUSPENDED:
                ranking *= 0.1;
                break;
            case DELETED:
                ranking = 0.0;
                break;
        }
        
        // Experience level multiplier
        String level = user.getExperienceLevel();
        switch (level) {
            case "Expert":
                ranking *= 2.0;
                break;
            case "Advanced":
                ranking *= 1.7;
                break;
            case "Intermediate":
                ranking *= 1.3;
                break;
            case "Beginner":
                ranking *= 1.0;
                break;
            case "Novice":
                ranking *= 0.7;
                break;
        }
        
        return Math.round(ranking * 100.0) / 100.0; // Round to 2 decimal places
    }

    /**
     * Finds users by age range with validation.
     * Demonstrates filtering and boundary conditions.
     */
    public List<User> findUsersByAgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        
        if (minAge > maxAge) {
            throw new IllegalArgumentException("Minimum age cannot be greater than maximum age");
        }
        
        return users.stream()
                .filter(user -> {
                    int age = user.getAge();
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());
    }

    /**
     * Generates a user report with statistics.
     * Demonstrates aggregation operations and mathematical calculations.
     */
    public UserStatistics generateStatistics() {
        if (users.isEmpty()) {
            return new UserStatistics(0, 0.0, 0.0, 0, 0, 0);
        }
        
        int totalUsers = users.size();
        int activeUsers = (int) users.stream()
                .filter(user -> user.getStatus() == User.UserStatus.ACTIVE)
                .count();
        
        double averageScore = users.stream()
                .mapToInt(User::getScore)
                .average()
                .orElse(0.0);
        
        double averageAge = users.stream()
                .mapToInt(User::getAge)
                .average()
                .orElse(0.0);
        
        int adultUsers = (int) users.stream()
                .filter(User::isAdult)
                .count();
        
        int promotableUsers = (int) users.stream()
                .filter(User::canBePromoted)
                .count();
        
        return new UserStatistics(totalUsers, averageScore, averageAge, 
                                activeUsers, adultUsers, promotableUsers);
    }

    /**
     * Validates username format using mathematical approach.
     * Demonstrates string validation with mathematical concepts.
     */
    public boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = username.trim();
        
        // Length check
        if (trimmed.length() < 3 || trimmed.length() > 50) {
            return false;
        }
        
        // Must start with letter
        if (!Character.isLetter(trimmed.charAt(0))) {
            return false;
        }
        
        // Check character composition
        int letters = 0;
        int digits = 0;
        int specialChars = 0;
        
        for (char c : trimmed.toCharArray()) {
            if (Character.isLetter(c)) {
                letters++;
            } else if (Character.isDigit(c)) {
                digits++;
            } else if (c == '_' || c == '-') {
                specialChars++;
            } else {
                return false; // Invalid character
            }
        }
        
        // Username must be at least 50% letters
        return (double) letters / trimmed.length() >= 0.5;
    }

    /**
     * Generates a secure user ID using mathematical operations.
     * Demonstrates mathematical operations and string manipulation.
     */
    public String generateUserId(String username, String email) {
        if (username == null || email == null) {
            throw new IllegalArgumentException("Username and email cannot be null");
        }
        
        // Use hash codes and mathematical operations
        int usernameHash = Math.abs(username.hashCode());
        int emailHash = Math.abs(email.hashCode());
        
        // Combine hashes using mathematical operations
        long combined = (long) usernameHash * 31 + emailHash;
        
        // Apply some mathematical transformations
        combined = combined ^ (combined >>> 16);
        combined = combined * 0x85ebca6b;
        combined = combined ^ (combined >>> 13);
        
        // Convert to string with prefix
        return "USR" + Math.abs(combined % 1000000);
    }

    /**
     * Checks if a user can access premium features.
     * Complex business logic with multiple conditions.
     */
    public boolean canAccessPremiumFeatures(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            return false;
        }
        
        // Must be active and adult
        if (user.getStatus() != User.UserStatus.ACTIVE || !user.isAdult()) {
            return false;
        }
        
        // Score requirements based on age
        int requiredScore = user.getAge() < 25 ? 60 : 70;
        
        if (user.getScore() < requiredScore) {
            return false;
        }
        
        // Additional check using utility method
        return MathUtils.isPrime(user.getScore()) || user.getScore() >= 85;
    }

    // Helper methods

    private void validateUserInput(String username, String email, LocalDate birthDate) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
        
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username format");
        }
    }

    private boolean usernameExists(String username) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    private boolean emailExists(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private User findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    private int calculateQualificationBonus(User user) {
        int bonus = 5; // Base bonus
        
        if (user.isAdult()) {
            bonus += 3;
        }
        
        if (user.hasValidEmailFormat()) {
            bonus += 2;
        }
        
        return bonus;
    }

    // Getter for testing purposes
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public void clearUsers() {
        users.clear();
    }

    // Inner class for statistics
    public static class UserStatistics {
        private final int totalUsers;
        private final double averageScore;
        private final double averageAge;
        private final int activeUsers;
        private final int adultUsers;
        private final int promotableUsers;

        public UserStatistics(int totalUsers, double averageScore, double averageAge,
                            int activeUsers, int adultUsers, int promotableUsers) {
            this.totalUsers = totalUsers;
            this.averageScore = averageScore;
            this.averageAge = averageAge;
            this.activeUsers = activeUsers;
            this.adultUsers = adultUsers;
            this.promotableUsers = promotableUsers;
        }

        // Getters
        public int getTotalUsers() { return totalUsers; }
        public double getAverageScore() { return averageScore; }
        public double getAverageAge() { return averageAge; }
        public int getActiveUsers() { return activeUsers; }
        public int getAdultUsers() { return adultUsers; }
        public int getPromotableUsers() { return promotableUsers; }
    }
}
