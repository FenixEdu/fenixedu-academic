package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentGroupingGroup extends PersistentGroupingGroup_Base {
    protected PersistentGroupingGroup(Grouping grouping) {
        super();
        setGrouping(grouping);
    }

    @Override
    public GroupingGroup toGroup() {
        return GroupingGroup.get(getGrouping());
    }

    @Override
    protected void gc() {
        setGrouping(null);
        super.gc();
    }

    public static PersistentGroupingGroup getInstance(Grouping grouping) {
        PersistentGroupingGroup instance = grouping.getGroupingGroup();
        return instance != null ? instance : create(grouping);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentGroupingGroup create(Grouping grouping) {
        PersistentGroupingGroup instance = grouping.getGroupingGroup();
        return instance != null ? instance : new PersistentGroupingGroup(grouping);
    }
}
