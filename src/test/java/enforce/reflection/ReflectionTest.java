package enforce.reflection;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

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
}
