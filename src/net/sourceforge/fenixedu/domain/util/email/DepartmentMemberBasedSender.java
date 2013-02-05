package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.UnitMembersGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

public class DepartmentMemberBasedSender extends DepartmentMemberBasedSender_Base {

    public DepartmentMemberBasedSender() {
        super();
    }

    public DepartmentMemberBasedSender(final Unit unit, final String fromAddress, final Group members) {
        super();
        init(unit, fromAddress, members);
    }

    @Override
    public String getFromName(final Person person) {
        return String.format("%s - %s", getUnit().getName(), person.getName());
    }

    @Override
    public String getFromName() {
        final Person person = AccessControl.getPerson();
        return getFromName(person);
    }

    @Service
    public static DepartmentMemberBasedSender newInstance(final Unit unit) {
        return new DepartmentMemberBasedSender(unit, Sender.getNoreplyMail(), new UnitMembersGroup(unit));
    }

}
