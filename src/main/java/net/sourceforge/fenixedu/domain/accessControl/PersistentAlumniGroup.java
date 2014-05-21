package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.Degree;

public class PersistentAlumniGroup extends PersistentAlumniGroup_Base {
    protected PersistentAlumniGroup(Degree degree) {
        super();
        setDegree(degree);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public org.fenixedu.bennu.core.groups.Group toGroup() {
        return AlumniGroup.get(getDegree());
    }

    @Override
    protected void gc() {
        setDegree(null);
        super.gc();
    }

    public static PersistentAlumniGroup getInstance() {
        return getInstance(null);
    }

    public static PersistentAlumniGroup getInstance(Degree degree) {
        return singleton(
                () -> degree == null ? find(PersistentAlumniGroup.class) : Optional.ofNullable(degree.getAlumniGroup()),
                        () -> new PersistentAlumniGroup(degree));
    }
}
