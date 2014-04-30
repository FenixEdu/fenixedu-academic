package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

public class PersistentDelegatesGroup extends PersistentDelegatesGroup_Base {
    public PersistentDelegatesGroup(Degree degree, FunctionType function) {
        super();
        setDegree(degree);
        setFunction(function);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public Group toGroup() {
        return DelegatesGroup.get(getDegree(), getFunction());
    }

    @Override
    protected void gc() {
        setDegree(null);
        super.gc();
    }

    public static PersistentDelegatesGroup getInstance(Degree degree, FunctionType function) {
        PersistentDelegatesGroup instance = select(degree, function);
        return instance != null ? instance : create(degree, function);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentDelegatesGroup create(Degree degree, FunctionType function) {
        PersistentDelegatesGroup instance = select(degree, function);
        return instance != null ? instance : new PersistentDelegatesGroup(degree, function);
    }

    private static PersistentDelegatesGroup select(Degree degree, final FunctionType function) {
        if (degree != null) {
            for (PersistentDelegatesGroup candidate : degree.getDelegatesGroupSet()) {
                if (Objects.equal(candidate.getFunction(), function)) {
                    return candidate;
                }
            }
        } else {
            return filter(PersistentDelegatesGroup.class).firstMatch(new Predicate<PersistentDelegatesGroup>() {
                @Override
                public boolean apply(PersistentDelegatesGroup group) {
                    return Objects.equal(group.getFunction(), function);
                }
            }).orNull();
        }
        return null;
    }
}
