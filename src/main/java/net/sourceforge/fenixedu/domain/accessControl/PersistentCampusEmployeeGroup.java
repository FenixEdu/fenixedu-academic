package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;

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
        return singleton(() -> Optional.ofNullable(campus.getCampusEmployeeGroup()), () -> new PersistentCampusEmployeeGroup(
                campus));
    }
}
