package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class AllAlumniGroup extends RoleTypeGroup {

    public AllAlumniGroup() {
	super(RoleType.ALUMNI);
    }

    @Override
    public String getPresentationNameKey() {
	return "label.name." + getClass().getSimpleName();
    }

}
