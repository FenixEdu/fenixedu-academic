package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class PersistentCoordinatorGroup extends PersistentCoordinatorGroup_Base {
    protected PersistentCoordinatorGroup(DegreeType degreeType, Degree degree) {
        super();
        setDegreeType(degreeType);
        setDegree(degree);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public Group toGroup() {
        return CoordinatorGroup.get(getDegreeType(), getDegree());
    }

    @Override
    protected void gc() {
        setDegree(null);
        super.gc();
    }

    public static PersistentCoordinatorGroup getInstance() {
        return getInstance(filter(PersistentCoordinatorGroup.class), null, null);
    }

    public static PersistentCoordinatorGroup getInstance(DegreeType degreeType) {
        return getInstance(filter(PersistentCoordinatorGroup.class), degreeType, null);
    }

    public static PersistentCoordinatorGroup getInstance(Degree degree) {
        return getInstance(FluentIterable.from(degree.getCoordinatorGroupSet()), null, degree);
    }

    public static PersistentCoordinatorGroup getInstance(DegreeType degreeType, Degree degree) {
        if (degreeType != null) {
            return getInstance(degreeType);
        }
        if (degree != null) {
            return getInstance(degree);
        }
        return getInstance();
    }

    private static PersistentCoordinatorGroup getInstance(FluentIterable<PersistentCoordinatorGroup> options,
            DegreeType degreeType, Degree degree) {
        Optional<PersistentCoordinatorGroup> instance = select(options, degreeType, degree);
        return instance.isPresent() ? instance.get() : create(options, degreeType, degree);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCoordinatorGroup create(FluentIterable<PersistentCoordinatorGroup> options, DegreeType degreeType,
            Degree degree) {
        Optional<PersistentCoordinatorGroup> instance = select(options, degreeType, degree);
        return instance.isPresent() ? instance.get() : new PersistentCoordinatorGroup(degreeType, degree);
    }

    private static Optional<PersistentCoordinatorGroup> select(FluentIterable<PersistentCoordinatorGroup> options,
            final DegreeType degreeType, final Degree degree) {
        return options.firstMatch(new Predicate<PersistentCoordinatorGroup>() {
            @Override
            public boolean apply(PersistentCoordinatorGroup group) {
                return Objects.equal(group.getDegreeType(), degreeType) && Objects.equal(group.getDegree(), degree);
            }
        });
    }
}
