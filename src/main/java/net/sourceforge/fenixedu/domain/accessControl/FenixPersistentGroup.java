package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;
import java.util.function.Supplier;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public abstract class FenixPersistentGroup extends FenixPersistentGroup_Base {
    protected FenixPersistentGroup() {
        super();
    }

    protected static <T extends FenixPersistentGroup> T singleton(Supplier<Optional<T>> selector, Supplier<T> creator) {
        if (FenixFramework.getTransaction().getTxIntrospector().isWriteTransaction()) {
            return selector.get().orElseGet(creator);
        }
        return selector.get().orElseGet(() -> create(selector, creator));
    }

    @Atomic(mode = TxMode.WRITE)
    private static <T extends FenixPersistentGroup> T create(Supplier<Optional<T>> selector, Supplier<T> creator) {
        return selector.get().orElseGet(creator);
    }
}
