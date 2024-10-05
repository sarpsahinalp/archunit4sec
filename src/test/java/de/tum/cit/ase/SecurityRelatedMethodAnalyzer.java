package de.tum.cit.ase;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FilePermission;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SecurityRelatedMethodAnalyzer {

    // FilePermission
    public static void analyzeFileSystemAccess(JavaClasses classes) {

    }

    // NetPermission, SocketPermission
    public static void analyzeNetworkAccess(JavaClasses classes) {

    }

    // ProcessBuilder and Runtime.exec
    public static void analyzeExecTerminal(JavaClasses classes) {

    }

    // checkExit
    public static void analyzeJVMTermination(JavaClasses classes) {

    }

    // ReflectionPermission
    public static void analyzeReflection(JavaClasses classes) {

    }

    // SecurityManager
    @Test
    public void analyzeSecurityManager() {
        JavaClasses classes = new ClassFileImporter().withImportOption(location -> location.contains("jrt")).importClasspath();
        ArchRuleDefinition.noClasses()
                .should(new ArchCondition<JavaClass>("calls SecurityManager") {
                    @Override
                    public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
                        javaClass.getAccessesFromSelf().forEach(javaAccess -> {
                            if (javaAccess.getTarget().getFullName().startsWith("java.lang.SecurityManager.check")) {
                                conditionEvents.add(new SimpleConditionEvent(javaClass, true, javaAccess.getOrigin().getFullName() + " needs the following call " + javaAccess.getTarget().getFullName()));
                            }
                        });
                    }
                })
                .check(classes);
    }

    @Test
    public void analyzeNativeMethods() {
        JavaClasses classes = new ClassFileImporter().withImportOption(location -> location.contains("jrt")).importClasspath();

        ArchRuleDefinition.noMethods()
                .that(new DescribedPredicate<>("isNative") {
                    @Override
                    public boolean test(JavaMethod javaMethod) {
                        return javaMethod.getModifiers().contains(JavaModifier.NATIVE);
                    }
                })
                .should(new ArchCondition<>("has the following security manager code") {
                    @Override
                    public void check(JavaMethod javaMethod, ConditionEvents conditionEvents) {
                        javaMethod.getAccessesFromSelf().forEach(javaAccess -> {
                            conditionEvents.add(new SimpleConditionEvent(javaMethod, javaAccess.getTarget().getFullName().startsWith("java.lang.SecurityManager.check"), javaMethod.getFullName() + " --> " + javaAccess.getTarget().getFullName()));
                        });
                        // Iteratively check the methods that are calling the java method,
                        // and if they are making a call to the security manager print out the security manager code in an evernt
                        Queue<JavaCodeUnitAccess<?>> accessSet = new LinkedList<>(javaMethod.getAccessesToSelf());
                        Set<String> visited = new HashSet<>();

                        while (!accessSet.isEmpty()) {
                            JavaCodeUnitAccess<?> access = accessSet.poll();
                            if (visited.contains(access.getOrigin().getFullName())) {
                                continue;
                            }

                            visited.add(access.getOrigin().getFullName());
                            AtomicBoolean isSecurityManager = new AtomicBoolean(false);
                            access.getOrigin().getAccessesFromSelf().forEach(javaAccess -> {
                                if (!isSecurityManager.get() && javaAccess.getTarget().getFullName().startsWith("java.lang.SecurityManager.check")) {
                                    isSecurityManager.set(true);
                                    conditionEvents.add(new SimpleConditionEvent(access.getOrigin(), isSecurityManager.get(), javaMethod.getDescription() + " --------> " + javaAccess.getOrigin().getDescription() + " -------> "  + javaAccess.getTarget().getDescription()));
                                }
                            });

                            if (isSecurityManager.get()) {
                                break;
                            }

                            accessSet.addAll((Collection<? extends JavaCodeUnitAccess<?>>) access.getOrigin().getAccessesToSelf());
                        }
                    }
                })
                .check(classes);
    }

    @Test
    void analyzeFilePermission() {
        JavaClasses classes = new ClassFileImporter().withImportOption(location -> location.contains("jrt")).importClasspath();

        // Identify classes that transitively access FilePermission
        ArchRuleDefinition.noClasses()
                .should(new TransitivelyAccessesMethodsCondition(new DescribedPredicate<>("accesses FilePermission") {
                    @Override
                    public boolean test(JavaAccess<?> javaAccess) {
                        return javaAccess.getTargetOwner().isAssignableTo(FilePermission.class.getName());
                    }
                }))
                .check(classes);
    }
}
