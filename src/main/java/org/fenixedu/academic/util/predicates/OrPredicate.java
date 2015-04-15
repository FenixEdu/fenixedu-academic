package org.fenixedu.academic.util.predicates;

import java.util.Collection;
import java.util.function.Predicate;

public class OrPredicate<T> extends ChainPredicate<T> {

    public OrPredicate(Collection<Predicate<T>> predicates) {
        super(predicates);
    }

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
