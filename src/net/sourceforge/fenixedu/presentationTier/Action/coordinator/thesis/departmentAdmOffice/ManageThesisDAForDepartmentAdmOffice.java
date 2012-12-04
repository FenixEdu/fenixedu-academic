package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/manageThesis", scope = "session", parameter = "method")
@Forwards(value = {
	@Forward(name = "search-student", path = "thesis-search-student"),
	@Forward(name = "review-proposal", path = "thesis-review-proposal"),
	@Forward(name = "change-information", path = "thesis-change-information"),
	@Forward(name = "view-submitted", path = "thesis-view-submitted"),
	@Forward(name = "editParticipant", path = "thesis-edit-participant"),
	@Forward(name = "select-unit", path = "thesis-select-unit"),
	@Forward(name = "view-confirmed", path = "thesis-view-confirmed"),
	@Forward(name = "list-thesis", path = "/coordinator/thesis/listThesis.jsp", tileProperties = @Tile(title = "private.administrationofcreditsofdepartmentteachers.dissertations.dissertationsmanagement")),
	@Forward(name = "select-person", path = "/coordinator/thesis/selectPerson.jsp"),
	@Forward(name = "view-approved", path = "thesis-view-approved"),
	@Forward(name = "view-evaluated", path = "thesis-view-evaluated"),
	@Forward(name = "collect-basic-information", path = "thesis-collect-basic-information"),
	@Forward(name = "edit-thesis", path = "/coordinator/thesis/editThesis.jsp") })
public class ManageThesisDAForDepartmentAdmOffice extends
	net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ManageThesisDA {
}