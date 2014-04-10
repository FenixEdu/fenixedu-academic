package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;

public class PersistentResearchAuthorGroup extends PersistentResearchAuthorGroup_Base {
    protected PersistentResearchAuthorGroup() {
        super();
    }

    @Override
    public Group toGroup() {
        return ResearchAuthorGroup.get();
    }

    public static PersistentResearchAuthorGroup getInstance() {
        Optional<PersistentResearchAuthorGroup> instance = find(PersistentResearchAuthorGroup.class);
        return instance.isPresent() ? instance.get() : create();
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentResearchAuthorGroup create() {
        Optional<PersistentResearchAuthorGroup> instance = find(PersistentResearchAuthorGroup.class);
        return instance.isPresent() ? instance.get() : new PersistentResearchAuthorGroup();
    }
}
