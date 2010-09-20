package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AcceptEnrolments;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.RejectEnrolments;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdCandidacyPredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdInactivePredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdSeminarPredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdThesisPredicateContainer;

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

@Forward(name = "viewAlertMessageArchive", path = "/phd/coordinator/viewAlertMessageArchive.jsp"),

@Forward(name = "viewAlertMessage", path = "/phd/coordinator/viewAlertMessage.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/coordinator/viewProcessAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessageArchive", path = "/phd/coordinator/viewProcessAlertMessageArchive.jsp"),

@Forward(name = "viewCurriculum", path = "/phd/coordinator/viewCurriculum.jsp"),

@Forward(name = "manageEnrolments", path = "/phd/coordinator/enrolments/manageEnrolments.jsp"),

@Forward(name = "validateEnrolments", path = "/phd/coordinator/enrolments/validateEnrolments.jsp")

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
    protected PhdInactivePredicateContainer getConcludedContainer() {
	return PhdInactivePredicateContainer.CONCLUDED_THIS_YEAR;
    }

    @Override
    protected List<PredicateContainer<?>> getThesisCategory() {
	return Arrays.asList(THESIS_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getSeminarCategory() {
	return Arrays.asList(SEMINAR_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getCandidacyCategory() {
	return Arrays.asList(CANDIDACY_CATEGORY);
    }

    // Manage enrolments

    public ActionForward manageEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	ManageEnrolmentsBean bean = (ManageEnrolmentsBean) getRenderedObject("manageEnrolmentsBean");

	if (bean == null) {
	    bean = new ManageEnrolmentsBean();
	    bean.setProcess(process);
	    bean.setSemester(ExecutionSemester.readActualExecutionSemester());
	}

	filterEnrolments(bean, process, false);

	request.setAttribute("manageEnrolmentsBean", bean);
	return mapping.findForward("manageEnrolments");
    }

    private void filterEnrolments(ManageEnrolmentsBean bean, PhdIndividualProgramProcess process, boolean filterByTemporary) {
	final StudentCurricularPlan scp = process.getRegistration().getLastStudentCurricularPlan();

	final Collection<Enrolment> enrolmentsPerformedByStudent = new HashSet<Enrolment>();
	final Collection<Enrolment> enrolmentsPerformedByAdminOffice = new HashSet<Enrolment>();

	for (final Enrolment enrolment : scp.getEnrolmentsByExecutionPeriod(bean.getSemester())) {

	    if (filterByTemporary && !enrolment.isTemporary()) {
		continue;
	    }

	    if (isPerformedByStudent(enrolment)) {
		enrolmentsPerformedByStudent.add(enrolment);
	    } else {
		enrolmentsPerformedByAdminOffice.add(enrolment);
	    }
	}

	bean.setEnrolmentsPerformedByStudent(enrolmentsPerformedByStudent);
	bean.setRemainingEnrolments(enrolmentsPerformedByAdminOffice);
    }

    private boolean isPerformedByStudent(Enrolment enrolment) {
	final Person person = Person.readPersonByUsername(enrolment.getCreatedBy());
	return person.hasRole(RoleType.STUDENT) && enrolment.getStudent().equals(person.getStudent());
    }

    public ActionForward prepareValidateEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	ManageEnrolmentsBean bean = (ManageEnrolmentsBean) getRenderedObject("manageEnrolmentsBean");

	if (bean == null) {
	    bean = new ManageEnrolmentsBean();
	    bean.setProcess(process);
	    setExecutionSemester(request, bean);
	}

	filterEnrolments(bean, process, true);
	request.setAttribute("manageEnrolmentsBean", bean);

	setDefaultMailInformation(bean, getProcess(request));
	return mapping.findForward("validateEnrolments");
    }

    private void setExecutionSemester(HttpServletRequest request, ManageEnrolmentsBean bean) {
	final ExecutionSemester semester = getDomainObject(request, "executionSemesterId");
	if (semester != null) {
	    bean.setSemester(semester);
	} else {
	    bean.setSemester(ExecutionSemester.readActualExecutionSemester());
	}
    }

    private void setDefaultMailInformation(ManageEnrolmentsBean bean, PhdIndividualProgramProcess process) {
	bean.setMailSubject(AlertService.getSubjectPrefixed(process, "message.phd.enrolments.validation.default.subject"));
	bean.setMailBody(AlertService.getBodyText(process, "message.phd.enrolments.validation.default.body"));
    }

    public ActionForward acceptEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) getRenderedObject("manageEnrolmentsBean");
	request.setAttribute("manageEnrolmentsBean", bean);

	try {

	    ExecuteProcessActivity.run(getProcess(request), AcceptEnrolments.class, bean);

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return mapping.findForward("validateEnrolments");
	}

	RenderUtils.invalidateViewState();
	return redirect(String.format("/phdIndividualProgramProcess.do?method=manageEnrolments&processId=%s", getProcess(request)
		.getExternalId()), request);
    }

    public ActionForward rejectEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) getRenderedObject("manageEnrolmentsBean");
	request.setAttribute("manageEnrolmentsBean", bean);

	try {

	    ExecuteProcessActivity.run(getProcess(request), RejectEnrolments.class, bean);

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return mapping.findForward("validateEnrolments");
	}

	RenderUtils.invalidateViewState();

	return redirect(String.format("/phdIndividualProgramProcess.do?method=manageEnrolments&processId=%s", getProcess(request)
		.getExternalId()), request);
    }

    // end of manage enrolments
}
