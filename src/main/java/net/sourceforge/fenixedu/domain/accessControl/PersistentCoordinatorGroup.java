package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.core.groups.Group;

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
        return getInstance(degree.getCoordinatorGroupSet().stream(), null, degree);
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

    private static PersistentCoordinatorGroup getInstance(Stream<PersistentCoordinatorGroup> options, DegreeType degreeType,
            Degree degree) {
        return singleton(() -> select(options, degreeType, degree), () -> new PersistentCoordinatorGroup(degreeType, degree));
    }

    private static Optional<PersistentCoordinatorGroup> select(Stream<PersistentCoordinatorGroup> options,
            final DegreeType degreeType, final Degree degree) {
        return options.filter(
                group -> Objects.equals(group.getDegreeType(), degreeType) && Objects.equals(group.getDegree(), degree))
                .findAny();
    }
}
