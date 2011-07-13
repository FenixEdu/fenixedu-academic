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

@Mapping(module = "operator", path = "/curricularCoursesCompetenceCourse", attribute = "curricularCoursesCompetenceCourseForm", formBean = "curricularCoursesCompetenceCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "chooseCurricularCourses", path = "df.page.chooseCurricularCourses"),
		@Forward(name = "chooseDegreeCurricularPlan", path = "df.page.chooseDegreeCurricularPlan"),
		@Forward(name = "showCompetenceCourse", path = "/competenceCourseManagement.do?method=showCompetenceCourse") })
public class CurricularCourseCompetenceCourseDispatchActionForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.CurricularCourseCompetenceCourseDispatchAction {
}