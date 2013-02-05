package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/curricularCoursesCompetenceCourse", attribute = "curricularCoursesCompetenceCourseForm",
        formBean = "curricularCoursesCompetenceCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseCurricularCourses", path = "/manager/competenceCourse/chooseCurricularCourses_bd.jsp"),
        @Forward(name = "chooseDegreeCurricularPlan", path = "/manager/competenceCourse/chooseDegreeCurricularPlan_bd.jsp"),
        @Forward(name = "showCompetenceCourse", path = "/competenceCourseManagement.do?method=showCompetenceCourse") })
public class CurricularCourseCompetenceCourseDispatchActionForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.CurricularCourseCompetenceCourseDispatchAction {
}