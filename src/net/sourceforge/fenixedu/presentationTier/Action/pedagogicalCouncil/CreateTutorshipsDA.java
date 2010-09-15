package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor.TutorManagementDispatchAction;
import net.sourceforge.fenixedu.util.Month;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * Class CreateTutorshipsDA.java
 * 
 * @author jaime created on Aug 3, 2010
 */

@Mapping(path = "/createTutorships", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "prepareCreate", path = "/pedagogicalCouncil/tutorship/createTutorships.jsp") })
public class CreateTutorshipsDA extends TutorManagementDispatchAction {

    private static int TUTORSHIP_DURATION = 2;

    public ActionForward prepareCreation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("tutorateBean", new ContextTutorshipCreationBean());

	return mapping.findForward("prepareCreate");
    }

    public ActionForward prepareViewCreateTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ContextTutorshipCreationBean bean = (ContextTutorshipCreationBean) getRenderedObject("tutorateBean");
	request.setAttribute("tutorateBean", bean);
	if (bean.getSchoolClass() != null && bean.getExecutionDegree() != null && bean.getExecutionSemester() != null) {
	    // get all students from ExecCourse
	    List<Person> students = new ArrayList<Person>();
	    for (Shift shift : bean.getSchoolClass().getAssociatedShifts()) {
		if (shift.containsType(ShiftType.TEORICA)
			&& shift.getExecutionPeriod().compareTo(bean.getExecutionSemester()) == 0) {
		    for (Registration registration : shift.getStudents()) {
			Student student = registration.getStudent();
			if (validForListing(registration, bean.getExecutionDegree())) {
			    students.add(student.getPerson());
			}

		    }
		}
	    }
	    RenderUtils.invalidateViewState();
	    request.setAttribute("students", students);
	    request.setAttribute("tutorBean", new TeacherTutorshipCreationBean(bean.getExecutionDegree(), bean
		    .getExecutionSemester()));
	    return mapping.findForward("prepareCreate");
	} else {
	    RenderUtils.invalidateViewState();
	    return mapping.findForward("prepareCreate");
	}
    }

    /**
     * Iterate on all shifts of a registration and construct a set of the
     * correspondent SchoolClasses
     * 
     * @param registration
     * @param shift
     * @return
     */
    public Set<SchoolClass> findSchoolClassesSetForStudent(Registration registration) {
	Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
	for (Shift shift : registration.getShifts()) {
	    schoolClasses.addAll(shift.getAssociatedClassesSet());
	}
	return schoolClasses;
    }

    public boolean validForListing(Registration registration, ExecutionDegree executionDegree) {
	Student student = registration.getStudent();
	// Only selects people which have registrations in
	// the
	// choosen
	// Degree and no Tutor assigned

	if (student.hasActiveRegistrationFor(executionDegree.getDegree())) {
	    if (registration.getActiveTutorship() == null) {
		return true;
	    }
	}
	return false;
    }

    public ActionForward prepareStudentsAndTeachers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ContextTutorshipCreationBean bean = (ContextTutorshipCreationBean) getRenderedObject("tutorateBean");
	request.setAttribute("tutorateBean", bean);
	return mapping.findForward("prepareCreate");
    }

    public ActionForward createTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Boolean errorEncountered = false;
	String[] selectedPersons = request.getParameterValues("selectedPersons");
	TeacherTutorshipCreationBean tutorBean = (TeacherTutorshipCreationBean) getRenderedObject("tutorBean");
	ContextTutorshipCreationBean contextBean = (ContextTutorshipCreationBean) getRenderedObject("tutorateBean");

	StudentsByEntryYearBean selectedStudentsAndTutorBean = new StudentsByEntryYearBean(contextBean.getExecutionSemester()
		.getExecutionYear());
	// Initialize Tutorship creation bean to use in InsertTutorship Service
	initializeBean(selectedStudentsAndTutorBean, tutorBean, contextBean, selectedPersons);

	Object[] args = new Object[] { contextBean.getExecutionDegree().getIdInternal(), selectedStudentsAndTutorBean };

	List<TutorshipErrorBean> tutorshipsNotInserted = new ArrayList<TutorshipErrorBean>();
	try {
	    tutorshipsNotInserted = (List<TutorshipErrorBean>) executeService("InsertTutorship", args);
	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    errorEncountered = true;
	}
	if (!tutorshipsNotInserted.isEmpty()) {
	    errorEncountered = true;
	    for (TutorshipErrorBean tutorship : tutorshipsNotInserted) {
		addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
	    }
	    if (tutorshipsNotInserted.size() < selectedPersons.length) {
		Integer argument = selectedPersons.length - tutorshipsNotInserted.size();
		String[] messageArgs = { argument.toString() };
		addActionMessage(request, "label.create.tutorship.remaining.correct", messageArgs);
	    }
	    return mapping.findForward("prepareCreate");
	} else if (!errorEncountered) {
	    request.setAttribute("success", "Sucess");
	    return prepareCreation(mapping, actionForm, request, response);
	}
	return prepareCreation(mapping, actionForm, request, response);
    }

    /**
     * Initializes Bean to insert in InsertTutorship service
     * 
     * @param studentsByEntryYearBean
     * @param tutorBean
     * @param contextBean
     * @param selectedPersons
     * @return list of students (Person) not registered in course
     */
    public void initializeBean(StudentsByEntryYearBean studentsByEntryYearBean, TeacherTutorshipCreationBean tutorBean,
	    ContextTutorshipCreationBean contextBean, String[] selectedPersons) {
	ExecutionSemester executionSemester = contextBean.getExecutionSemester();
	ExecutionDegree executionDegree = contextBean.getExecutionDegree();
	studentsByEntryYearBean.receiveStudentsToCreateTutorshipList(selectedPersons, executionSemester, executionDegree);
	studentsByEntryYearBean.setTeacher(tutorBean.getTeacher().getTeacher());
	DateTime today = new DateTime();
	studentsByEntryYearBean.setTutorshipEndMonth(Month.fromDateTime(today));
	studentsByEntryYearBean.setTutorshipEndYear(today.getYear() + TUTORSHIP_DURATION);

    }

}
