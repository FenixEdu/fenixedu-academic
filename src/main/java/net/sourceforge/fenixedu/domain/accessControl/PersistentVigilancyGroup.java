package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentVigilancyGroup extends PersistentVigilancyGroup_Base {
    protected PersistentVigilancyGroup(Vigilancy vigilancy) {
        super();
        setVigilancy(vigilancy);
    }

    @Override
    public Group toGroup() {
        return VigilancyGroup.get(getVigilancy());
    }

    @Override
    protected void gc() {
        setVigilancy(null);
        super.gc();
    }

    public static PersistentVigilancyGroup getInstance(Vigilancy vigilancy) {
        return singleton(() -> Optional.ofNullable(vigilancy.getVigilancyGroup()), () -> new PersistentVigilancyGroup(vigilancy));
    }
}
