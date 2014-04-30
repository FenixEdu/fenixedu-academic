package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.resourceAllocationManager;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMCurriculumHistoricApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = RAMCurriculumHistoricApp.class, path = "view", titleKey = "link.visualize")
@Mapping(module = "resourceAllocationManager", path = "/chooseExecutionYearAndDegreeCurricularPlan",
formBean = "executionYearDegreeCurricularPlanForm")
public class DegreeCurricularPlanExecutionYearDispacthActionForResourceAllocationManager extends
        DegreeCurricularPlanExecutionYearDispacthAction {
}