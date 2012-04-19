package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.secondCycle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingSecondCycleIndividualCandidacyProcess", module = "coordinator", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/caseHandlingSecondCycleCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/secondCycle/listIndividualCandidacyActivities.jsp") })
public class SecondCycleIndividualCandidacyProcessDA extends net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle.SecondCycleIndividualCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }
}
