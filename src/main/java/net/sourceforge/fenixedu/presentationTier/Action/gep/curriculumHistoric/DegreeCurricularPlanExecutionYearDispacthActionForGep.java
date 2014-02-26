package net.sourceforge.fenixedu.presentationTier.Action.gep.curriculumHistoric;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction;
import net.sourceforge.fenixedu.presentationTier.Action.gep.GepApplication.GepPortalApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = GepPortalApp.class, path = "curriculum-historic", titleKey = "link.curriculumHistoric",
        bundle = "CurriculumHistoricResources")
@Mapping(module = "gep", path = "/chooseExecutionYearAndDegreeCurricularPlan",
        input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0")
@Forwards({ @Forward(name = "chooseExecutionYear", path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp"),
        @Forward(name = "showActiveCurricularCourses", path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp") })
public class DegreeCurricularPlanExecutionYearDispacthActionForGep extends DegreeCurricularPlanExecutionYearDispacthAction {
}