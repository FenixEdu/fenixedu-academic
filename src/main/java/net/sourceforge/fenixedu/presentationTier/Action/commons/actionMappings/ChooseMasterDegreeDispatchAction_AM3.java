package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.ChooseMasterDegreeDispatchAction;


@Mapping(path = "/chooseMasterDegreeToCandidateStudyPlan", module = "masterDegreeAdministrativeOffice", input = "/candidate/chooseMasterDegreeToSelectCandidates.jsp", attribute = "chooseMasterDegreeForm", formBean = "chooseMasterDegreeForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/candidate/chooseMasterDegreeToSelectCandidates.jsp"),
	@Forward(name = "ChooseSuccess", path = "/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&page=0")})
public class ChooseMasterDegreeDispatchAction_AM3 extends ChooseMasterDegreeDispatchAction {

}
