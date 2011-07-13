package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.operator;

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

@Mapping(module = "operator", path = "/competenceCourseManagement", input = "/competenceCourseManagement.do?method=showAllCompetences", attribute = "competenceCourseForm", formBean = "competenceCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "readCompetenceCourses", path = "/competenceCourseManagement.do?method=showDepartmentCompetenceCourses"),
		@Forward(name = "showCompetenceCourses", path = "/manager/competenceCourse/showAllCompetenceCourses_bd.jsp", tileProperties = @Tile(navLocal = "/operator/competenceCourse/mainMenu.jsp")),
		@Forward(name = "showCompetenceCourse", path = "df.page.showCompetenceCourse"),
		@Forward(name = "chooseDepartment", path = "df.page.chooseDepartment") })
public class CompetenceCourseDispatchActionForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.CompetenceCourseDispatchAction {
}