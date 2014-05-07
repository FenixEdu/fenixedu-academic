package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import net.sourceforge.fenixedu.presentationTier.Action.messaging.UnitMailSenderAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/sendEmailToResearchUnitGroups", functionality = ResearchUnitFunctionalities.class)
public class SendEmailToResearchUnitGroups extends UnitMailSenderAction {

}
