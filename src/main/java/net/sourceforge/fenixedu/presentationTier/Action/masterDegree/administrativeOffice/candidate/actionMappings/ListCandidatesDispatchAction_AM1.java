package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.ListCandidatesDispatchAction;


@Mapping(path = "/visualizeCandidates", module = "masterDegreeAdministrativeOffice", input = "/chooseCandidateList.jsp", attribute = "listCandidatesForm", formBean = "listCandidatesForm")
@Forwards(value = {
	@Forward(name = "PrepareReady", path = "/chooseCandidateList.jsp"),
	@Forward(name = "ActionReady", path = "/visualizePersonCandidateList.jsp"),
	@Forward(name = "VisualizeCandidate", path = "/visualizeCandidate.jsp"),
	@Forward(name = "ChooseCandidate", path = "/candidateList.jsp")})
public class ListCandidatesDispatchAction_AM1 extends ListCandidatesDispatchAction {

}
