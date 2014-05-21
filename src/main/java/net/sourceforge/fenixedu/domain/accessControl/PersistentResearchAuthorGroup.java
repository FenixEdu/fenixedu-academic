package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentResearchAuthorGroup extends PersistentResearchAuthorGroup_Base {
    protected PersistentResearchAuthorGroup() {
        super();
    }

    @Override
    public Group toGroup() {
        return ResearchAuthorGroup.get();
    }

    public static PersistentResearchAuthorGroup getInstance() {
        return singleton(() -> find(PersistentResearchAuthorGroup.class), () -> new PersistentResearchAuthorGroup());
    }
}
