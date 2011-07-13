package net.sourceforge.fenixedu.presentationTier.Action.research.activity.researcher;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/activities/editResearchActivity", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "prepareEditEventEditionParticipants", path = "/activities/editResearchActivity.do?method=prepareEditEventEditionParticipants"),
		@Forward(name = "prepareEditJournalIssueParticipants", path = "/activities/editResearchActivity.do?method=prepareEditJournalIssueParticipants"),
		@Forward(name = "EditParticipants", path = "/researcher/activities/editResearchActivityParticipants.jsp"),
		@Forward(name = "prepareEditEventParticipants", path = "/activities/editResearchActivity.do?method=prepareEditEventParticipants"),
		@Forward(name = "prepareEditScientificJournalParticipants", path = "/activities/editResearchActivity.do?method=prepareEditScientificJournalParticipants"),
		@Forward(name = "EditData", path = "/researcher/activities/editResearchActivityData.jsp"),
		@Forward(name = "prepareEditCooperationParticipants", path = "/activities/editResearchActivity.do?method=prepareEditCooperationParticipants"),
		@Forward(name = "EditResearchActivity", path = "/researcher/activities/editResearchActivity.jsp") })
public class EditResearchActivityDispatchActionForResearcher extends net.sourceforge.fenixedu.presentationTier.Action.research.activity.EditResearchActivityDispatchAction {
}