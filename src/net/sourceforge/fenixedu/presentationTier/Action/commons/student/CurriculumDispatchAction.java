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
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.EnrollmentStateSelectionType;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 * @author André Fernandes / João Brito
 */

public class CurriculumDispatchAction extends FenixDispatchAction {

    private final static ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	Registration registration = null;
	
	final Integer registrationOID = getRegistrationOID(request);
	final Integer studentNumber = getStudentNumber(request);
	final Student loggedStudent = getUserView(request).getPerson().getStudent();
	
	if (registrationOID != null) {
	    registration = rootDomainObject.readRegistrationByOID(registrationOID);
	} else if (studentNumber != null) {
	    registration = Registration.readByNumber(studentNumber);
	} else if (loggedStudent != null) {
	    if (loggedStudent.getRegistrations().size() == 1) {
		registration = loggedStudent.getRegistrations().get(0);
	    } else {
		request.setAttribute("student", loggedStudent);
		return mapping.findForward("chooseRegistration");
	    }
	}

	if (registration == null || (loggedStudent != null && !loggedStudent.hasRegistrations(registration))) {
	    return mapping.findForward("NotAuthorized");
	} else {
	    return getStudentCP(registration, mapping, request);
	}
    }

    private Integer getRegistrationOID(HttpServletRequest request) {
	String registrationOID = request.getParameter("registrationOID");
	if (registrationOID == null || !StringUtils.isNumeric(registrationOID)) {
	    registrationOID = (String) request.getAttribute("registrationOID");
	}
	
	return (registrationOID == null || !StringUtils.isNumeric(registrationOID)) ? null : Integer.valueOf(registrationOID);
    }

    private Integer getStudentNumber(HttpServletRequest request) {
	String studentNumber = request.getParameter("studentNumber");
	if (studentNumber == null || !StringUtils.isNumeric(studentNumber)) {
	    studentNumber = (String) request.getAttribute("studentNumber");
	}

	request.setAttribute("studentNumber", studentNumber);
	return (studentNumber == null || !StringUtils.isNumeric(studentNumber)) ? null : Integer.valueOf(studentNumber);
    }

    private ActionForward getStudentCP(final Registration registration, final ActionMapping mapping, final HttpServletRequest request) {
	request.setAttribute("registration", registration);
	
	request.setAttribute("selectedStudentCurricularPlans", getSelectedStudentCurricularPlans(registration, request));
	request.setAttribute("scpsLabelValueBeanList", getSCPsLabelValueBeanList(registration.getStudentCurricularPlans()));
        request.setAttribute("enrollmentOptions", EnrollmentStateSelectionType.getLabelValueBeanList());
        request.setAttribute("enrolmentStateSelectionType", request.getParameter("select") == null ? EnrollmentStateSelectionType.ALL_TYPE : Integer.valueOf(request.getParameter("select")));
	request.setAttribute("organizedBy", request.getParameter("organizedBy") == null ? "executionYears" : request.getParameter("organizedBy"));

	if (request.getParameter("degreeCurricularPlanID") == null || Integer.valueOf(request.getParameter("degreeCurricularPlanID")) == 0) {
	    return mapping.findForward("ShowStudentCurriculum");
	} else {
	    request.setAttribute("degreeCurricularPlanID", Integer.valueOf(request.getParameter("degreeCurricularPlanID")));
	    return mapping.findForward("ShowStudentCurriculumForCoordinator");
	}
    }

    private List<StudentCurricularPlan> getSelectedStudentCurricularPlans(final Registration registration, final HttpServletRequest request) {
	final List<StudentCurricularPlan> selectedStudentCurricularPlans;
	final String studentCPID = getStudentCPID(request);
	final StudentCurricularPlanIDDomainType scpIdType = studentCPID == null ? StudentCurricularPlanIDDomainType.NEWEST : new StudentCurricularPlanIDDomainType(studentCPID);
	if (scpIdType.isNewest()) {
	    selectedStudentCurricularPlans = Collections.singletonList(registration.getLastStudentCurricularPlan());
	} else if (scpIdType.isAll()) {
	    selectedStudentCurricularPlans = getSortedStudentCurricularPlans(registration);
	} else {
	    selectedStudentCurricularPlans = Collections.singletonList(getStudentCurricularPlan(registration, Integer.valueOf(studentCPID)));
	}
	return selectedStudentCurricularPlans;
    }

    private String getStudentCPID(HttpServletRequest request) {
	String result = request.getParameter("studentCPID");
	if (result == null) {
	    result = (String) request.getAttribute("studentCPID");
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

    private List<LabelValueBean> getSCPsLabelValueBeanList(
	    List<StudentCurricularPlan> studentCurricularPlans) {
	final List<LabelValueBean> result = new ArrayList<LabelValueBean>();

	result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.NEWEST_STRING,
		StudentCurricularPlanIDDomainType.NEWEST.toString()));
	result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.ALL_STRING,
		StudentCurricularPlanIDDomainType.ALL.toString()));

	for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
	    final StringBuilder label = new StringBuilder();

	    label.append(studentCurricularPlan.getDegreeType().getLocalizedName());
	    label.append(" ").append(applicationResources.getString("label.in"));
	    label.append(" ").append(studentCurricularPlan.getDegree().getNome());
	    label.append(", ").append(studentCurricularPlan.getDegreeCurricularPlan().getName());

	    if (studentCurricularPlan.getSpecialization() != null) {
		label.append(" - ")
			.append(
				enumerationResources.getString(studentCurricularPlan.getSpecialization()
					.name()));
	    }

	    label.append(" - ").append(
		    DateFormatUtil.format("dd.MM.yyyy", studentCurricularPlan.getStartDate()));

	    result.add(new LabelValueBean(label.toString(), String.valueOf(studentCurricularPlan
		    .getIdInternal())));
	}

	return result;
    }
    
    public ActionForward getCurriculumForCoordinator(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws ExistingActionException, FenixFilterException, FenixServiceException {

	// get and set the degreeCurricularPlanID from the request and onto the
	// request
	Integer degreeCurricularPlanID = null;
	if (request.getParameter("degreeCurricularPlanID") != null) {
	    degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	}

	IUserView userView = getUserView(request);

	String studentCurricularPlanID = request.getParameter("studentCPID");
	if (studentCurricularPlanID == null) {
	    studentCurricularPlanID = (String) request.getAttribute("studentCPID");
	}

	Integer executionDegreeID = getExecutionDegree(request);
	List result = null;
	try {
	    Object args[] = { executionDegreeID, Integer.valueOf(studentCurricularPlanID) };
	    result = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
		    "ReadStudentCurriculum", args);
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
	    infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
		    .executeService(userView, "ReadStudentCurricularPlan", args);
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
