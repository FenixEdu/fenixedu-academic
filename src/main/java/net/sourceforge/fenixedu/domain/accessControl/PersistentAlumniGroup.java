package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentAlumniGroup instance = degree == null ? find(PersistentAlumniGroup.class).orNull() : degree.getAlumniGroup();
        return instance != null ? instance : create(degree);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentAlumniGroup create(Degree degree) {
        PersistentAlumniGroup instance = degree == null ? find(PersistentAlumniGroup.class).orNull() : degree.getAlumniGroup();
        return instance != null ? instance : new PersistentAlumniGroup(degree);
    }
}
