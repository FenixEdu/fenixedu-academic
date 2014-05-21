package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;

import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(() -> select(degree, function), () -> new PersistentDelegatesGroup(degree, function));
    }

    private static Optional<PersistentDelegatesGroup> select(Degree degree, final FunctionType function) {
        Stream<PersistentDelegatesGroup> options =
                degree != null ? degree.getDelegatesGroupSet().stream() : filter(PersistentDelegatesGroup.class);
        return options.filter(group -> Objects.equals(group.getFunction(), function)).findAny();
    }
}
