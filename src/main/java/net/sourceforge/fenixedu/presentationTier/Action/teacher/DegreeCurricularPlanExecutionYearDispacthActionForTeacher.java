package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherApplication.TeacherTeachingApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = TeacherTeachingApp.class, path = "view-curriculum-historic",
        titleKey = "link.curriculumHistoric.consult", bundle = "CurriculumHistoricResources")
@Mapping(module = "teacher", path = "/chooseExecutionYearAndDegreeCurricularPlan",
        formBean = "executionYearDegreeCurricularPlanForm")
@Forwards({ @Forward(name = "chooseExecutionYear", path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp"),
        @Forward(name = "showActiveCurricularCourses", path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp") })
public class DegreeCurricularPlanExecutionYearDispacthActionForTeacher extends DegreeCurricularPlanExecutionYearDispacthAction {
}