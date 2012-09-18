package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class AllResearchersGroup extends RoleTypeGroup {

    public AllResearchersGroup() {
	super(RoleType.RESEARCHER);
    }

    @Override
    public String getPresentationNameKey() {
	return "label.name." + getClass().getSimpleName();
    }

}
