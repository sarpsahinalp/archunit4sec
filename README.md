# ArchUnit Findings for Security

1. General, can forbid classes or methods - [x]
2. Network, can be forbidden by ArchRules - [x]
3. Reflection, can be forbidden by ArchRules - [x]
4. Creation of threads can be forbidden in classes, but couldn't find a
   way to make sure, how many threads are created? - []
5. Endless loops not really identifiable by ArchUnit tests, can enforce
   @StrictTimeout in test cases - [x]
6. System.in can be restricted to students with ArchUnit tests - [x]
7. Course language: Can create a custom ArchRule to check if all error
   messages are using localized error messages to adhere to course language - []
8. Excessive Output: ArchRule not possible to see how many calls there
   are with loops etc. (not a feasible solution! have to take a look at ast we might recognize a pattern) - []
9. Can enforce @HiddenTest to tests with hidden in them (naming convention important) - [x]
10. System.exit can be forbidden via ArchUnit tests in assignment/src of students - [x]
11. Tests may leak test values or other internals, implement good feedback - [x]
12. Command execution can be forbidden (also can still be problematic if used another package) - [x]

13. Take a look at junit 5 extension for better feedback: - [x]

* TestInstancePostProcessor: This type of extension is executed after an instance of a test has been created.
* ExecutionCondition: This type of extension determines whether a container or a test should be executed.
* TestExecutionExceptionHandler: This type of extension handles exceptions thrown during the execution of a test.
* ParameterResolver: This type of extension is used to resolve parameters for constructors, methods, and fields.
  TestInstanceFactory: This type of extension is used to create test instances.
  *Logging security-related information.
  *Checking for sensitive data leakage.
  *Enforcing security policies (e.g., preventing certain operations).
  *Validating input parameters.
  *Handling security exceptions gracefully.
