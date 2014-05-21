package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.Grouping;

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
        return singleton(() -> Optional.ofNullable(grouping.getGroupingGroup()), () -> new PersistentGroupingGroup(grouping));
    }
}
