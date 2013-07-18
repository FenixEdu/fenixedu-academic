package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.adistInstitucionalProjectsManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "adistInstitucionalProjectsManagement", path = "/adistInstitucionalProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp") })
public class InstitucionalProjectManagerIndexActionForInstitucionalADISTProjectsManagement extends
        net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}