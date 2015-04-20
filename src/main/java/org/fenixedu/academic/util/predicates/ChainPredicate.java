package org.fenixedu.academic.util.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

abstract class ChainPredicate<T> implements Predicate<T> {

    protected Collection<Predicate<T>> predicates;

    public ChainPredicate(Collection<Predicate<T>> predicates) {
        this.predicates = predicates;
    }

    public ChainPredicate() {
        this(new ArrayList<Predicate<T>>());
    }

    public void add(Predicate<T> predicate) {
        this.predicates.add(predicate);
    }

}
