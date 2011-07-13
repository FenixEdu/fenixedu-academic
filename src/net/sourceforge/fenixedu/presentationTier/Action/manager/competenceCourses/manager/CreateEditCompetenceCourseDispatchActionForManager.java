package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.manager;

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

@Mapping(module = "manager", path = "/createEditCompetenceCourse", attribute = "createEditCompetenceCourseForm", formBean = "createEditCompetenceCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "edit", path = "/manager/competenceCourse/editCompetenceCourse_bd.jsp"),
		@Forward(name = "createCompetenceCourse", path = "/manager/competenceCourse/createCompetenceCourse_bd.jsp"),
		@Forward(name = "showCompetenceCourse", path = "/manager/competenceCourse/showCompetenceCourse_bd.jsp") })
public class CreateEditCompetenceCourseDispatchActionForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.CreateEditCompetenceCourseDispatchAction {
}