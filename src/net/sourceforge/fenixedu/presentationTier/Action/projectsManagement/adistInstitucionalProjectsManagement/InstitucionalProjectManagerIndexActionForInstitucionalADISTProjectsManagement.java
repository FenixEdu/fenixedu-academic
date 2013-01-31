package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.adistInstitucionalProjectsManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "adistInstitucionalProjectsManagement", path = "/adistInstitucionalProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp", tileProperties = @Tile(
		navLocal = "/projectsManagement/costCenterNavBar.jsp")) })
public class InstitucionalProjectManagerIndexActionForInstitucionalADISTProjectsManagement extends
		net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}