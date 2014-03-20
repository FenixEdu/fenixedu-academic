package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.resourceAllocationManager;

import net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric.ShowCurriculumHistoricAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "resourceAllocationManager", path = "/showCurriculumHistoric",
        functionality = DegreeCurricularPlanExecutionYearDispacthActionForResourceAllocationManager.class)
public class ShowCurriculumHistoricActionForResourceAllocationManager extends ShowCurriculumHistoricAction {
}