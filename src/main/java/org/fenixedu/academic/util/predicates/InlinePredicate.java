package org.fenixedu.academic.util.predicates;

import java.util.function.Predicate;

public abstract class InlinePredicate<T, K> implements Predicate<T> {

    private K value;

    public InlinePredicate(K k) {
        this.value = k;
    }

    protected K getValue() {
        return value;
    }

}
