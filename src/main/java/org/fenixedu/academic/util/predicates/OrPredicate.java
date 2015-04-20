package org.fenixedu.academic.util.predicates;

import java.util.function.Predicate;

public class OrPredicate<T> extends ChainPredicate<T> {

    public OrPredicate() {
        super();
    }

    @Override
    public boolean test(T t) {
        for (Predicate<T> predicate : predicates) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

}
