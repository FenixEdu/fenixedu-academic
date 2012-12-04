package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "coordinator", path = "/manageThesis", scope = "session", parameter = "method")
@Forwards(value = {
	@Forward(name = "search-student", path = "/coordinator/thesis/searchStudent.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "review-proposal", path = "/coordinator/thesis/reviewProposal.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "change-information", path = "/coordinator/thesis/changeInformation.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "editParticipant", path = "/coordinator/thesis/editParticipant.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "view-submitted", path = "/coordinator/thesis/viewSubmitted.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "change-information-with-docs", path = "/coordinator/thesis/changeInformationWithDocs.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "viewOperationsThesis", path = "/student/thesis/viewOperationsThesis.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "select-unit", path = "/coordinator/thesis/selectUnit.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "view-confirmed", path = "/coordinator/thesis/viewConfirmed.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "list-thesis", path = "/coordinator/thesis/listThesis.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "select-person", path = "/coordinator/thesis/selectPerson.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "view-approved", path = "/coordinator/thesis/viewApproved.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "view-evaluated", path = "/coordinator/thesis/viewEvaluated.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "collect-basic-information", path = "/coordinator/thesis/collectBasicInformation.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")),
	@Forward(name = "edit-thesis", path = "/coordinator/thesis/editThesis.jsp", tileProperties = @Tile(title = "private.coordinator.management.courses.dissertations")) })
public class ManageThesisDAForCoordinator extends
	net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ManageThesisDA {
}