package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.adistProjectsManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "adistProjectsManagement", path = "/adistProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp", tileProperties = @Tile(
		navLocal = "/projectsManagement/costCenterNavBar.jsp")) })
public class InstitucionalProjectManagerIndexActionForADISTProjectsManagement extends
		net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}