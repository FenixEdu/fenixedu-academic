package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor.DeleteTutorship;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewTutorship", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "viewTutorship", path = "/pedagogicalCouncil/tutorship/viewTutorship.jsp") })
public class ViewTutorshipDA extends FenixDispatchAction {

    private static int TUTORSHIP_DURATION = 2;

    public ActionForward prepareTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String success = (String) request.getAttribute("success");
	if (success != null) {
	    request.setAttribute("success", success);
	    return mapping.findForward("viewTutorship");
	}
	Integer tutorshipId = getIdInternal(request, "tutorshipId");
	Tutorship tutorship = RootDomainObject.getInstance().readTutorshipByOID(tutorshipId);

	ExecutionDegree executionDegree = getExecutionDegree(tutorship);
	final List<ExecutionSemester> executionSemesters = provideSemesters(tutorship);

	RenderUtils.invalidateViewState();
	request.setAttribute("periodBean", new TutorshipPeriodPartialBean(tutorship, executionDegree));
	return mapping.findForward("viewTutorship");
    }

    public ActionForward deleteTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	Integer tutorshipId = new Integer(request.getParameter("tutorshipID"));
	Tutorship tutorship = rootDomainObject.readTutorshipByOID(tutorshipId);

	ExecutionDegree executionDegree = getExecutionDegree(tutorship);
	deleteTutor(tutorship, executionDegree, request, mapping);
	RenderUtils.invalidateViewState();
	request.setAttribute("successDelete", "successDelete");
	return mapping.findForward("viewTutorship");
    }

    public ActionForward changeTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {


	// public List<TutorshipErrorBean> run(Integer executionDegreeID,
	// Integer tutorNumber, List<Tutorship> tutorsToDelete)
	// throws FenixServiceException {


	// TODO: What is tutor number?
	TutorshipPeriodPartialBean tutorshipPeriodPartialBean = (TutorshipPeriodPartialBean) getRenderedObject("periodBean");

	Tutorship tutorship = tutorshipPeriodPartialBean.getTutorship();

	ExecutionDegree executionDegree = getExecutionDegree(tutorship);

	if (tutorshipPeriodPartialBean.getTeacher() != null) {
	    Person studentPerson = tutorship.getStudent().getPerson();
	    Partial endDate = tutorship.getEndDate();
	    ExecutionYear executionYear = ExecutionYear.readByPartial(tutorship.getStartDate());
	    deleteTutor(tutorship, executionDegree, request, mapping);
	    createTutorship(request, mapping, actionForm, executionYear, response, executionDegree, studentPerson, endDate,
		    tutorshipPeriodPartialBean);
	} else if (tutorshipPeriodPartialBean.getEndDate() != null) {
	    changeDate(tutorship, executionDegree, tutorshipPeriodPartialBean, request, mapping);
	}

	return mapping.findForward("viewTutorship");
    }

    private void changeDate(Tutorship tutorship, ExecutionDegree executionDegree, TutorshipPeriodPartialBean tutorshipPeriodPartialBean , HttpServletRequest request,
	    ActionMapping mapping) {
	
	final List<ChangeTutorshipBean> changeTutorshipBeans = new ArrayList<ChangeTutorshipBean>();
	ChangeTutorshipBean tutorshipBean = initializeChangeBean(tutorship, tutorshipPeriodPartialBean.getEndDate());
	changeTutorshipBeans.add(tutorshipBean);
	if (request.getParameter("cancel") == null) {
	    Object[] args = new Object[] { executionDegree.getIdInternal(), changeTutorshipBeans };

	    List<TutorshipErrorBean> tutorshipsNotChanged = new ArrayList<TutorshipErrorBean>();
	    try {
		tutorshipsNotChanged = (List<TutorshipErrorBean>) executeService("ChangeTutorship", args);
	    } catch (FenixServiceException e) {
		addActionMessage(request, e.getMessage(), e.getArgs());
	    } catch (FenixFilterException fenixFilterExceptione) {
		// TODO Auto-generated catch block
		addActionMessage(request, fenixFilterExceptione.getMessage());
		fenixFilterExceptione.printStackTrace();
	    }

	    if (!tutorshipsNotChanged.isEmpty()) {
		for (TutorshipErrorBean tutorshipNotChanged : tutorshipsNotChanged) {
		    addActionMessage(request, tutorshipNotChanged.getMessage(), tutorshipNotChanged.getArgs());
		}
	    } else {
		request.setAttribute("successDate", "successDate");
	    }
	}
    }

    public ChangeTutorshipBean initializeChangeBean(Tutorship tutorship, Partial endDate) {
	ExecutionYear executionYear = ExecutionYear.readByPartial(tutorship.getStartDate());
	ChangeTutorshipByEntryYearBean tutorshipByEntryYearBean = new ChangeTutorshipByEntryYearBean(executionYear);
	tutorshipByEntryYearBean.addTutorship(tutorship);
	// Only one tutorship inside
	ChangeTutorshipBean changeTutorshipBean = tutorshipByEntryYearBean.getChangeTutorshipsBeans().get(0);

	DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/yyyy");
	changeTutorshipBean.setTutorshipEndMonthYear(dateTimeFormatter.print(endDate));
	return changeTutorshipBean;
    }

    /**
     * TODO: Falta adicionar novo tutor!
     * 
     * @param tutorship
     * @param executionDegree
     * @param request
     * @param mapping
     * @return
     * @throws Exception
     */
    public ActionForward deleteTutor(Tutorship tutorship, ExecutionDegree executionDegree, HttpServletRequest request,
	    ActionMapping mapping) throws Exception {

	// public List<TutorshipErrorBean> run(Integer executionDegreeID,
	// Integer tutorNumber, List<Tutorship> tutorsToDelete)
	List<Tutorship> tutorshipToDelete = new ArrayList<Tutorship>();
	tutorshipToDelete.add(tutorship);
	Object[] args = new Object[] { executionDegree.getIdInternal(), new Integer(1), tutorshipToDelete };
	DeleteTutorship d = new DeleteTutorship();
	List<TutorshipErrorBean> tutorshipErrors = new ArrayList<TutorshipErrorBean>();
	try {
	    tutorshipErrors = (List<TutorshipErrorBean>) executeService("DeleteTutorship", args);
	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}

	if (!tutorshipErrors.isEmpty()) {
	    for (TutorshipErrorBean tutorshipError : tutorshipErrors) {
		addActionMessage(request, tutorshipError.getMessage(), tutorshipError.getArgs());
	    }
	    return mapping.findForward("viewTutorship");
	}
	return mapping.findForward("viewTutorship");
    }

    public ActionForward createTutorship(HttpServletRequest request, ActionMapping mapping, ActionForm actionForm, ExecutionYear executionYear,HttpServletResponse response, 
 ExecutionDegree executionDegree, Person student,
	    Partial endDate,
	    TutorshipPeriodPartialBean tutorshipPeriodPartialBean) throws Exception {
	Boolean errorEncountered = false;
	StudentsByEntryYearBean selectedStudentsAndTutorBean = new StudentsByEntryYearBean(executionYear);
	// Initialize Tutorship creation bean to use in InsertTutorship Service
	BeanInitializer.initializeBean(selectedStudentsAndTutorBean,
 tutorshipPeriodPartialBean, executionDegree, student,
		endDate);

	Object[] args = new Object[] {
		executionDegree.getIdInternal(),
		selectedStudentsAndTutorBean };

	List<TutorshipErrorBean> tutorshipsNotInserted = new
	ArrayList<TutorshipErrorBean>();
	try {
	    tutorshipsNotInserted = (List<TutorshipErrorBean>)
	    executeService("InsertTutorship", args);
	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    errorEncountered = true;
	}
	if (!tutorshipsNotInserted.isEmpty()) {

	    errorEncountered = true;
	    for (TutorshipErrorBean tutorshipError : tutorshipsNotInserted) {
		addActionMessage(request, tutorshipError.getMessage(), tutorshipError.getArgs());
	    }
	    if (tutorshipsNotInserted.size() == 0 ) {
		Integer argument = tutorshipsNotInserted.size();
		String[] messageArgs = { argument.toString() };
		addActionMessage(request, "label.create.tutorship.remaining.correct",
			messageArgs);
	    }
	    return mapping.findForward("prepareCreate");
	} else if (!errorEncountered) {
	    request.setAttribute("success", "success");
	    return prepareTutorship(mapping, actionForm, request, response);
	}

	return mapping.findForward("viewTutorship");
    }

    public ExecutionDegree getExecutionDegree(Tutorship tutorship) {
	Registration registration = tutorship.getStudent();
	ExecutionYear executionYear = ExecutionYear.readByPartial(tutorship.getStartDate());

	DegreeCurricularPlan degreeCurricularPlan = registration.getStudentCurricularPlan(executionYear)
		.getDegreeCurricularPlan();

	ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan,
		executionYear);
	return executionDegree;
    }

    /**
     * TODO: Refactor this to a provider class This should provide Future
     * Execution Semesters
     * 
     * TODO: Remove... Now useless
     * 
     * @return
     */
    private List<ExecutionSemester> provideSemesters(Tutorship tutorship) {
	final List<ExecutionSemester> executionSemestersFinal = new ArrayList<ExecutionSemester>(RootDomainObject.getInstance()
		.getExecutionPeriods());
	Collections.sort(executionSemestersFinal, new ReverseComparator());
	List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();

	// for each existing ExecutionSemester
	for (ExecutionSemester executionSemester : executionSemestersFinal) {
	    ExecutionYear semesterExecutionYear = executionSemester.getExecutionYear();
	    // filter for years that tutorship has
	    for (ExecutionYear executionYear : tutorship.getCoveredExecutionYears()) {
		if (semesterExecutionYear.isAfter(executionYear)) {
		    executionSemesters.add(executionSemester);
		    break;
		}
	    }

	}

	return executionSemesters;
    }
}
