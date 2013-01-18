package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.ResearchUnitElementGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class ResearchUnitBasedSender extends ResearchUnitBasedSender_Base {

    public ResearchUnitBasedSender() {
	super();
    }

    public ResearchUnitBasedSender(Unit unit, String fromAddress, Group members) {
	super();
	init(unit, fromAddress, members);
    }

    @Override
    public String getFromName(Person person) {
	return String.format("%s - %s", getUnit().getAcronym(), person.getName());
    }

    @Service
    public static ResearchUnitBasedSender newInstance(Unit unit) {
	return new ResearchUnitBasedSender(unit, Sender.getNoreplyMail(), new ResearchUnitElementGroup((ResearchUnit) unit));
    }
}
