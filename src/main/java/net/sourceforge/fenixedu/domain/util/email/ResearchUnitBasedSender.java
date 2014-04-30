package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class ResearchUnitBasedSender extends ResearchUnitBasedSender_Base {

    public ResearchUnitBasedSender() {
        super();
    }

    public ResearchUnitBasedSender(Unit unit, String fromAddress, Group members) {
        super();
        init(unit, fromAddress, members);
    }

    @Atomic
    public static ResearchUnitBasedSender newInstance(Unit unit) {
        return new ResearchUnitBasedSender(unit, Sender.getNoreplyMail(), UnitGroup.get(unit,
                AccountabilityTypeEnum.RESEARCH_CONTRACT, false));
    }
}
