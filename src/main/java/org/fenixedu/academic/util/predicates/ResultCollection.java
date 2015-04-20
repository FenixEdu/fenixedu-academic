package org.fenixedu.academic.util.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class ResultCollection<T> {

    private Predicate<T> predicate;
    private Collection<T> result;

    public ResultCollection(final Predicate<T> predicate) {
        this.result = new ArrayList<>();
        this.predicate = predicate;
    }

    public Collection<T> getResult() {
        return result;
    }

    private boolean add(T t) {
        return this.result.add(t);
    }

    public void condicionalAdd(T t) {
        if (predicate.test(t)) {
            add(t);
        }
    }

}
