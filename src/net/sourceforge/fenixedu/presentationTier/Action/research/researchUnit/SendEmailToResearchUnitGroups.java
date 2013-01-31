package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import net.sourceforge.fenixedu.presentationTier.Action.messaging.UnitMailSenderAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/sendEmailToResearchUnitGroups", scope = "session", parameter = "method")
public class SendEmailToResearchUnitGroups extends UnitMailSenderAction {

}
