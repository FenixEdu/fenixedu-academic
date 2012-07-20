package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.standalone;

import java.io.IOException;

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

@Mapping(path = "/caseHandlingStandaloneCandidacyProcess", module = "coordinator", formBeanClass = net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneCandidacyProcessDA.StandaloneCandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/coordinator/candidacy/standalone/mainCandidacyProcess.jsp")
})
public class StandaloneCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareExecuteSendToCoordinator(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeSendToCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareExecuteIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward prepareIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	throw new RuntimeException("not allowed");
    }

    public ActionForward executeIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	throw new RuntimeException("not allowed");
    }

}
