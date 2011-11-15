package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.istidProjectsManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "istidProjectsManagement", path = "/istidProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp", tileProperties = @Tile(navLocal = "/projectsManagement/costCenterNavBar.jsp")) })
public class InstitucionalProjectManagerIndexActionForISTIDProjectsManagement extends net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}