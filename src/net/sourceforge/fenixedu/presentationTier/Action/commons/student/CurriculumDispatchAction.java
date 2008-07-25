package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.EnrolmentStateFilterType;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.ViewType;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 * @author André Fernandes / João Brito
 */

public class CurriculumDispatchAction extends FenixDispatchAction {

    private final static ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources",
	    Language.getLocale());

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	RenderUtils.invalidateViewState();

	Registration registration = null;

	final Integer registrationOID = getRegistrationOID(request);
	final Student loggedStudent = getUserView(request).getPerson().getStudent();

	if (registrationOID != null) {
	    registration = rootDomainObject.readRegistrationByOID(registrationOID);
	} else if (loggedStudent != null) {
	    if (loggedStudent.getRegistrations().size() == 1) {
		registration = loggedStudent.getRegistrations().get(0);
	    } else {
		request.setAttribute("student", loggedStudent);
		return mapping.findForward("chooseRegistration");
	    }
	}

	if (registration == null) {
	    return mapping.findForward("NotAuthorized");
	} else {
	    return getStudentCP(registration, mapping, (DynaActionForm) form, request);
	}
    }

    private Integer getRegistrationOID(HttpServletRequest request) {
	String registrationOID = request.getParameter("registrationOID");
	if (registrationOID == null || !StringUtils.isNumeric(registrationOID)) {
	    registrationOID = (String) request.getAttribute("registrationOID");
	}

	return (registrationOID == null || registrationOID.equals("") || !StringUtils.isNumeric(registrationOID)) ? null
		: Integer.valueOf(registrationOID);
    }

    public ActionForward prepareReadByStudentNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	RenderUtils.invalidateViewState();

	DynaActionForm actionForm = (DynaActionForm) form;
	Registration registration = null;

	final Integer degreeCurricularPlanId = (Integer) actionForm.get("degreeCurricularPlanID");
	if (degreeCurricularPlanId != null && degreeCurricularPlanId > 0) {
	    DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
	    registration = getStudent(actionForm).readRegistrationByDegreeCurricularPlan(degreeCurricularPlan);
	} else {
	    final List<Registration> registrations = getStudent(actionForm).getRegistrations();
	    if (!registrations.isEmpty()) {
		registration = registrations.iterator().next();
	    }
	}

	if (registration == null) {
	    return mapping.findForward("NotAuthorized");
	} else {
	    return getStudentCP(registration, mapping, actionForm, request);
	}

    }

    private Student getStudent(DynaActionForm form) {
	final Integer studentNumber = Integer.valueOf((String) form.get("studentNumber"));
	return Student.readStudentByNumber(studentNumber);
    }

    private ActionForward getStudentCP(final Registration registration, final ActionMapping mapping, DynaActionForm actionForm,
	    final HttpServletRequest request) {
	request.setAttribute("registration", registration);

	String studentCPID = getStudentCPID(request, actionForm);
	if (StringUtils.isEmpty(studentCPID)) {
	    studentCPID = getDefaultStudentCPID(registration).getId().toString();
	    actionForm.set("studentCPID", studentCPID);
	}
	request.setAttribute("selectedStudentCurricularPlans", getSelectedStudentCurricularPlans(registration, studentCPID));
	request.setAttribute("scpsLabelValueBeanList", getSCPsLabelValueBeanList(registration.getStudentCurricularPlans()));

	if (StringUtils.isEmpty(actionForm.getString("viewType"))) {
	    actionForm.set("viewType", ViewType.ALL.name());
	}

	if (StringUtils.isEmpty(actionForm.getString("select"))) {
	    actionForm.set("select", EnrolmentStateFilterType.APPROVED_OR_ENROLED.name());
	}

	if (StringUtils.isEmpty(actionForm.getString("organizedBy"))) {
	    String organizedBy = registration.getDegreeType() == DegreeType.MASTER_DEGREE ? OrganizationType.EXECUTION_YEARS
		    .name() : OrganizationType.GROUPS.name();
	    actionForm.set("organizedBy", organizedBy);
	}

	if (request.getParameter("degreeCurricularPlanID") == null
		|| Integer.valueOf(request.getParameter("degreeCurricularPlanID")) == 0) {
	    return mapping.findForward("ShowStudentCurriculum");
	} else {
	    request.setAttribute("degreeCurricularPlanID", Integer.valueOf(request.getParameter("degreeCurricularPlanID")));
	    return mapping.findForward("ShowStudentCurriculumForCoordinator");
	}
    }

    private List<StudentCurricularPlan> getSelectedStudentCurricularPlans(final Registration registration,
	    final String studentCPID) {
	final List<StudentCurricularPlan> result;

	final StudentCurricularPlanIDDomainType scpIdType = new StudentCurricularPlanIDDomainType(studentCPID);
	if (scpIdType.isNewest()) {
	    result = Collections.singletonList(registration.getLastStudentCurricularPlan());
	} else if (scpIdType.isAll()) {
	    result = getSortedStudentCurricularPlans(registration);
	} else {
	    result = Collections.singletonList(getStudentCurricularPlan(registration, Integer.valueOf(studentCPID)));
	}

	return result;
    }

    private StudentCurricularPlanIDDomainType getDefaultStudentCPID(final Registration registration) {
	return registration.isBolonha() ? StudentCurricularPlanIDDomainType.NEWEST : StudentCurricularPlanIDDomainType.ALL;
    }

    private String getStudentCPID(HttpServletRequest request, DynaActionForm actionForm) {
	String result = request.getParameter("studentCPID");
	if (result == null) {
	    result = (String) request.getAttribute("studentCPID");
	} else if (result == null) {
	    result = (String) actionForm.get("studentCPID");
	}

	return result;
    }

    private List<StudentCurricularPlan> getSortedStudentCurricularPlans(final Registration registration) {
	final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	result.addAll(registration.getStudentCurricularPlans());
	Collections.sort(result, new BeanComparator("startDateYearMonthDay"));

	return result;
    }

    private StudentCurricularPlan getStudentCurricularPlan(final Registration registration, final Integer scpId) {
	for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.getIdInternal().equals(scpId)) {
		return studentCurricularPlan;
	    }
	}

	return null;
    }

    private List<LabelValueBean> getSCPsLabelValueBeanList(List<StudentCurricularPlan> studentCurricularPlans) {
	final List<LabelValueBean> result = new ArrayList<LabelValueBean>();

	result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.NEWEST_STRING, StudentCurricularPlanIDDomainType.NEWEST
		.toString()));
	result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.ALL_STRING, StudentCurricularPlanIDDomainType.ALL
		.toString()));

	for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
	    final StringBuilder label = new StringBuilder();

	    label.append(studentCurricularPlan.getDegreeType().getLocalizedName());
	    label.append(" ").append(applicationResources.getString("label.in"));
	    label.append(" ").append(studentCurricularPlan.getDegree().getNome());
	    label.append(", ").append(studentCurricularPlan.getDegreeCurricularPlan().getName());

	    if (studentCurricularPlan.getSpecialization() != null) {
		label.append(" - ").append(enumerationResources.getString(studentCurricularPlan.getSpecialization().name()));
	    }

	    label.append(" - ").append(DateFormatUtil.format("dd.MM.yyyy", studentCurricularPlan.getStartDate()));

	    result.add(new LabelValueBean(label.toString(), String.valueOf(studentCurricularPlan.getIdInternal())));
	}

	return result;
    }

    public ActionForward getCurriculumForCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ExistingActionException, FenixFilterException, FenixServiceException {

	// get and set the degreeCurricularPlanID from the request and onto the
	// request
	Integer degreeCurricularPlanID = null;
	if (request.getParameter("degreeCurricularPlanID") != null) {
	    degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	}

	IUserView userView = getUserView(request);

	final String studentCurricularPlanID = getStudentCPID(request, (DynaActionForm) form);

	Integer executionDegreeID = getExecutionDegree(request);
	List result = null;
	try {
	    Object args[] = { executionDegreeID, Integer.valueOf(studentCurricularPlanID) };
	    result = (ArrayList) ServiceManagerServiceFactory.executeService("ReadStudentCurriculum", args);
	} catch (NotAuthorizedException e) {
	    return mapping.findForward("NotAuthorized");
	}
	// TODO Remove this exception! It returns null and it is not supposed!
	catch (Exception exp) {
	    exp.printStackTrace();
	    return null;
	}

	BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
	BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
	ComparatorChain chainComparator = new ComparatorChain();
	chainComparator.addComparator(courseName);
	chainComparator.addComparator(executionYear);

	Collections.sort(result, chainComparator);

	InfoStudentCurricularPlan infoStudentCurricularPlan = null;
	try {
	    Object args[] = { Integer.valueOf(studentCurricularPlanID) };
	    infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
		    "ReadStudentCurricularPlan", args);
	} catch (ExistingServiceException e) {
	    throw new ExistingActionException(e);
	}

	request.setAttribute(SessionConstants.CURRICULUM, result);
	request.setAttribute(SessionConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

	return mapping.findForward("ShowStudentCurriculum");
    }

    private Integer getExecutionDegree(HttpServletRequest request) {
	Integer executionDegreeId = null;

	String executionDegreeIdString = request.getParameter("executionDegreeId");
	if (executionDegreeIdString == null) {
	    executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
	}
	if (executionDegreeIdString != null) {
	    executionDegreeId = Integer.valueOf(executionDegreeIdString);
	}
	request.setAttribute("executionDegreeId", executionDegreeId);

	return executionDegreeId;
    }

}
