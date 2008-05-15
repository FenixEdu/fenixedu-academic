package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.AddAttendsBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationCurriculumBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person.PersonBeanFactoryEditor;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentDA extends FenixDispatchAction {

    private Student getStudent(final HttpServletRequest request) {
	final String studentID = request.getParameter("studentID");
	final Student student = rootDomainObject.readStudentByOID(Integer.valueOf(studentID));
	request.setAttribute("student", student);
	return student;
    }

    private Registration getRegistration(final HttpServletRequest request) {
	final Integer registrationID = getIntegerFromRequest(request, "registrationID") != null ? getIntegerFromRequest(request,
		"registrationID") : getIntegerFromRequest(request, "registrationId");
	final Registration registration = rootDomainObject.readRegistrationByOID(Integer.valueOf(registrationID));
	request.setAttribute("registration", registration);
	return registration;
    }

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final Student student = getStudent(request);

	final Employee employee = student.getPerson().getEmployee();
	if (employee != null && employee.getCurrentWorkingContract() != null) {
	    addActionMessage(request, "message.student.personIsEmployee");
	    return mapping.findForward("viewStudentDetails");
	}

	request.setAttribute("personBean", new PersonBeanFactoryEditor(student.getPerson()));
	return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	getStudent(request);
	executeFactoryMethod(request);
	RenderUtils.invalidateViewState();
	addActionMessage(request, "message.student.personDataEditedWithSuccess");
	return mapping.findForward("viewStudentDetails");
    }

    public ActionForward viewPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("personBean", new PersonBeanFactoryEditor(getStudent(request).getPerson()));
	return mapping.findForward("viewPersonalData");
    }

    public ActionForward visualizeRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	getRegistration(request);
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward visualizeStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	getStudent(request);
	return mapping.findForward("viewStudentDetails");
    }

    public ActionForward prepareViewRegistrationCurriculum(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	RenderUtils.invalidateViewState();

	final Registration registration = getRegistration(request);
	final RegistrationCurriculumBean registrationCurriculumBean = new RegistrationCurriculumBean(registration);
	request.setAttribute("registrationCurriculumBean", registrationCurriculumBean);

	final Integer degreeCurricularPlanID = getIntegerFromRequest(request, "degreeCurricularPlanID");
	if (degreeCurricularPlanID != null) {
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	}

	if (!registrationCurriculumBean.hasCycleCurriculumGroup()) {
	    final List<CycleCurriculumGroup> internalCycleCurriculumGrops = registration.getLastStudentCurricularPlan()
		    .getInternalCycleCurriculumGrops();
	    if (internalCycleCurriculumGrops.size() > 1) {
		return mapping.findForward("chooseCycleForViewRegistrationCurriculum");
	    }
	}

	return mapping.findForward("view-registration-curriculum");
    }

    public ActionForward prepareViewRegistrationCurriculumInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("registrationCurriculumBean", getRegistrationCurriculumBeanFromViewState());

	final Integer degreeCurricularPlanID = getIntegerFromRequest(request, "degreeCurricularPlanID");
	if (degreeCurricularPlanID != null) {
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	}

	return mapping.findForward("chooseCycleForViewRegistrationCurriculum");
    }

    private RegistrationCurriculumBean getRegistrationCurriculumBeanFromViewState() {
	return (RegistrationCurriculumBean) getObjectFromViewState("registrationCurriculumBean");
    }

    public ActionForward chooseCycleForViewRegistrationCurriculum(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final RegistrationCurriculumBean registrationCurriculumBean = getRegistrationCurriculumBeanFromViewState();
	request.setAttribute("registrationCurriculumBean", registrationCurriculumBean);
	request.setAttribute("registration", registrationCurriculumBean.getRegistration());

	request.setAttribute("degreeCurricularPlanID", registrationCurriculumBean.getCycleCurriculumGroup()
		.getStudentCurricularPlan().getDegreeCurricularPlan().getIdInternal());

	return mapping.findForward("view-registration-curriculum");
    }

    public ActionForward viewRegistrationCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final RegistrationCurriculumBean registrationCurriculumBean = getRegistrationCurriculumBeanFromViewState();

	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	if (registrationCurriculumBean.getExecutionYear() == null
		&& registrationCurriculumBean.getRegistration().hasAnyEnrolmentsIn(currentExecutionYear)) {
	    registrationCurriculumBean.setExecutionYear(currentExecutionYear);
	}

	final Integer degreeCurricularPlanID = getIntegerFromRequest(request, "degreeCurricularPlanID");
	if (degreeCurricularPlanID != null) {
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	}

	request.setAttribute("registrationCurriculumBean", registrationCurriculumBean);
	return mapping.findForward("view-registration-curriculum");
    }

    public ActionForward prepareRegistrationConclusionProcess(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	RenderUtils.invalidateViewState();

	final Registration registration = getRegistration(request);

	if (registration.isBolonha()) {
	    if (registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops().size() > 1) {
		request.setAttribute("registrationConclusionBean", new RegistrationConclusionBean(registration));
		return mapping.findForward("chooseCycleForRegistrationConclusion");
	    } else if (registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops().size() == 1) {
		request.setAttribute("registrationConclusionBean", new RegistrationConclusionBean(registration, registration
			.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops().iterator().next()));
		return mapping.findForward("registrationConclusion");
	    } else {
		return mapping.findForward("chooseCycleForRegistrationConclusion");
	    }
	}

	request.setAttribute("registrationConclusionBean", new RegistrationConclusionBean(registration));
	return mapping.findForward("registrationConclusion");
    }

    public ActionForward prepareRegistrationConclusionProcessInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("registrationConclusionBean", getRegistrationConclusionBeanFromViewState());

	return mapping.findForward("chooseCycleForRegistrationConclusion");

    }

    private RegistrationConclusionBean getRegistrationConclusionBeanFromViewState() {
	return (RegistrationConclusionBean) getObjectFromViewState("registrationConclusionBean");
    }

    public ActionForward doRegistrationConclusion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final RegistrationConclusionBean registrationConclusionBean = getRegistrationConclusionBeanFromViewState();

	try {
	    executeService("RegistrationConclusionProcess", registrationConclusionBean);
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("registrationConclusionBean", registrationConclusionBean);

	    return mapping.findForward("registrationConclusion");
	}

	request.setAttribute("registrationId", registrationConclusionBean.getRegistration().getIdInternal());
	return visualizeRegistration(mapping, form, request, response);

    }

    public ActionForward chooseCycleCurriculumGroupForConclusion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final RegistrationConclusionBean registrationConclusionBean = getRegistrationConclusionBeanFromViewState();
	request.setAttribute("registrationConclusionBean", registrationConclusionBean);
	request.setAttribute("registration", registrationConclusionBean.getRegistration());

	return mapping.findForward("registrationConclusion");
    }

    public ActionForward prepareRegistrationConclusionDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	RenderUtils.invalidateViewState();

	final Registration registration = getRegistration(request);
	request.setAttribute("registration", registration);

	final Integer cycleCurriculumGroupId = getIntegerFromRequest(request, "cycleCurriculumGroupId");
	final CycleCurriculumGroup cycleCurriculumGroup = (CycleCurriculumGroup) rootDomainObject
		.readCurriculumModuleByOID(cycleCurriculumGroupId);
	final RegistrationConclusionBean registrationConclusionBean;
	if (cycleCurriculumGroupId == null) {
	    registrationConclusionBean = new RegistrationConclusionBean(registration);
	} else {
	    registrationConclusionBean = new RegistrationConclusionBean(registration, cycleCurriculumGroup);
	}
	request.setAttribute("registrationConclusionBean", registrationConclusionBean);

	return mapping.findForward("registrationConclusionDocument");
    }

    public ActionForward viewAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RenderUtils.invalidateViewState();

	final Registration registration = getRegistration(request);
	request.setAttribute("registration", registration);

	if (registration != null) {
	    final SortedMap<ExecutionSemester, SortedSet<Attends>> attendsMap = new TreeMap<ExecutionSemester, SortedSet<Attends>>();
	    for (final Attends attends : registration.getAssociatedAttendsSet()) {
		final ExecutionSemester executionSemester = attends.getExecutionPeriod();
		SortedSet<Attends> attendsSet = attendsMap.get(executionSemester);
		if (attendsSet == null) {
		    attendsSet = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR);
		    attendsMap.put(executionSemester, attendsSet);
		}
		attendsSet.add(attends);
	    }
	    request.setAttribute("attendsMap", attendsMap);
	}

	return mapping.findForward("viewAttends");
    }

    public ActionForward prepareAddAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final Registration registration = getRegistration(request);
	request.setAttribute("registration", registration);

	AddAttendsBean addAttendsBean = (AddAttendsBean) getObjectFromViewState("addAttendsBean");
	if (addAttendsBean == null) {
	    addAttendsBean = new AddAttendsBean();
	    final ExecutionSemester executionPeriod = ExecutionSemester.readActualExecutionSemester();
	    final ExecutionYear executionYear = executionPeriod.getExecutionYear();
	    final Degree degree = registration.getDegree();
	    final ExecutionDegree executionDegree = getExecutionDegree(executionYear, degree);

	    addAttendsBean.setExecutionPeriod(executionPeriod);
	    addAttendsBean.setExecutionYear(executionYear);
	    addAttendsBean.setExecutionDegree(executionDegree);
	}
	request.setAttribute("addAttendsBean", addAttendsBean);

	RenderUtils.invalidateViewState();

	return mapping.findForward("addAttends");
    }

    protected ExecutionDegree getExecutionDegree(final ExecutionYear executionYear, final Degree degree) {
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionDegree.getExecutionYear() == executionYear) {
		    return executionDegree;
		}
	    }
	}
	return null;
    }

    public ActionForward addAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Registration registration = getRegistration(request);
	request.setAttribute("registration", registration);

	final AddAttendsBean addAttendsBean = (AddAttendsBean) getObjectFromViewState("addAttendsBean");
	final ExecutionCourse executionCourse = addAttendsBean.getExecutionCourse();

	executeService("WriteStudentAttendingCourse", registration, executionCourse.getIdInternal());

	return viewAttends(mapping, actionForm, request, response);
    }

    public ActionForward deleteAttends(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Registration registration = getRegistration(request);
	request.setAttribute("registration", registration);

	final String attendsIdString = request.getParameter("attendsId");
	final Integer attendsId = attendsIdString != null && attendsIdString.length() > 0 ? Integer.valueOf(attendsIdString)
		: null;
	final Attends attends = attendsId == null ? null : rootDomainObject.readAttendsByOID(attendsId);

	executeService("DeleteStudentAttendingCourse", registration, attends.getExecutionCourse().getIdInternal());

	return viewAttends(mapping, actionForm, request, response);
    }

}
