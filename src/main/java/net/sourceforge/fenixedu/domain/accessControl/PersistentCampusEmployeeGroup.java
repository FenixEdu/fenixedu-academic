package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentCampusEmployeeGroup extends PersistentCampusEmployeeGroup_Base {
    public PersistentCampusEmployeeGroup(Space campus) {
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

    public static PersistentCampusEmployeeGroup getInstance(Space campus) {
        PersistentCampusEmployeeGroup instance = campus.getCampusEmployeeGroup();
        return instance != null ? instance : create(campus);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCampusEmployeeGroup create(Space campus) {
        PersistentCampusEmployeeGroup instance = campus.getCampusEmployeeGroup();
        return instance != null ? instance : new PersistentCampusEmployeeGroup(campus);
    }
}
