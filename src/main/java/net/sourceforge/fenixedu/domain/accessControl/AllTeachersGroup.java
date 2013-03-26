package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class AllTeachersGroup extends RoleTypeGroup {

    public AllTeachersGroup() {
        super(RoleType.TEACHER);
    }

    @Override
    public String getPresentationNameKey() {
        return "label.name." + getClass().getSimpleName();
    }

}
