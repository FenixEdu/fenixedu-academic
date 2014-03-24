package net.sourceforge.fenixedu.presentationTier.Action.commons;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/chooseMasterDegreeToSelectCandidates", module = "masterDegreeAdministrativeOffice",
        input = "/candidate/chooseMasterDegreeToSelectCandidates_bd.jsp", formBean = "chooseMasterDegreeForm",
        functionality = ChooseExecutionYearToSelectCandidatesDA.class)
@Forwards({
        @Forward(name = "PrepareSuccess",
                path = "/masterDegreeAdministrativeOffice/candidate/chooseMasterDegreeToSelectCandidates.jsp"),
        @Forward(name = "ChooseSuccess",
                path = "/masterDegreeAdministrativeOffice/displayListToSelectCandidates.do?method=prepareSelectCandidates&page=0") })
public class ChooseMasterDegreeToSelectCandidatesDA extends ChooseMasterDegreeDispatchAction {

}
