package testenv.archconditions;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.thirdparty.com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tngtech.archunit.lang.ConditionEvent.createMessage;
import static com.tngtech.archunit.thirdparty.com.google.common.base.Preconditions.checkNotNull;
import static com.tngtech.archunit.thirdparty.com.google.common.collect.Iterables.getLast;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

public final class TransitiveDependencyConditionExcluding extends ArchCondition<JavaClass> {

    private final DescribedPredicate<? super JavaClass> conditionPredicate;
    private final TransitiveDependencyConditionExcluding.TransitiveDependencyPath transitiveDependencyPath = new TransitiveDependencyConditionExcluding.TransitiveDependencyPath();
    private Collection<JavaClass> allClasses;
    private final Set<String> excludedClasses;

    public TransitiveDependencyConditionExcluding(DescribedPredicate<? super JavaClass> conditionPredicate, Set<String> excludedClasses) {
        super("transitively depend on classes that " + conditionPredicate.getDescription());

        this.conditionPredicate = checkNotNull(conditionPredicate);
        this.excludedClasses = excludedClasses;
    }

    @Override
    public void init(Collection<JavaClass> allObjectsToTest) {
        this.allClasses = allObjectsToTest;
    }

    @Override
    public void check(JavaClass javaClass, ConditionEvents events) {
        boolean hasTransitiveDependency = false;
        for (JavaClass target : getDirectDependencyTargetsOutsideOfAnalyzedClasses(javaClass)) {
            List<JavaClass> dependencyPath = transitiveDependencyPath.findPathTo(target).stream()
                    .filter(j -> !excludedClasses.contains(j.getName()))
                    .toList();
            if (!dependencyPath.isEmpty()) {
                events.add(newTransitiveDependencyPathFoundEvent(javaClass, dependencyPath));
                hasTransitiveDependency = true;
            }
        }
        if (!hasTransitiveDependency) {
            events.add(newNoTransitiveDependencyPathFoundEvent(javaClass));
        }
    }

    private static ConditionEvent newTransitiveDependencyPathFoundEvent(JavaClass javaClass, List<JavaClass> transitiveDependencyPath) {
        String message = String.format("%sdepends on <%s>",
                transitiveDependencyPath.size() > 1 ? "transitively " : "",
                getLast(transitiveDependencyPath).getFullName());

        if (transitiveDependencyPath.size() > 1) {
            message += " by [" + transitiveDependencyPath.stream().map(JavaClass::getName).collect(joining("->")) + "]";
        }

        return SimpleConditionEvent.satisfied(javaClass, createMessage(javaClass, message));
    }

    private static ConditionEvent newNoTransitiveDependencyPathFoundEvent(JavaClass javaClass) {
        return SimpleConditionEvent.violated(javaClass, createMessage(javaClass, "does not transitively depend on any matching class"));
    }

    private Set<JavaClass> getDirectDependencyTargetsOutsideOfAnalyzedClasses(JavaClass item) {
        return item.getDirectDependenciesFromSelf().stream()
                .map(dependency -> dependency.getTargetClass().getBaseComponentType())
                .filter(targetClass -> !allClasses.contains(targetClass))
                .collect(toSet());
    }

    private class TransitiveDependencyPath {
        /**
         * @return some outgoing transitive dependency path to the supplied class or empty if there is none
         */
        List<JavaClass> findPathTo(JavaClass clazz) {
            ImmutableList.Builder<JavaClass> transitivePath = ImmutableList.builder();
            addDependenciesToPathFrom(clazz, transitivePath, new HashSet<>());
            return transitivePath.build().reverse();
        }

        private boolean addDependenciesToPathFrom(
                JavaClass clazz,
                ImmutableList.Builder<JavaClass> dependencyPath,
                Set<JavaClass> analyzedClasses
        ) {
            if (conditionPredicate.test(clazz)) {
                dependencyPath.add(clazz);
                return true;
            }

            analyzedClasses.add(clazz);

            for (JavaClass directDependency : getDirectDependencyTargetsOutsideOfAnalyzedClasses(clazz)) {
                if (!analyzedClasses.contains(directDependency)
                        && addDependenciesToPathFrom(directDependency, dependencyPath, analyzedClasses)) {
                    dependencyPath.add(clazz);
                    return true;
                }
            }

            return false;
        }
    }
}
