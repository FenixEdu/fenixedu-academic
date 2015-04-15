package org.fenixedu.academic.util.predicates;

import java.util.function.Predicate;

public interface PredicateContainer<Type> {

    public Predicate<Type> getPredicate();

}
