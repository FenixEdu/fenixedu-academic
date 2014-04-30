package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentVigilancyGroup instance = vigilancy.getVigilancyGroup();
        return instance != null ? instance : create(vigilancy);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentVigilancyGroup create(Vigilancy vigilancy) {
        PersistentVigilancyGroup instance = vigilancy.getVigilancyGroup();
        return instance != null ? instance : new PersistentVigilancyGroup(vigilancy);
    }

}
