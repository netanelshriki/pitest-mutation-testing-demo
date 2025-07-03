# 🧪 PIT Mutation Testing Demo

A comprehensive Spring Boot project demonstrating **PIT (Pitest) Mutation Testing** with detailed explanations, examples, and best practices.

## 📖 Table of Contents

- [What is Mutation Testing?](#-what-is-mutation-testing)
- [Why Do We Need Mutation Testing?](#-why-do-we-need-mutation-testing)
- [Traditional Coverage vs Mutation Coverage](#-traditional-coverage-vs-mutation-coverage)
- [Project Setup](#-project-setup)
- [Running the Project](#-running-the-project)
- [Running Tests](#-running-tests)
- [Running Mutation Testing](#-running-mutation-testing)
- [Understanding PIT Reports](#-understanding-pit-reports)
- [Comparing Weak vs Strong Tests](#-comparing-weak-vs-strong-tests)
- [How to Improve Mutation Coverage](#-how-to-improve-mutation-coverage)
- [Best Practices](#-best-practices)
- [Common Mutation Operators](#-common-mutation-operators)
- [Troubleshooting](#-troubleshooting)

## 🧬 What is Mutation Testing?

**Mutation Testing** is a fault-based software testing technique that evaluates the quality of your test suite by introducing small, controlled changes (mutations) to your source code and checking whether your tests detect these changes.

### Key Concepts:

- **Mutant**: A copy of your code with a small change (e.g., `>` changed to `>=`)
- **Killed Mutant**: A mutant that causes at least one test to fail (✅ Good!)
- **Survived Mutant**: A mutant that doesn't cause any test to fail (❌ Bad!)
- **Mutation Score**: Percentage of mutants killed by your tests

### Example:

**Original Code:**
```java
public boolean isAdult(int age) {
    return age >= 18;  // Original condition
}
```

**Mutated Code:**
```java
public boolean isAdult(int age) {
    return age > 18;   // Mutated: >= changed to >
}
```

If your tests don't fail with this mutation, it means you're not testing the boundary condition properly!

## 🎯 Why Do We Need Mutation Testing?

Traditional code coverage metrics (line, branch, condition coverage) tell you **what code was executed** but not **whether your tests actually verify the behavior**. 

### Problems with Traditional Coverage:

```java
// This test achieves 100% line coverage but is terrible!
@Test
void testCalculateDiscount() {
    int result = calculateDiscount(100, 10);
    assertTrue(result >= 0); // Very weak assertion!
}
```

### Benefits of Mutation Testing:

1. **Reveals Weak Tests**: Shows tests that don't properly verify behavior
2. **Finds Missing Assertions**: Identifies where you need stronger assertions  
3. **Discovers Edge Cases**: Reveals boundary conditions you haven't tested
4. **Improves Test Quality**: Forces you to write more comprehensive tests
5. **Builds Confidence**: High mutation coverage means robust, trustworthy tests

## 📊 Traditional Coverage vs Mutation Coverage

| Metric | What it Measures | Limitation |
|--------|------------------|------------|
| **Line Coverage** | Lines of code executed | Doesn't verify correctness |
| **Branch Coverage** | Branches taken | Doesn't test branch logic |
| **Condition Coverage** | Boolean conditions evaluated | Doesn't test condition boundaries |
| **Mutation Coverage** | Faults detected by tests | **Actually measures test effectiveness!** |

### Real Example:

```java
public int divide(int a, int b) {
    if (b != 0) {
        return a / b;
    }
    return 0;
}

// BAD TEST: 100% line coverage, poor mutation coverage
@Test
void testDivide() {
    int result = divide(10, 2);
    assertTrue(result > 0); // Doesn't verify exact value!
}

// GOOD TEST: Kills mutants
@Test
void testDivide() {
    assertEquals(5, divide(10, 2));     // Exact assertion
    assertEquals(0, divide(10, 0));     // Edge case
    assertEquals(-5, divide(-10, 2));   // Negative numbers
}
```

## 🛠 Project Setup

### Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **Git**

### Clone the Repository

```bash
git clone https://github.com/netanelshriki/pitest-mutation-testing-demo.git
cd pitest-mutation-testing-demo
```

### Verify Setup

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Compile the project
mvn clean compile
```

## 🚀 Running the Project

### Start the Spring Boot Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Database Console

Visit: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:pitdemo`
- Username: `sa`
- Password: (empty)

## 🧪 Running Tests

### Run All Tests

```bash
mvn test
```

### Run Tests with Coverage Report (JaCoCo)

```bash
mvn clean test jacoco:report
```

Coverage report: `target/site/jacoco/index.html`

### Run Specific Test Class

```bash
# Run weak tests
mvn test -Dtest=UserTest

# Run improved tests  
mvn test -Dtest=UserImprovedTest
```

## 🔬 Running Mutation Testing

### Basic Mutation Testing

```bash
mvn clean test-compile org.pitest:pitest-maven:mutationCoverage
```

### Using Maven Profile

```bash
# Run only mutation testing
mvn clean test -Pmutation-testing

# Run both traditional coverage and mutation testing
mvn clean test -Pfull-testing
```

### Mutation Testing with Custom Configuration

```bash
# Increase verbosity
mvn clean test org.pitest:pitest-maven:mutationCoverage -Dpit.verbose=true

# Target specific classes
mvn clean test org.pitest:pitest-maven:mutationCoverage -DtargetClasses=com.example.pitdemo.model.*
```

### Reports Location

Mutation testing reports are generated in:
```
target/pit-reports/
├── index.html          # Main report
├── com.example.pitdemo.model/
│   └── User.java.html  # Detailed class report
└── css/
    └── style.css
```

## 📋 Understanding PIT Reports

### 1. Main Report (`index.html`)

Open `target/pit-reports/index.html` in your browser to see:

#### Key Metrics:
- **Line Coverage**: Traditional line coverage percentage
- **Mutation Coverage**: Percentage of mutants killed
- **Test Strength**: Mutation coverage of covered lines

#### Understanding the Numbers:
```
Line Coverage: 85% (85/100 lines executed)
Mutation Coverage: 65% (65/100 mutants killed)  
Test Strength: 76% (65/85 mutations in covered code killed)
```

### 2. Package and Class Reports

Click on packages/classes to see detailed breakdowns:

#### Color Coding:
- 🟢 **Light Green**: Line covered, mutant killed (✅ Good!)
- 🔴 **Light Pink**: Line not covered (❌ Missing coverage!)
- 🟢 **Dark Green**: Line covered, all mutants killed (✅ Excellent!)
- 🔴 **Dark Pink**: Line covered, mutant survived (❌ Weak test!)

### 3. Detailed Mutation Information

Click on line numbers to see:
- **Mutation Description**: What was changed
- **Mutator**: Type of mutation applied  
- **Status**: KILLED, SURVIVED, NO_COVERAGE

#### Example Mutation Details:
```
Line 42: return age >= 18;

Mutation: Substituted <= for >=
Mutator: CONDITIONALS_BOUNDARY
Status: SURVIVED
Description: This mutant changed >= to <= but no test failed
```

## ⚖️ Comparing Weak vs Strong Tests

This project includes both weak and strong test examples:

### Weak Tests (Poor Mutation Coverage)
- `UserTest.java` - Demonstrates poor testing practices
- `MathUtilsTest.java` - Shows weak assertions and missing edge cases
- `UserServiceTest.java` - Examples of insufficient boundary testing

### Strong Tests (Good Mutation Coverage)  
- `UserImprovedTest.java` - Comprehensive testing with boundaries
- (Additional improved tests to be added)

### Running Comparison

```bash
# Test with weak tests only
mvn test -Dtest="*Test" -Dmutation.coverage

# Test with improved tests only  
mvn test -Dtest="*ImprovedTest" -Dmutation.coverage
```

Compare the mutation coverage scores to see the dramatic difference!

## 📈 How to Improve Mutation Coverage

### 1. Analyze Surviving Mutants

Look at the PIT report and identify survived mutants:

```java
// Survived mutant: >= changed to >
return age >= 18;
```

**Fix**: Add boundary test:
```java
@Test
void shouldTestAdultBoundary() {
    assertFalse(isAdult(17)); // Just below boundary
    assertTrue(isAdult(18));  // Exact boundary  
    assertTrue(isAdult(19));  // Just above boundary
}
```

### 2. Common Weak Test Patterns to Fix

#### ❌ Weak: Vague Assertions
```java
assertTrue(result > 0); // Too vague!
```

#### ✅ Strong: Exact Assertions  
```java
assertEquals(42, result); // Exact value
```

#### ❌ Weak: Single Test Case
```java
assertEquals(5, factorial(5)); // Only one case
```

#### ✅ Strong: Boundary and Edge Cases
```java
assertEquals(1, factorial(0));    // Edge case
assertEquals(1, factorial(1));    // Boundary  
assertEquals(2, factorial(2));    // Small value
assertEquals(120, factorial(5));  // Normal case
```

#### ❌ Weak: Missing Exception Tests
```java
// Only tests valid input
assertEquals(10, divide(20, 2));
```

#### ✅ Strong: Exception Testing
```java
assertEquals(10, divide(20, 2));           // Valid case
assertEquals(0, divide(20, 0));            // Edge case
assertThrows(Exception.class, () -> {      // Exception case
    divide(20, 0);
});
```

### 3. Systematic Approach to Improvement

1. **Run mutation testing**: `mvn test org.pitest:pitest-maven:mutationCoverage`
2. **Open the report**: `target/pit-reports/index.html`
3. **Find survived mutants**: Look for pink lines
4. **Analyze the mutation**: Understand what changed
5. **Add targeted tests**: Write tests to kill specific mutants
6. **Re-run and verify**: Confirm mutants are killed
7. **Repeat**: Continue until satisfied with coverage

## 🎯 Best Practices

### 1. Start with Traditional Coverage
- Achieve good line/branch coverage first
- Then use mutation testing to improve test quality

### 2. Focus on Critical Code
- Use `targetClasses` to focus on important business logic
- Don't waste time on getters/setters and simple methods

### 3. Test Boundaries and Edge Cases
```java
// Test boundary conditions thoroughly
@ParameterizedTest
@ValueSource(ints = {17, 18, 19}) // Around boundary
void testAdultBoundary(int age) {
    // Test implementation
}
```

### 4. Use Exact Assertions
```java
// Bad
assertTrue(result > 0);

// Good  
assertEquals(42, result);
```

### 5. Test All Code Paths
```java
public String getLevel(int score) {
    if (score >= 90) return "Expert";
    else if (score >= 70) return "Advanced";
    else if (score >= 50) return "Intermediate";
    else return "Beginner";
}

// Test ALL boundaries: 49,50,69,70,89,90
```

### 6. Performance Considerations
- Use `threads` parameter for faster execution
- Use `targetClasses` to limit scope
- Consider `timeoutConstant` for slow tests

## 🔧 Common Mutation Operators

| Operator | Description | Example |
|----------|-------------|---------|
| **CONDITIONALS_BOUNDARY** | Changes boundary conditions | `>=` → `>`, `<` → `<=` |
| **NEGATE_CONDITIONALS** | Negates conditions | `==` → `!=`, `<` → `>=` |
| **MATH** | Changes math operators | `+` → `-`, `*` → `/` |
| **INCREMENTS** | Changes increment/decrement | `++` → `--`, `+1` → `-1` |
| **RETURN_VALS** | Changes return values | `return true` → `return false` |
| **VOID_METHOD_CALLS** | Removes method calls | `method()` → `// method()` |
| **INVERT_NEGS** | Inverts negations | `-x` → `x` |

### Configuring Mutators

```xml
<configuration>
    <mutators>
        <mutator>CONDITIONALS_BOUNDARY</mutator>
        <mutator>INCREMENTS</mutator>
        <mutator>MATH</mutator>
        <!-- Add/remove mutators as needed -->
    </mutators>
</configuration>
```

## 🚨 Troubleshooting

### Common Issues and Solutions

#### 1. "No mutations found"
```
Caused by: No mutations found. This probably means there is an issue with either the supplied
```

**Solutions:**
- Check `targetClasses` configuration
- Ensure classes are compiled: `mvn test-compile`
- Verify package names match

#### 2. Tests timeout
```
[ERROR] Tests run: X, Skipped: Y due to timeout
```

**Solutions:**
- Increase `timeoutConstant`: `<timeoutConstant>8000</timeoutConstant>`
- Increase `timeoutFactor`: `<timeoutFactor>2.0</timeoutFactor>`
- Optimize slow tests

#### 3. OutOfMemoryError
```
[ERROR] java.lang.OutOfMemoryError: Java heap space
```

**Solutions:**
- Increase heap size: `export MAVEN_OPTS="-Xmx2g"`
- Reduce target classes scope
- Increase `threads` to parallelize

#### 4. Plugin version conflicts
**Solutions:**
- Use latest plugin versions (see `pom.xml`)
- Check for dependency conflicts: `mvn dependency:tree`

#### 5. JUnit 5 not detected
```
WARNING: JUnit 5 is on the classpath but the pitest junit 5 plugin is not installed
```

**Solution:**
Ensure JUnit 5 plugin is configured:
```xml
<dependencies>
    <dependency>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-junit5-plugin</artifactId>
        <version>1.2.3</version>
    </dependency>
</dependencies>
```

## 📚 Learning Path

### Beginner
1. ✅ Clone this project
2. ✅ Run regular tests: `mvn test`
3. ✅ Run mutation testing: `mvn test org.pitest:pitest-maven:mutationCoverage`
4. ✅ Open and explore the HTML report
5. ✅ Compare weak vs improved tests

### Intermediate  
1. ✅ Analyze survived mutants in the report
2. ✅ Write additional tests to kill specific mutants
3. ✅ Experiment with different mutation operators
4. ✅ Configure PIT for your own projects

### Advanced
1. ✅ Integrate PIT into CI/CD pipelines
2. ✅ Set mutation coverage thresholds
3. ✅ Optimize PIT performance for large codebases
4. ✅ Create custom mutation operators

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Add new examples or improvements
4. Ensure all tests pass
5. Submit a pull request

## 📖 Additional Resources

### Official Documentation
- [PIT Official Website](https://pitest.org/)
- [PIT Maven Plugin](https://pitest.org/quickstart/maven/)
- [PIT Mutation Operators](https://pitest.org/quickstart/mutators/)

### Articles and Tutorials
- [Mutation Testing: Better Than Code Coverage](https://www.baeldung.com/java-mutation-testing-with-pitest)
- [Why Mutation Testing is Important](https://blog.frankel.ch/mutation-testing-pitest/)

### Academic Papers
- [Mutation Testing: A Comprehensive Survey](https://ieeexplore.ieee.org/document/8371445)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [PIT Testing Tool](https://github.com/hcoles/pitest) by Henry Coles
- [Spring Boot](https://spring.io/projects/spring-boot) team
- [JUnit 5](https://junit.org/junit5/) team

---

**Happy Mutation Testing! 🧬🔬**

*Remember: High mutation coverage doesn't guarantee bug-free code, but it does guarantee that your tests are doing their job properly!*
