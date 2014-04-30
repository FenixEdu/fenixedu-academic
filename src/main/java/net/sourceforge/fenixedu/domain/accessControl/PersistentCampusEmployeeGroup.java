package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.space.Campus;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentCampusEmployeeGroup extends PersistentCampusEmployeeGroup_Base {
    public PersistentCampusEmployeeGroup(Campus campus) {
        super();
        setCampus(campus);
    }

    @Override
    public Group toGroup() {
        return CampusEmployeeGroup.get(getCampus());
    }

    @Override
    protected void gc() {
        setCampus(null);
        super.gc();
    }

    public static PersistentCampusEmployeeGroup getInstance(Campus campus) {
        PersistentCampusEmployeeGroup instance = campus.getCampusEmployeeGroup();
        return instance != null ? instance : create(campus);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCampusEmployeeGroup create(Campus campus) {
        PersistentCampusEmployeeGroup instance = campus.getCampusEmployeeGroup();
        return instance != null ? instance : new PersistentCampusEmployeeGroup(campus);
    }
}
