package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

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
        return singleton(() -> Optional.ofNullable(degree.getScientificCommissionGroup()),
                () -> new PersistentScientificCommissionGroup(degree));
    }
}
