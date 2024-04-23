# ArchUnit Findings for Security

1. General, can forbid classes or methods
2. Network, can be forbidden by ArchRules
3. Reflection, can be forbidden by ArchRules
4. Creation of threads can be forbidden in classes, but couldn't find a
   way to make sure, how many threads are created
5. Endless loops not really identifiable by ArchUnit tests, can enforce
   @StrictTimeout in test cases
6. System.in can be restricted to students with ArchUnit tests
7. Course language: Can create a custom ArchRule to check if all error
   messages are using localized error messages to adhere to course language
8. Excessive Output: ArchRule not possible to see how many calls there
   are with loops etc. (not a feasible solution!)
9. Can enforce @HiddenTest to tests with hidden in them? (naming convention important)
10. System.exit can be forbidden via ArchUnit tests in assignment/src of students
11. Tests may leak test values or other internals, implement a filter?
12. Command execution can be forbidden (also can still be problematic if used another package)

13. Take a look at junit 5 extension for better feedback:

* TestInstancePostProcessor: This type of extension is executed after an instance of a test has been created1.
* ExecutionCondition: This type of extension determines whether a container or a test should be executed1.
* TestExecutionExceptionHandler: This type of extension handles exceptions thrown during the execution of a test1.
* ParameterResolver: This type of extension is used to resolve parameters for constructors, methods, and fields1.
  TestInstanceFactory: This type of extension is used to create test instances1.
  *Logging security-related information.
  *Checking for sensitive data leakage.
  *Enforcing security policies (e.g., preventing certain operations).
  *Validating input parameters.
  *Handling security exceptions gracefully.

Possible problems: Cannot detect anything outside the current package:

* Can students create another package and use it
* External frameworks that use the forbidden packages are not detected

## Possible problems with ArchUnit:

* ArchUnit not really helpful when students use other packages that are not declared in assignment/src (Can students
  create another package and call a method that uses a forbidden package?)

* Can enforce tutors/instructors writing tests to enforce secure testing: @StrictTimeout, @Hidden etc
  ** Example:

```java
  ArchRule rule = ArchRuleDefinition.methods()
          .that().areAnnotatedWith(Test.class)
          .should().beAnnotatedWith(MyCustomAnnotation.class)
          .because("all test methods should be annotated with @MyCustomAnnotation");
  ```

* Can still enforce the rules inside the assignment/src but as mentioned security problems when it comes to methods
  defined in other packages
  ** Example:

```java

ArchRule rule = ArchRuleDefinition.noClasses()
        .should().dependOnClassesThat().resideInAnyPackage("java.lang.reflect..")
        .because("we don't want to use reflection");

```

* Also, possible to enforce error message to be localized according to the course (error messages must adhere to a
  certain architecture, haven't tested this yet)

## Proposed solutions:

* Check dependencies recursively just an example might have to create a backtracking algorithm for efficiency (?):
  ```java
  @Test
  public void noReflectionTest() {
  JavaClasses importedClasses = new ClassFileImporter().importPackages("org.sarps");

        ArchRule rule = ArchRuleDefinition.noClasses()
                .should().accessTargetWhere(usesJavaNetPackagePredicate);

        rule.check(importedClasses);
  }

  DescribedPredicate<JavaAccess<?>> usesJavaNetPackagePredicate =
  new DescribedPredicate<>("uses java.net package") {

                @Override
                public boolean test(JavaAccess<?> javaAccess) {
                    JavaClass targetClass = javaAccess.getTarget().getOwner();
                    System.out.println(targetClass.getName());
                    JavaClasses directDependencies = new ClassFileImporter().importPackages(targetClass.getPackageName());
                    ArchRuleDefinition.noClasses().should()
                            .accessTargetWhere(usesJavaNetPackagePredicate).check(directDependencies);
                    return targetClass.getPackageName().startsWith("java.net");
                }
            };
  ```