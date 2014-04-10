package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentScientificCommissionGroup extends PersistentScientificCommissionGroup_Base {
    protected PersistentScientificCommissionGroup(Degree degree) {
        super();
        setDegree(degree);
    }

    @Override
    public Group toGroup() {
        return ScientificCommissionGroup.get(getDegree());
    }

    @Override
    protected void gc() {
        setDegree(null);
        super.gc();
    }

    public static PersistentScientificCommissionGroup getInstance(Degree degree) {
        PersistentScientificCommissionGroup instance = degree.getScientificCommissionGroup();
        return instance != null ? instance : create(degree);
    }

    private static PersistentScientificCommissionGroup create(Degree degree) {
        PersistentScientificCommissionGroup instance = degree.getScientificCommissionGroup();
        return instance != null ? instance : new PersistentScientificCommissionGroup(degree);
    }
}
