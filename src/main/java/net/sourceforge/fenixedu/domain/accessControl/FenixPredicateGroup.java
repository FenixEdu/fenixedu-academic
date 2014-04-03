package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;

public abstract class FenixPredicateGroup extends FenixPredicateGroup_Base {
    public FenixPredicateGroup() {
        super();
        setRootForFenixPredicate(getRoot());
    }

    protected static <T extends FenixPredicateGroup> FluentIterable<T> filter(Class<T> type) {
        return FluentIterable.from(Bennu.getInstance().getFenixPredicateGroupSet()).filter(type);
    }

    protected static <T extends FenixPredicateGroup> Optional<T> find(Class<T> type) {
        return FluentIterable.from(Bennu.getInstance().getFenixPredicateGroupSet()).filter(type).first();
    }

}
