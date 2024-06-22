## ArchUnit Tests for Security

### Introduction
This document describes the ArchUnit tests that are used to verify the security of the student submission. The tests are written in Java and are executed during the build process. The tests are located in the `src/test/java` directory.

### ArchUnit
ArchUnit is a free, simple and extensible library for checking the architecture of your Java code. That is, ArchUnit can check dependencies between packages and classes, layers and slices, check for cyclic dependencies, or check for layer violations. ArchUnit is available at [https://www.archunit.org/](https://www.archunit.org/).

### Security Tests
ArchManagerTest is the main test class that contains all the security tests. The tests are written in Java and are executed during the build process. Tests cover possible security issiues such as:
- Prevents unrestricted access to the file system
- Prevents command execution
- Prevents usage of reflective programming
- Prevents network connections
- Prevents creating threads
- Prevents modifying the Security Manager
- Prevents modifying SSL
- Prevents modifying authentication
- Prevents modifying serialisation
- Prevents modifying management
- Prevents utilising AWT

Example of a test:
* ```java
	@ArchTest
    void checkGWTAccess(JavaClasses classes) {
        ArchRuleDefinition.noClasses()
                .should()
                .onlyAccessClassesThat()
                .containAnyCodeUnitsThat(new DescribedPredicate<>("GWT access") {
                    @Override
                    public boolean test(JavaCodeUnit javaCodeUnit) {
                        System.out.println(javaCodeUnit.getFullName());
                        return false;
                    }
                })
                .orShould(new ArchCondition<>("GWT access") {
                    @Override
                    public void check(JavaClass item, ConditionEvents events) {
                        System.out.println(events);
                        System.out.println(item);
                        events.add(SimpleConditionEvent.violated(item, "GWT access"));
                    }
                })
                .check(classes);

        Architectures.LayeredArchitecture layers = Architectures
                .layeredArchitecture()
                .consideringAllDependencies()
                .layer("Client")
                .definedBy("..client..")
                .layer("Shared")
                .definedBy("..shared..");

        ArchRule rule = layers.whereLayer("Client")
                .mayOnlyAccessLayers("Shared", "DomDtos", "DomBase", "DomConfig")
                .ignoreDependency(
                        DescribedPredicate.alwaysTrue(),
                        JavaClass.Predicates.resideOutsideOfPackages("com.myapp.svc..")
                );

        rule.check(classes);
    }
```

