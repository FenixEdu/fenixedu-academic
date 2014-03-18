package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.ListCandidatesDispatchAction;


@Mapping(path = "/editCandidates", module = "masterDegreeAdministrativeOffice", input = "/chooseCandidateList.jsp", attribute = "listCandidatesForm", formBean = "listCandidatesForm")
@Forwards(value = {
	@Forward(name = "PrepareReady", path = "/chooseCandidateList.jsp"),
	@Forward(name = "ActionReady", path = "/editCandidate.do?method=prepareEdit&page=0"),
	@Forward(name = "ChooseCandidate", path = "/candidateList.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class)})
public class ListCandidatesDispatchAction_AM2 extends ListCandidatesDispatchAction {

}
