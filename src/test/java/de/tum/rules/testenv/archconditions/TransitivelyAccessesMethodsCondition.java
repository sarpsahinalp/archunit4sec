package de.tum.rules.testenv.archconditions;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnitAccess;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.thirdparty.com.google.common.collect.ImmutableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tngtech.archunit.lang.ConditionEvent.createMessage;
import static com.tngtech.archunit.thirdparty.com.google.common.base.Preconditions.checkNotNull;
import static com.tngtech.archunit.thirdparty.com.google.common.collect.Iterables.getLast;
import static java.util.stream.Collectors.joining;

/**
 * Checks that a class transitively accesses methods that match a given predicate.
 */
public class TransitivelyAccessesMethodsCondition extends ArchCondition<JavaClass> {

    private final DescribedPredicate<? super JavaCodeUnitAccess<?>> conditionPredicate;
    private final TransitiveAccessPath transitiveAccessPath = new TransitiveAccessPath();

    public TransitivelyAccessesMethodsCondition(DescribedPredicate<? super JavaCodeUnitAccess<?>> conditionPredicate) {
        super("transitively depend on classes that " + conditionPredicate.getDescription());

        this.conditionPredicate = checkNotNull(conditionPredicate);
    }

    @Override
    public void check(JavaClass item, ConditionEvents events) {
        boolean hastTransitiveAccess = false;
        for (JavaCodeUnitAccess<?> target : item.getCodeUnitAccessesFromSelf()) {
            List<JavaCodeUnitAccess<?>> dependencyPath = transitiveAccessPath.findPathTo(target);
            if (!dependencyPath.isEmpty()) {
                events.add(newTransitiveAccessPathFoundEvent(target, dependencyPath));
                hastTransitiveAccess = true;
            }
        }
        if (!hastTransitiveAccess) {
            events.add(newNoTransitiveDependencyPathFoundEvent(item));
        }
    }

    private static ConditionEvent newTransitiveAccessPathFoundEvent(JavaCodeUnitAccess<?> javaClass, List<JavaCodeUnitAccess<?>> transitiveDependencyPath) {
        String message = String.format("%saccesses <%s>",
                transitiveDependencyPath.size() > 1 ? "transitively " : "",
                getLast(transitiveDependencyPath).getTarget().getFullName());

        if (transitiveDependencyPath.size() > 1) {
            message += " by [" + transitiveDependencyPath.stream().map(access -> access.getOrigin().getFullName()).collect(joining("->")) + "]";
        }

        return SimpleConditionEvent.satisfied(javaClass, createMessage(javaClass, message));
    }

    private static ConditionEvent newNoTransitiveDependencyPathFoundEvent(JavaClass javaClass) {
        return SimpleConditionEvent.violated(javaClass, createMessage(javaClass, "does not transitively depend on any matching class"));
    }

    private Set<JavaCodeUnitAccess<?>> getDirectAccessTargetsOutsideOfAnalyzedClasses(JavaCodeUnitAccess<?> item) {
        return item.getTargetOwner().getCodeUnitAccessesFromSelf()
                .stream().filter(a -> a.getOrigin().getFullName().equals(item.getTarget().getFullName()))
                .collect(Collectors.toSet());
    }

    private class TransitiveAccessPath {
        /**
         * @return some outgoing transitive dependency path to the supplied class or empty if there is none
         */
        List<JavaCodeUnitAccess<?>> findPathTo(JavaCodeUnitAccess<?> method) {
            ImmutableList.Builder<JavaCodeUnitAccess<?>> transitivePath = ImmutableList.builder();
            addAccessesToPathFrom(method, transitivePath, new HashSet<>());
            return transitivePath.build().reverse();
        }

        private boolean addAccessesToPathFrom(
                JavaCodeUnitAccess<?> method,
                ImmutableList.Builder<JavaCodeUnitAccess<?>> transitivePath,
                Set<String> analyzedMethods
        ) {
            if (conditionPredicate.test(method)) {
                transitivePath.add(method);
                return true;
            }

            analyzedMethods.add(method.getTarget().getFullName());

            for (JavaCodeUnitAccess<?> access : getDirectAccessTargetsOutsideOfAnalyzedClasses(method)) {
                if (!analyzedMethods.contains(access.getTarget().getFullName()) && addAccessesToPathFrom(access, transitivePath, analyzedMethods)) {
                    transitivePath.add(method);
                    return true;
                }
            }

            return false;
        }
    }
}
