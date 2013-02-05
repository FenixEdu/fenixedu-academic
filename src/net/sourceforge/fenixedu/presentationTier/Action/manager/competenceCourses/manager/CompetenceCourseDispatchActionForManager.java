package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/competenceCourseManagement",
        input = "/competenceCourseManagement.do?method=showAllCompetences", attribute = "competenceCourseForm",
        formBean = "competenceCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "readCompetenceCourses", path = "/competenceCourseManagement.do?method=showDepartmentCompetenceCourses"),
        @Forward(name = "showCompetenceCourses", path = "/manager/competenceCourse/showAllCompetenceCourses_bd.jsp"),
        @Forward(name = "showCompetenceCourse", path = "/manager/competenceCourse/showCompetenceCourse_bd.jsp"),
        @Forward(name = "chooseDepartment", path = "df.page.chooseDepartment") })
public class CompetenceCourseDispatchActionForManager extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses.CompetenceCourseDispatchAction {
}