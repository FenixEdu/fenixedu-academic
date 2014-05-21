package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;

public abstract class FenixPredicateGroup extends FenixPredicateGroup_Base {
    public FenixPredicateGroup() {
        super();
        setRootForFenixPredicate(getRoot());
    }

    @Override
    protected void gc() {
        setRootForFenixPredicate(null);
        super.gc();
    }

    protected static <T extends FenixPredicateGroup> Stream<T> filter(Class<T> type) {
        return (Stream<T>) Bennu.getInstance().getFenixPredicateGroupSet().stream().filter(p -> p.getClass() == type);
    }

    protected static <T extends FenixPredicateGroup> Optional<T> find(Class<T> type) {
        return filter(type).findAny();
    }
}
