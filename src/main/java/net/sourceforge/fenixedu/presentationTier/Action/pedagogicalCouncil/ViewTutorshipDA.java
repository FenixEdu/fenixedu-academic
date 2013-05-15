package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/viewTutorship", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewTutorship", path = "/pedagogicalCouncil/tutorship/viewTutorship.jsp"),
        @Forward(name = "prepareCreateNewTutorship", path = "/pedagogicalCouncil/tutorship/createNewTutorship.jsp") })
public class ViewTutorshipDA extends FenixDispatchAction {

    /**
     * TODO: Refactor 'success'
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String success = (String) request.getAttribute("success");
        if (success != null) {
            request.setAttribute("success", success);
            return mapping.findForward("viewTutorship");
        }

        Tutorship tutorship = provideTutorship(request);
        ExecutionDegree executionDegree = getExecutionDegree(tutorship);
        final List<ExecutionSemester> executionSemesters = provideSemesters(tutorship);

        RenderUtils.invalidateViewState();
        request.setAttribute("periodBean", new TutorshipPeriodPartialBean(tutorship, executionDegree));
        return mapping.findForward("viewTutorship");
    }

    /**
     * Provide Tutorship by getting different values from request
     * 
     * @param request
     * @return
     */
    private Tutorship provideTutorship(HttpServletRequest request) {
        // If atribute "tutorshipId" is present
        if (request.getParameter("tutorshipId") != null) {
            Integer tutorshipId = getIdInternal(request, "tutorshipId");
            Tutorship tutorship = RootDomainObject.getInstance().readTutorshipByOID(tutorshipId);
            return tutorship;
        }

        // else
        String studentId = request.getParameter("studentId");
        Person studentPerson = AbstractDomainObject.fromExternalId(studentId);
        Student student = studentPerson.getStudent();
        return student.getActiveTutorships().get(0);
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

        TutorshipPeriodPartialBean tutorshipPeriodPartialBean = (TutorshipPeriodPartialBean) getRenderedObject("periodBean");
        Boolean deletionCorrect = true;
        Boolean creationCorrect = true;
        Tutorship tutorship = tutorshipPeriodPartialBean.getTutorship();
        List<TutorshipErrorBean> tutorshipsNotInserted = new ArrayList<TutorshipErrorBean>();
        List<TutorshipErrorBean> tutorshipsNotDeleted = new ArrayList<TutorshipErrorBean>();
        ExecutionDegree executionDegree = getExecutionDegree(tutorship);
        ExecutionYear executionYear = ExecutionYear.readByPartial(tutorship.getStartDate());
        Person studentPerson = tutorship.getStudent().getPerson();
        Partial endDate = tutorship.getEndDate();

        if (tutorshipPeriodPartialBean.getTeacher() != null) {

            // Process Tutorship Deletion
            try {
                tutorshipsNotDeleted = deleteTutor(tutorship, executionDegree, request, mapping);

            } catch (FenixServiceException e) {
                addActionMessage(request, e.getMessage(), e.getArgs());
                deletionCorrect = false;
            }
            if (!tutorshipsNotDeleted.isEmpty()) {
                deletionCorrect = false;
                for (TutorshipErrorBean tutorshipError : tutorshipsNotDeleted) {
                    addActionMessage(request, tutorshipError.getMessage(), tutorshipError.getArgs());
                }
            }
            // end tutorship deletion

            // Process Tutorship Creation
            if (deletionCorrect) {

                try {
                    tutorshipsNotInserted =
                            createTutorship(executionYear, executionDegree, studentPerson, endDate, tutorshipPeriodPartialBean
                                    .getTeacher().getTeacher());
                } catch (FenixServiceException e) {
                    addActionMessage(request, e.getMessage(), e.getArgs());
                    creationCorrect = false;
                }

                if (!tutorshipsNotInserted.isEmpty()) {
                    creationCorrect = false;
                    for (TutorshipErrorBean tutorshipError : tutorshipsNotInserted) {
                        addActionMessage(request, tutorshipError.getMessage(), tutorshipError.getArgs());
                    }
                    if (tutorshipsNotInserted.size() == 0) {
                        Integer argument = tutorshipsNotInserted.size();
                        String[] messageArgs = { argument.toString() };
                        addActionMessage(request, "label.create.tutorship.remaining.correct", messageArgs);
                    }
                }
            }
            // end tutorship Creation

            if (deletionCorrect && creationCorrect) {
                request.setAttribute("success", "success");
            }

        } else if (tutorshipPeriodPartialBean.getEndDate() != null) {
            changeDate(tutorship, executionDegree, tutorshipPeriodPartialBean, request, mapping);
        }

        return mapping.findForward("viewTutorship");
    }

    private void changeDate(Tutorship tutorship, ExecutionDegree executionDegree,
            TutorshipPeriodPartialBean tutorshipPeriodPartialBean, HttpServletRequest request, ActionMapping mapping) {

        final List<ChangeTutorshipBean> changeTutorshipBeans = new ArrayList<ChangeTutorshipBean>();
        ChangeTutorshipBean tutorshipBean = initializeChangeBean(tutorship, tutorshipPeriodPartialBean.getEndDate());
        changeTutorshipBeans.add(tutorshipBean);
        if (request.getParameter("cancel") == null) {
            Object[] args = new Object[] { executionDegree.getIdInternal(), changeTutorshipBeans };

            List<TutorshipErrorBean> tutorshipsNotChanged = new ArrayList<TutorshipErrorBean>();
            try {
                tutorshipsNotChanged = (List<TutorshipErrorBean>) ServiceManagerServiceFactory.executeService("ChangeTutorship", args);
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

    private ChangeTutorshipBean initializeChangeBean(Tutorship tutorship, Partial endDate) {
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
     * Method responsible for tutorship deletion
     * 
     * @param tutorship
     * @param executionDegree
     * @param request
     * @param mapping
     * @return
     * @throws Exception
     */
    private List<TutorshipErrorBean> deleteTutor(Tutorship tutorship, ExecutionDegree executionDegree,
            HttpServletRequest request, ActionMapping mapping) throws Exception {

        List<Tutorship> tutorshipToDelete = new ArrayList<Tutorship>();
        tutorshipToDelete.add(tutorship);
        Object[] args = new Object[] { executionDegree.getIdInternal(), new Integer(1), tutorshipToDelete };
        return (List<TutorshipErrorBean>) ServiceManagerServiceFactory.executeService("DeleteTutorship", args);

    }

    /**
     * Method resposible for executing tutorship creating
     * 
     * @param executionYear
     * @param executionDegree
     * @param student
     * @param endDate
     * @param teacher
     * @return
     * @throws FenixServiceException
     * @throws FenixFilterException
     * @throws Exception
     */
    private List<TutorshipErrorBean> createTutorship(ExecutionYear executionYear, ExecutionDegree executionDegree,
            Person student, Partial endDate, Teacher teacher) throws FenixFilterException, FenixServiceException {
        StudentsByEntryYearBean selectedStudentsAndTutorBean = new StudentsByEntryYearBean(executionYear);
        // Initialize Tutorship creation bean to use in InsertTutorship Service
        BeanInitializer.initializeBean(selectedStudentsAndTutorBean, teacher, executionDegree, student, endDate);
        Object[] args = new Object[] { executionDegree.getIdInternal(), selectedStudentsAndTutorBean };

        return (List<TutorshipErrorBean>) ServiceManagerServiceFactory.executeService("InsertTutorship", args);
    }

    private ExecutionDegree getExecutionDegree(Tutorship tutorship) {
        Registration registration = tutorship.getStudent();
        ExecutionYear executionYear = ExecutionYear.readByPartial(tutorship.getStartDate());

        DegreeCurricularPlan degreeCurricularPlan =
                registration.getStudentCurricularPlan(executionYear).getDegreeCurricularPlan();

        ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
        return executionDegree;
    }

    /**
     * 
     * @return
     */
    private List<ExecutionSemester> provideSemesters(Tutorship tutorship) {
        final List<ExecutionSemester> executionSemestersFinal =
                new ArrayList<ExecutionSemester>(RootDomainObject.getInstance().getExecutionPeriods());
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

    /**
     * Entrypoint to create a new tutorship
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward prepareCreateNewTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Person studentPerson = (Person) getRenderedObject("studentId");
        String studentPersonId = request.getParameter("studentId");
        Person studentPerson = AbstractDomainObject.fromExternalId(studentPersonId);
        Student student = studentPerson.getStudent();

        student.getActiveRegistrations();
        if (student.getActiveRegistrations().size() > 1) {
            // mandar mensagem de erro se tiver mais que uma matricula
            addActionMessage(request, "Aluno tem mais que uma matricula");
            return mapping.findForward("viewTutorship");
        }

        if (student.getActiveRegistrations().isEmpty()) {
            // mandar mensagem de erro se não tiver matricula
            addActionMessage(request, "Aluno não tem matricula");
            return mapping.findForward("viewTutorship");
        }

        StudentCurricularPlan studentCurricularPlan = student.getActiveRegistrations().get(0).getActiveStudentCurricularPlan();
        Degree degree = studentCurricularPlan.getDegree();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degree.getMostRecentDegreeCurricularPlan(),
                        currentExecutionYear);

        TeacherTutorshipCreationBean teacherTutorshipCreationBean = new TeacherTutorshipCreationBean(executionDegree);
        request.setAttribute("tutors", teacherTutorshipCreationBean);
        request.setAttribute("studentId", studentPersonId);
        return mapping.findForward("prepareCreateNewTutorship");
    }

    /**
     * Method to process tutor selection and tutorship creation
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward createNewTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherTutorshipCreationBean teacherTutorshipCreationBean = (TeacherTutorshipCreationBean) getRenderedObject("tutors");
        String studentPersonId = request.getParameter("studentId");
        Person studentPerson = AbstractDomainObject.fromExternalId(studentPersonId);
        Student student = studentPerson.getStudent();
        Partial endDate = createPartialForEndDate();
        Person teacherPerson = teacherTutorshipCreationBean.getTeacher();
        ExecutionDegree executionDegree = teacherTutorshipCreationBean.getExecutionDegree();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        List<TutorshipErrorBean> tutorshipsNotInserted = new ArrayList<TutorshipErrorBean>();

        Boolean creationCorrect = true;
        try {
            tutorshipsNotInserted =
                    createTutorship(currentExecutionYear, executionDegree, student.getPerson(), endDate,
                            teacherPerson.getTeacher());
        } catch (Exception e) {
            addActionMessage(request, e.getMessage());
            creationCorrect = false;
        }

        if (!tutorshipsNotInserted.isEmpty()) {
            creationCorrect = false;
            for (TutorshipErrorBean tutorshipError : tutorshipsNotInserted) {
                addActionMessage(request, tutorshipError.getMessage(), tutorshipError.getArgs());
            }
        }
        // Since there is only one
        if (creationCorrect) {
            List<Tutorship> tutorships = student.getActiveTutorships();
            Tutorship tutorship = tutorships.get(0);
            request.setAttribute("tutorshipId", tutorship.getIdInternal());
            request.setAttribute("success", "success");
        }

        return mapping.findForward("viewTutorship");
    }

    /**
     * Aux method to create Partials
     * 
     * @return
     */
    private Partial createPartialForEndDate() {
        DateTime today = new DateTime();
        return new Partial(today.plusYears(2).toLocalDate());
    }

}
