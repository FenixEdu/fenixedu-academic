package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class PersistentDelegateStudentsGroup extends PersistentDelegateStudentsGroup_Base {
    private PersistentDelegateStudentsGroup(PersonFunction delegateFunction, FunctionType type) {
        super();
        setDelegateFunction(delegateFunction);
        setType(type);
    }

    @Override
    public Group toGroup() {
        return DelegateStudentsGroup.get(getDelegateFunction(), getType());
    }

    @Override
    protected void gc() {
        setDelegateFunction(null);
        super.gc();
    }

    public static PersistentDelegateStudentsGroup getInstance(PersonFunction delegateFunction, FunctionType type) {
        PersistentDelegateStudentsGroup instance = select(delegateFunction, type);
        return instance != null ? instance : create(delegateFunction, type);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentDelegateStudentsGroup create(PersonFunction delegateFunction, FunctionType type) {
        PersistentDelegateStudentsGroup instance = select(delegateFunction, type);
        return instance != null ? instance : new PersistentDelegateStudentsGroup(delegateFunction, type);
    }

    private static PersistentDelegateStudentsGroup select(PersonFunction delegateFunction, final FunctionType type) {
        return FluentIterable.from(delegateFunction.getDelegateStudentsGroupSet())
                .firstMatch(new Predicate<PersistentDelegateStudentsGroup>() {
                    @Override
                    public boolean apply(PersistentDelegateStudentsGroup group) {
                        return Objects.equal(group.getType(), type);
                    }
                }).orNull();
    }
}
