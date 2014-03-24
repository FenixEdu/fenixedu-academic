package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseExecutionYearToCandidateStudyPlanDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/displayCandidateListToMakeStudyPlan", module = "masterDegreeAdministrativeOffice",
        input = "/candidate/displayCandidateListToMakeStudyPlan_bd.jsp", formBean = "chooseSecondMasterDegreeForm",
        functionality = ChooseExecutionYearToCandidateStudyPlanDA.class)
@Forwards({
        @Forward(name = "PrepareSuccess",
                path = "/masterDegreeAdministrativeOffice/candidate/displayCandidateListToMakeStudyPlan_bd.jsp"),
        @Forward(name = "PrepareSecondChooseMasterDegreeReady",
                path = "/masterDegreeAdministrativeOffice/candidate/secondChooseMasterDegree_bd.jsp"),
        @Forward(name = "ChooseReady",
                path = "/masterDegreeAdministrativeOffice/displayCourseListToStudyPlan.do?method=chooseMasterDegree&page=0"),
        @Forward(name = "ChooseSuccess",
                path = "/masterDegreeAdministrativeOffice/displayCourseListToStudyPlan.do?method=prepareSelectCourseList&page=0"),
        @Forward(name = "PrintReady", path = "/masterDegreeAdministrativeOffice/candidate/studyPlanTemplate.jsp"),
        @Forward(name = "BackError", path = "/masterDegreeAdministrativeOffice/candidate/backErrorPage_bd.jsp") })
public class DisplayCandidateListToMakeStudyPlan extends MakeCandidateStudyPlanDispatchAction {

}
