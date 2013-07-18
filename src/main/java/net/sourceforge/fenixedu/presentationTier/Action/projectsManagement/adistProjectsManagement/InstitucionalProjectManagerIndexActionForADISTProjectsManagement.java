package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.adistProjectsManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "adistProjectsManagement", path = "/adistProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp") })
public class InstitucionalProjectManagerIndexActionForADISTProjectsManagement extends
        net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}