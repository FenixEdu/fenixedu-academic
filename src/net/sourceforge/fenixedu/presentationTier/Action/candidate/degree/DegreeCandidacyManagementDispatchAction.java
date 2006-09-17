package net.sourceforge.fenixedu.presentationTier.Action.candidate.degree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.workflow.CandidacyOperation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DegreeCandidacyManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("showWelcome");
    }

    public ActionForward showCandidacyDetails(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException {

	final DegreeCandidacy candidacy = getCandidacy(request);
	request.setAttribute("candidacy", candidacy);

	final SortedSet<Operation> operations = new TreeSet<Operation>();
	operations.addAll(candidacy.getActiveCandidacySituation().getOperationsForPerson(
		getLoggedPerson(request)));
	request.setAttribute("operations", operations);

	return mapping.findForward("showCandidacyDetails");
    }

    public ActionForward doOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException,
	    FenixServiceException {
	final CandidacyOperation operation = (CandidacyOperation) getCandidacy(request)
		.getActiveCandidacySituation().getOperationByTypeAndPerson(getOperationType(request),
			getLoggedPerson(request));
	request.setAttribute("operation", operation);
	request.setAttribute("candidacy", getCandidacy(request));
	request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));

	if (operation.isInput()) {
	    request.setAttribute("currentForm", operation.moveToNextForm());

	    return mapping.findForward("fillData");

	} else {
	    return executeOperation(mapping, form, request, response, operation);
	}

    }

    private String getSchemaSuffixForPerson(HttpServletRequest request) {
	return (getUserView(request).getPerson().hasRole(RoleType.EMPLOYEE)) ? ".forEmployee" : "";
    }

    public ActionForward processForm(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException {
	request.setAttribute("candidacy", getCandidacy(request));

	final CandidacyOperation operation = (CandidacyOperation) RenderUtils.getViewState(
		"operation-view-state").getMetaObject().getObject();
	request.setAttribute("operation", operation);
	request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));

	if (!validateCurrentForm(request)) {
	    return mapping.findForward("fillData");
	}

	if (operation.hasMoreForms()) {
	    request.setAttribute("currentForm", operation.moveToNextForm());
	    return mapping.findForward("fillData");
	} else {
	    return executeOperation(mapping, actionForm, request, response, operation);
	}

    }

    private boolean validateCurrentForm(HttpServletRequest request) {
	final Form form = (Form) RenderUtils.getViewState("fillData" + getCurrentFormPosition(request))
		.getMetaObject().getObject();
	final List<LabelFormatter> messages = form.validate();
	if (!messages.isEmpty()) {
	    request.setAttribute("formMessages", solveLabelFormatterArgs(request, messages
		    .toArray(new LabelFormatter[messages.size()])));
	    request.setAttribute("currentForm", form);

	    return false;
	} else {
	    return true;
	}

    }

    private ActionForward executeOperation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    final CandidacyOperation candidacyOperation) throws FenixServiceException,
	    FenixFilterException, FenixActionException {

	try {
	    final IUserView userView = getUserView(request);
	    ServiceUtils.executeService(userView, "ExecuteStateOperation", new Object[] {
		    candidacyOperation, getLoggedPerson(request) });

	    if (candidacyOperation.getType() == CandidacyOperationType.PRINT_SCHEDULE) {
		final List<InfoLesson> infoLessons = (List) ServiceUtils
			.executeService(userView, "ReadStudentTimeTable", new Object[] { getCandidacy(
				request).getRegistration() });
		request.setAttribute("infoLessons", infoLessons);

		return mapping.findForward("printSchedule");
	    } else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_REGISTRATION_DECLARATION) {
		request.setAttribute("registration", getCandidacy(request).getRegistration());
		request.setAttribute("executionYear", getCandidacy(request).getExecutionDegree()
			.getExecutionYear());

		return mapping.findForward("printRegistrationDeclaration");
	    } else if (candidacyOperation.getType() == CandidacyOperationType.PRINT_SYSTEM_ACCESS_DATA) {
		request.setAttribute("person", userView.getPerson());

		return mapping.findForward("printSystemAccessData");
	    } else if (candidacyOperation.getType() == CandidacyOperationType.FILL_PERSONAL_DATA) {
		request.setAttribute("aditionalInformation", getResources(request).getMessage(
			"label.candidacy.username.changed.message",
			userView.getPerson().getIstUsername()));
	    }

	    request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));
	    request.setAttribute("candidacyID", candidacyOperation.getCandidacy().getIdInternal());
	    
	    return showCandidacyDetails(mapping, form, request, response);

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    return showCurrentForm(mapping, form, request, response);
	}

    }

    public ActionForward showCurrentForm(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException {

	request.setAttribute("candidacy", getCandidacy(request));
	request.setAttribute("operation", RenderUtils.getViewState("operation-view-state")
		.getMetaObject().getObject());
	request.setAttribute("currentForm", RenderUtils.getViewState(
		"fillData" + getCurrentFormPosition(request)).getMetaObject().getObject());
	request.setAttribute("schemaSuffix", getSchemaSuffixForPerson(request));

	return mapping.findForward("fillData");
    }

    private DegreeCandidacy getCandidacy(HttpServletRequest request) {
	return (DegreeCandidacy) rootDomainObject.readCandidacyByOID(Integer.valueOf(getFromRequest(
		request, "candidacyID").toString()));
    }

    private Integer getCurrentFormPosition(HttpServletRequest request) {
	return getRequestParameterAsInteger(request, "currentFormPosition");
    }

    private CandidacyOperationType getOperationType(HttpServletRequest request) {
	return CandidacyOperationType.valueOf(request.getParameter("operationType"));
    }

    @Override
    protected Map<String, String> getMessageResourceProviderBundleMappings() {
	final Map<String, String> bundleMappings = new HashMap<String, String>();
	bundleMappings.put("enum", "ENUMERATION_RESOURCES");
	bundleMappings.put("application", "");

	return bundleMappings;
    }

}
