package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.itProjectsManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "itProjectsManagement", path = "/institucionalProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp") })
public class InstitucionalProjectManagerIndexActionForItProjectsManagement extends
        net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}