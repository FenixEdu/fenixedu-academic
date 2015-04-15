package org.fenixedu.academic.util.predicates;

import java.util.Collection;
import java.util.function.Predicate;

public class AndPredicate<T> extends ChainPredicate<T> {

    public AndPredicate() {
        super();
    }

    public AndPredicate(Collection<Predicate<T>> predicates) {
        super(predicates);
    }

    public AndPredicate(Predicate<T> pred1, Predicate<T> pred2) {
        super();
        add(pred1);
        add(pred2);
    }

    @Override
    public boolean test(T t) {
        for (Predicate<T> predicate : predicates) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

}
