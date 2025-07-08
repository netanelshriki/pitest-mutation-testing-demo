package com.example.pitdemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * User entity that demonstrates various mutation testing scenarios.
 * 
 * This class contains business logic that can be effectively tested
 * with mutation testing to reveal weak spots in our test suite.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Min(value = 0, message = "Score cannot be negative")
    @Max(value = 100, message = "Score cannot exceed 100")
    private int score;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    // Constructors
    public User() {
        this.status = UserStatus.INACTIVE;
        this.score = 0;
    }

    public User(String username, String email, LocalDate birthDate) {
        this();
        this.username = username;
        this.email = email;
        this.birthDate = birthDate;
    }

    // Business logic methods - these are perfect for mutation testing

    /**
     * Calculates the user's age based on birth date.
     * This method demonstrates boundary conditions that mutation testing can reveal.
     */
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Determines if the user is an adult (18 or older).
     * This method has a clear boundary condition that should be thoroughly tested.
     */
    public boolean isAdult() {
        return getAge() >= 18;
    }

    /**
     * Calculates the user's experience level based on their score.
     * This method demonstrates multiple conditional branches.
     */
    public String getExperienceLevel() {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        
        if (score >= 90) {
            return "Expert";
        } else if (score >= 70) {
            return "Advanced";
        } else if (score >= 50) {
            return "Intermediate";
        } else if (score >= 30) {
            return "Beginner";
        } else {
            return "Novice";
        }
    }

    /**
     * Updates the user's score with validation.
     * This method demonstrates parameter validation and state changes.
     */
    public void updateScore(int newScore) {
        if (newScore < 0 || newScore > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        
        this.score = newScore;
        
        // Automatically activate user if they achieve a good score
        if (newScore >= 50 && this.status == UserStatus.INACTIVE) {
            this.status = UserStatus.ACTIVE;
        }
    }

    /**
     * Checks if the user can be promoted based on score and status.
     * This method combines multiple conditions.
     */
    public boolean canBePromoted() {
        return this.status == UserStatus.ACTIVE && 
               this.score >= 75 && 
               isAdult();
    }

    /**
     * Calculates a bonus score based on various factors.
     * This method demonstrates mathematical operations that can be mutated.
     */
    public int calculateBonus() {
        int bonus = 0;
        
        if (isAdult()) {
            bonus += 10;
        }
        
        if (score > 80) {
            bonus += score * 2;
        } else if (score > 50) {
            bonus += score;
        }
        
        if (status == UserStatus.ACTIVE) {
            bonus *= 2;
        }
        
        return bonus;
    }

    /**
     * Validates the email format (simplified version).
     * This method demonstrates string operations and boolean logic.
     */
    public boolean hasValidEmailFormat() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Basic structure checks
        if (!email.contains("@") || !email.contains(".")) {
            return false;
        }

        // Check for multiple @ symbols
        if (email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }

        int atIndex = email.indexOf("@");
        int lastDotIndex = email.lastIndexOf(".");

        // @ must come before the last .
        if (atIndex >= lastDotIndex) {
            return false;
        }

        // There must be at least one character before @
        if (atIndex == 0) {
            return false;
        }

        // There must be at least one character after the last .
        if (lastDotIndex == email.length() - 1) {
            return false;
        }

        // There must be at least one character between @ and .
        if (lastDotIndex - atIndex <= 1) {
            return false;
        }

        // Check for consecutive dots or @ followed immediately by .
        if (email.contains("..") || email.contains("@.")) {
            return false;
        }

        return true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", score=" + score +
                ", status=" + status +
                '}';
    }

    // Enum for user status
    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        DELETED
    }
}
