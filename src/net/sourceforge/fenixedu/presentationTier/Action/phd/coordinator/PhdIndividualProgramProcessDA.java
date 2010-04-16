package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdRegistrationConclusionBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

@Mapping(path = "/phdIndividualProgramProcess", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "manageProcesses", path = "/phd/coordinator/manageProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/coordinator/viewProcess.jsp"),

@Forward(name = "viewInactiveProcesses", path = "/phd/coordinator/viewInactiveProcesses.jsp"),

@Forward(name = "searchResults", path = "/phd/coordinator/searchResults.jsp"),

@Forward(name = "viewAlertMessages", path = "/phd/coordinator/viewAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/coordinator/viewProcessAlertMessages.jsp"),

@Forward(name = "viewCurriculum", path = "/phd/coordinator/viewCurriculum.jsp"),

@Forward(name = "manageEnrolments", path = "/phd/coordinator/manageEnrolments.jsp")

})
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    private static final PredicateContainer<?>[] CANDIDACY_CATEGORY = { PhdCandidacyPredicateContainer.DELIVERED,
	    PhdCandidacyPredicateContainer.PENDING, PhdCandidacyPredicateContainer.APPROVED,
	    PhdCandidacyPredicateContainer.CONCLUDED };

    private static final PredicateContainer<?>[] SEMINAR_CATEGORY = { PhdSeminarPredicateContainer.SEMINAR_PROCESS_STARTED,
	    PhdSeminarPredicateContainer.AFTER_FIRST_SEMINAR_REUNION };

    private static final PredicateContainer<?>[] THESIS_CATEGORY = { PhdThesisPredicateContainer.PROVISIONAL_THESIS_DELIVERED,
	    PhdThesisPredicateContainer.DISCUSSION_SCHEDULED };

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
	SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
	searchBean.setPhdPrograms(getManagedPhdPrograms(request));
	searchBean.setFilterPhdProcesses(false);
	return searchBean;
    }

    private Set<PhdProgram> getManagedPhdPrograms(HttpServletRequest request) {
	final Set<PhdProgram> result = new HashSet<PhdProgram>();
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	for (final Coordinator coordinator : getLoggedPerson(request).getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegree().hasPhdProgram()
		    && coordinator.getExecutionDegree().getExecutionYear() == currentExecutionYear) {
		result.add(coordinator.getExecutionDegree().getDegree().getPhdProgram());
	    }
	}

	return result;
    }

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	if (process != null && process.hasRegistration()) {
	    request.setAttribute("registrationConclusionBean", new PhdRegistrationConclusionBean(process.getRegistration()));
	}

	return super.viewProcess(mapping, form, request, response);
    }

    public ActionForward manageEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	request.setAttribute("enrolments", process.getRegistration().getLastStudentCurricularPlan().getEnrolments());

	return mapping.findForward("manageEnrolments");
    }

    @Override
    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
	RenderUtils.invalidateViewState();

	return forwardToManageProcesses(mapping, request, searchBean);
    }

    private SearchPhdIndividualProgramProcessBean getOrCreateSearchBean(HttpServletRequest request) {
	SearchPhdIndividualProgramProcessBean searchBean = (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");
	if (searchBean == null) {
	    searchBean = initializeSearchBean(request);
	}
	return searchBean;
    }

    private ActionForward forwardToManageProcesses(ActionMapping mapping, HttpServletRequest request,
	    SearchPhdIndividualProgramProcessBean searchBean) {
	request.setAttribute("searchProcessBean", searchBean);
	request.setAttribute("candidacyCategory", Arrays.asList(CANDIDACY_CATEGORY));
	request.setAttribute("seminarCategory", Arrays.asList(SEMINAR_CATEGORY));
	request.setAttribute("thesisCategory", Arrays.asList(THESIS_CATEGORY));
	request.setAttribute("concludedThisYearContainer", PhdInactivePredicateContainer.CONCLUDED_THIS_YEAR);
	return mapping.findForward("manageProcesses");
    }

    public ActionForward searchAllProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
	RenderUtils.invalidateViewState();

	List<PhdIndividualProgramProcess> processes;
	try {
	    processes = PhdIndividualProgramProcess.search(searchBean.getPredicates());
	} catch (NumberFormatException ex) {
	    addActionMessage("error", request, "error.invalidFormat");
	    return forwardToManageProcesses(mapping, request, searchBean);
	}
	if (processes.isEmpty()) {
	    addActionMessage("results", request, "message.noResults");
	    return forwardToManageProcesses(mapping, request, searchBean);
	}
	if (processes.size() == 1) {
	    request.setAttribute("process", processes.get(0));
	    return mapping.findForward("viewProcess");
	}
	request.setAttribute("searchProcessBean", searchBean);
	request.setAttribute("processes", processes);
	return mapping.findForward("searchResults");
    }

    public ActionForward viewInactiveProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
	RenderUtils.invalidateViewState();

	request.setAttribute("searchProcessBean", searchBean);
	request.setAttribute("suspendedContainer", PhdInactivePredicateContainer.SUSPENDED);
	request.setAttribute("concludedContainer", PhdInactivePredicateContainer.CONCLUDED);
	request.setAttribute("abolishedContainer", PhdInactivePredicateContainer.ABOLISHED);
	return mapping.findForward("viewInactiveProcesses");
    }
}
