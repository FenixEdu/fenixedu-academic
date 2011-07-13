package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.coordinator;

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

@Mapping(module = "coordinator", path = "/manageThesis", scope = "session", parameter = "method")
@Forwards(value = {
		@Forward(name = "search-student", path = "/coordinator/thesis/searchStudent.jsp"),
		@Forward(name = "review-proposal", path = "/coordinator/thesis/reviewProposal.jsp"),
		@Forward(name = "change-information", path = "/coordinator/thesis/changeInformation.jsp"),
		@Forward(name = "editParticipant", path = "/coordinator/thesis/editParticipant.jsp"),
		@Forward(name = "view-submitted", path = "/coordinator/thesis/viewSubmitted.jsp"),
		@Forward(name = "change-information-with-docs", path = "/coordinator/thesis/changeInformationWithDocs.jsp"),
		@Forward(name = "viewOperationsThesis", path = "/student/thesis/viewOperationsThesis.jsp"),
		@Forward(name = "select-unit", path = "/coordinator/thesis/selectUnit.jsp"),
		@Forward(name = "view-confirmed", path = "/coordinator/thesis/viewConfirmed.jsp"),
		@Forward(name = "list-thesis", path = "/coordinator/thesis/listThesis.jsp"),
		@Forward(name = "select-person", path = "/coordinator/thesis/selectPerson.jsp"),
		@Forward(name = "view-approved", path = "/coordinator/thesis/viewApproved.jsp"),
		@Forward(name = "view-evaluated", path = "/coordinator/thesis/viewEvaluated.jsp"),
		@Forward(name = "collect-basic-information", path = "/coordinator/thesis/collectBasicInformation.jsp"),
		@Forward(name = "edit-thesis", path = "/coordinator/thesis/editThesis.jsp") })
public class ManageThesisDAForCoordinator extends net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ManageThesisDA {
}