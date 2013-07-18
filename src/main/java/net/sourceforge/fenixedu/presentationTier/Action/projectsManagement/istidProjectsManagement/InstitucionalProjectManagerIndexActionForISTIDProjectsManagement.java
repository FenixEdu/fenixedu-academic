package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.istidProjectsManagement;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "istidProjectsManagement", path = "/istidProjectIndex", scope = "session")
@Forwards(value = { @Forward(name = "success", path = "/projectsManagement/firstPage.jsp") })
public class InstitucionalProjectManagerIndexActionForISTIDProjectsManagement extends
        net.sourceforge.fenixedu.presentationTier.Action.projectsManagement.InstitucionalProjectManagerIndexAction {
}