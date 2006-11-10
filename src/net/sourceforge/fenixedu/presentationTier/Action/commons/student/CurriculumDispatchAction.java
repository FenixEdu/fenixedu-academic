package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
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

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getStudentCP(mapping, form, request, response);
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final Person person;
	final String studentNumber = getStudentNumber(request);
	if (studentNumber == null) {
	    person = getUserView(request).getPerson();
	} else {
	    request.setAttribute("studentNumber", studentNumber);
	    
	    final Registration registration = Registration.readByNumber(Integer.valueOf(studentNumber));
	    if (registration == null) {
		return mapping.findForward("NotAuthorized");
	    }

	    person = registration.getPerson();
	}

	final String studentCPID = request.getParameter("studentCPID") != null ? request.getParameter("studentCPID") : (String) request.getAttribute("studentCPID");
	final StudentCurricularPlanIDDomainType scpIdType = studentCPID == null ? StudentCurricularPlanIDDomainType.NEWEST : new StudentCurricularPlanIDDomainType(studentCPID);
	
	final List<StudentCurricularPlan> selectedStudentCurricularPlans;
	final List<StudentCurricularPlan> allStudentCurricularPlans = getAllStudentCurricularPlans(person);
	if (scpIdType.isNewest()) {
	    selectedStudentCurricularPlans = Collections.singletonList(getMostRecentStudentCurricularPlan(person));
	} else if (scpIdType.isAll()) {
	    selectedStudentCurricularPlans = allStudentCurricularPlans;
	} else {
	    selectedStudentCurricularPlans = Collections.singletonList(rootDomainObject.readStudentCurricularPlanByOID(scpIdType.getId()));
	}
	request.setAttribute("selectedStudentCurricularPlans", selectedStudentCurricularPlans);

	request.setAttribute("registration", selectedStudentCurricularPlans.iterator().next().getRegistration());
	request.setAttribute("scpsLabelValueBeanList", getSCPsLabelValueBeanList(allStudentCurricularPlans));
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

    private String getStudentNumber(HttpServletRequest request) {
	String studentNumber = request.getParameter("studentNumber");
	if (studentNumber == null || !StringUtils.isNumeric(studentNumber)) {
	    studentNumber = (String) request.getAttribute("studentNumber");
	}
	return studentNumber;
    }

    private List<StudentCurricularPlan> getAllStudentCurricularPlans(final Person person) {
	final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (final Registration registration : person.getStudentsSet()) {
	    result.addAll(registration.getStudentCurricularPlans());
	}
	
	Collections.sort(result, new BeanComparator("startDate"));
	
	return result;
    }

    private StudentCurricularPlan getMostRecentStudentCurricularPlan(final Person person) {
	StudentCurricularPlan mostRecentStudentCurricularPlan = null;
	for (final StudentCurricularPlan studentCurricularPlan : getAllStudentCurricularPlans(person)) {
	    if (mostRecentStudentCurricularPlan == null
		    || mostRecentStudentCurricularPlan.getStartDateYearMonthDay().isBefore(
			    studentCurricularPlan.getStartDateYearMonthDay())) {
		mostRecentStudentCurricularPlan = studentCurricularPlan;
	    }
	}
	return mostRecentStudentCurricularPlan;
    }

    public static List<LabelValueBean> getSCPsLabelValueBeanList(
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
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

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
