package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class DepartmentMemberBasedSender extends DepartmentMemberBasedSender_Base {

    public DepartmentMemberBasedSender() {
        super();
    }

    public DepartmentMemberBasedSender(final Unit unit, final String fromAddress, final Group members) {
        super();
        init(unit, fromAddress, members);
    }

    @Atomic
    public static DepartmentMemberBasedSender newInstance(final Unit unit) {
        return new DepartmentMemberBasedSender(unit, Sender.getNoreplyMail(), UnitGroup.recursiveWorkers(unit));
    }

}
