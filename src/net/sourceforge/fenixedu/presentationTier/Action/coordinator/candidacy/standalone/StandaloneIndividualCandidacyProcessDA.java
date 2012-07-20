package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.standalone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingStandaloneIndividualCandidacyProcess", module = "coordinator", formBeanClass = net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneIndividualCandidacyProcessDA.StandaloneIndividualCandidacyForm.class)
@Forwards({ @Forward(name = "intro", path = "/caseHandlingStandaloneCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/coordinator/candidacy/standalone/listIndividualCandidacyActivities.jsp")

})
public class StandaloneIndividualCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneIndividualCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward fillCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward searchCurricularCourseByDegreePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward addCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward removeCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	throw new RuntimeException("not allowed");
    }

}
