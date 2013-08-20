package net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor.InsertTutorship;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "coordinator", path = "/createTutorship", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "notAuthorized", path = "/coordinator/tutors/notAuthorized.jsp"),
        @Forward(name = "createTutorships", path = "/coordinator/tutors/createTutorships.jsp") })
public class CreateTutorshipDispatchAction extends TutorManagementDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareCreateTutorships(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getLoggedPerson(request);
        final String executionDegreeId = getFromRequest(request, "executionDegreeId");
        final String degreeCurricularPlanID = getFromRequest(request, "degreeCurricularPlanID");

        final ExecutionDegree executionDegree = (ExecutionDegree) AbstractDomainObject.fromExternalId(executionDegreeId);

        if (!validateDegreeTypeAccessRestrictions(executionDegree)) {
            addActionMessage(request, "error.tutor.notAuthorized.notBolonhaOrLEEC");
            return mapping.findForward("notAuthorized");
        }

        if (!validateCoordinationAccessRestrictions(person, executionDegree)) {
            addActionMessage(request, "error.tutor.notAuthorized.notCoordinatorOfDegree");
            return mapping.findForward("notAuthorized");
        }

        List<StudentsByEntryYearBean> studentsWithoutTutorBeans;
        if ((request.getParameter("showAll") != null) && (request.getParameter("showAll").equalsIgnoreCase("true"))) {
            studentsWithoutTutorBeans =
                    getAllStudentsWithoutTutorByEntryYearBeans(degreeCurricularPlanID, executionDegreeId,
                            request.getParameter("showAll"));
            request.setAttribute("showAll", "true");
        } else {
            studentsWithoutTutorBeans =
                    getStudentsWithoutTutorByEntryYearBeans(degreeCurricularPlanID, executionDegreeId, "false");
        }

        if (request.getParameter("selectedEntryYear") != null) {
            ExecutionYear entryYear = ExecutionYear.readExecutionYearByName(request.getParameter("selectedEntryYear"));
            StudentsByEntryYearBean selectedBean = getSelectedBeanFromList(studentsWithoutTutorBeans, entryYear);
            request.setAttribute("filteredStudentsBean", selectedBean);
        }

        request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
        request.setAttribute("executionDegreeId", executionDegreeId);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        return mapping.findForward("createTutorships");
    }

    public ActionForward prepareSelectGivenNumberOfTutorships(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final List<StudentsByEntryYearBean> studentsWithoutTutorBeans =
                (List<StudentsByEntryYearBean>) getViewState("studentsWithoutTutor");
        StudentsByEntryYearBean selectedBean = (StudentsByEntryYearBean) getViewState("numberOfStudentsBean");
        RenderUtils.invalidateViewState();

        selectedBean.selectStudentsToCreateTutorshipList();

        request.setAttribute("filteredStudentsBean", selectedBean);
        request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
        request.setAttribute("executionDegreeId", selectedBean.getExecutionDegreeID().toString());
        request.setAttribute("degreeCurricularPlanID", selectedBean.getDegreeCurricularPlanID().toString());
        return mapping.findForward("createTutorships");
    }

    public ActionForward selectStudentsToCreateTutorships(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final List<StudentsByEntryYearBean> studentsWithoutTutorBeans =
                (List<StudentsByEntryYearBean>) getViewState("studentsWithoutTutor");
        StudentsByEntryYearBean selectedStudentsBean = (StudentsByEntryYearBean) getViewState("selectedStudentsBean");
        RenderUtils.invalidateViewState();

        if (selectedStudentsBean != null) {
            int numberOfStudents = selectedStudentsBean.getStudentsToCreateTutorshipList().size();
            selectedStudentsBean.setNumberOfStudentsToCreateTutorship(numberOfStudents);
        }

        if (request.getParameter("clearSelection") != null) {
            selectedStudentsBean.clearSelectedStudentsToCreateTutorshipList();
            request.setAttribute("filteredStudentsBean", selectedStudentsBean);
            request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
        } else if (selectedStudentsBean.getStudentsToCreateTutorshipList().isEmpty()) {
            addActionMessage(request, "error.coordinator.tutor.createTutorship.mustSelectAtLeastOneStudent");
            request.setAttribute("filteredStudentsBean", selectedStudentsBean);
            request.setAttribute("studentsWithoutTutor", studentsWithoutTutorBeans);
        } else {
            selectedStudentsBean.setTutorshipEndMonth(Month.SEPTEMBER);
            selectedStudentsBean.setTutorshipEndYear(Tutorship.getLastPossibleTutorshipYear());
            request.setAttribute("selectedStudentsBean", selectedStudentsBean);
        }

        request.setAttribute("executionDegreeId", selectedStudentsBean.getExecutionDegreeID().toString());
        request.setAttribute("degreeCurricularPlanID", selectedStudentsBean.getDegreeCurricularPlanID().toString());
        return mapping.findForward("createTutorships");
    }

    public ActionForward prepareSelectTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StudentsByEntryYearBean selectedStudentsBean = (StudentsByEntryYearBean) getViewState("selectedStudentsBean");

        request.setAttribute("selectedStudentsBean", selectedStudentsBean);
        request.setAttribute("executionDegreeId", selectedStudentsBean.getExecutionDegreeID().toString());
        request.setAttribute("degreeCurricularPlanID", selectedStudentsBean.getDegreeCurricularPlanID().toString());
        return mapping.findForward("createTutorships");
    }

    public ActionForward prepareCreateTutorshipForSelectedStudents(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        StudentsByEntryYearBean selectedStudentsBean = (StudentsByEntryYearBean) getViewState("selectedStudentsBean");
        RenderUtils.invalidateViewState();

        final ExecutionDegree executiondegree = AbstractDomainObject.fromExternalId(selectedStudentsBean.getExecutionDegreeID());
        List<Teacher> possibleTutorsForExecutionDegree = executiondegree.getPossibleTutorsFromExecutionDegreeDepartments();

        final Teacher teacher = User.readUserByUserUId(selectedStudentsBean.getTeacherId()).getPerson().getTeacher();

        if (!possibleTutorsForExecutionDegree.contains(teacher)) {
            selectedStudentsBean.setTeacherId(null);
            addActionMessage(request, "error.tutor.cannotBeTutorOfExecutionDegree");
        } else {
            selectedStudentsBean.setTeacher(teacher);
        }

        request.setAttribute("selectedStudentsBean", selectedStudentsBean);
        request.setAttribute("executionDegreeId", selectedStudentsBean.getExecutionDegreeID().toString());
        request.setAttribute("degreeCurricularPlanID", selectedStudentsBean.getDegreeCurricularPlanID().toString());
        return mapping.findForward("createTutorships");
    }

    public ActionForward createTutorshipForSelectedStudentsAndTutor(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        StudentsByEntryYearBean selectedStudentsAndTutorBean =
                (StudentsByEntryYearBean) getViewState("selectedStudentsAndTutorBean");
        RenderUtils.invalidateViewState();

        List<TutorshipErrorBean> tutorshipsNotInserted = new ArrayList<TutorshipErrorBean>();
        try {
            tutorshipsNotInserted =
                    InsertTutorship.runInsertTutorship(selectedStudentsAndTutorBean.getExecutionDegreeID(),
                            selectedStudentsAndTutorBean);
        } catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        request.setAttribute("executionDegreeId", selectedStudentsAndTutorBean.getExecutionDegreeID().toString());
        request.setAttribute("degreeCurricularPlanID", selectedStudentsAndTutorBean.getDegreeCurricularPlanID().toString());

        if (!tutorshipsNotInserted.isEmpty()) {
            for (TutorshipErrorBean tutorship : tutorshipsNotInserted) {
                addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
            }
            request.setAttribute("selectedStudentsBean", selectedStudentsAndTutorBean);
            return mapping.findForward("createTutorships");
        } else {
            return prepareCreateTutorships(mapping, actionForm, request, response);
        }
    }

    /*
     * AUXILIARY METHODS
     */

    private StudentsByEntryYearBean getSelectedBeanFromList(List<StudentsByEntryYearBean> studentsWithoutTutorBeans,
            ExecutionYear entryYear) {
        for (StudentsByEntryYearBean bean : studentsWithoutTutorBeans) {
            if (bean.getExecutionYear().equals(entryYear)) {
                return bean;
            }
        }
        return null;
    }

    /*
     * Returns a list of beans containing a list of students without tutor for
     * the last 5 entry years
     */
    private List<StudentsByEntryYearBean> getStudentsWithoutTutorByEntryYearBeans(String degreeCurricularPlanID,
            String executionDegreeID, String showAll) {
        final DegreeCurricularPlan degreeCurricularPlan =
                (DegreeCurricularPlan) AbstractDomainObject.fromExternalId(degreeCurricularPlanID);

        Map<ExecutionYear, StudentsByEntryYearBean> studentsWithoutTutorByEntryYear =
                new HashMap<ExecutionYear, StudentsByEntryYearBean>();

        ExecutionYear entryYear = ExecutionYear.readCurrentExecutionYear();

        for (int i = 0; i < 5; i++) {
            List<StudentCurricularPlan> studentsWithoutTutor =
                    degreeCurricularPlan.getStudentsWithoutTutorGivenEntryYear(entryYear);
            if (!studentsWithoutTutor.isEmpty()) {
                StudentsByEntryYearBean bean = new StudentsByEntryYearBean(entryYear);
                bean.setStudentsList(studentsWithoutTutor);
                bean.setDegreeCurricularPlanID(degreeCurricularPlanID);
                bean.setExecutionDegreeID(executionDegreeID);
                bean.setShowAll(showAll);
                studentsWithoutTutorByEntryYear.put(entryYear, bean);
            }
            entryYear = entryYear.getPreviousExecutionYear();
        }
        ArrayList<StudentsByEntryYearBean> beans =
                new ArrayList<StudentsByEntryYearBean>(studentsWithoutTutorByEntryYear.values());
        Collections.sort(beans, new BeanComparator("executionYear"));
        Collections.reverse(beans);

        return beans;
    }

    private List<StudentsByEntryYearBean> getAllStudentsWithoutTutorByEntryYearBeans(String degreeCurricularPlanID,
            String executionDegreeID, String showAll) {
        final DegreeCurricularPlan degreeCurricularPlan =
                (DegreeCurricularPlan) AbstractDomainObject.fromExternalId(degreeCurricularPlanID);

        Map<ExecutionYear, StudentsByEntryYearBean> studentsWithoutTutorByEntryYear =
                new HashMap<ExecutionYear, StudentsByEntryYearBean>();

        ExecutionYear entryYear = ExecutionYear.readCurrentExecutionYear();

        Degree sourceDegree = degreeCurricularPlan.getEquivalencePlan().getSourceDegree();
        while ((degreeCurricularPlan.getExecutionDegreeByYear(entryYear) != null)
                || (sourceDegree.getDegreeCurricularPlansForYear(entryYear).isEmpty() == false)) {
            List<StudentCurricularPlan> studentsWithoutTutor =
                    degreeCurricularPlan.getStudentsWithoutTutorGivenEntryYear(entryYear);
            for (DegreeCurricularPlan oldDegreeCurricularPlan : sourceDegree.getDegreeCurricularPlansForYear(entryYear)) {
                studentsWithoutTutor.addAll(oldDegreeCurricularPlan.getStudentsWithoutTutorGivenEntryYear(entryYear));
            }
            if (!studentsWithoutTutor.isEmpty()) {
                StudentsByEntryYearBean bean = new StudentsByEntryYearBean(entryYear);
                bean.setStudentsList(studentsWithoutTutor);
                bean.setDegreeCurricularPlanID(degreeCurricularPlanID);
                bean.setExecutionDegreeID(executionDegreeID);
                bean.setShowAll(showAll);
                studentsWithoutTutorByEntryYear.put(entryYear, bean);
            }
            entryYear = entryYear.getPreviousExecutionYear();
        }
        ArrayList<StudentsByEntryYearBean> beans =
                new ArrayList<StudentsByEntryYearBean>(studentsWithoutTutorByEntryYear.values());
        Collections.sort(beans, new BeanComparator("executionYear"));
        Collections.reverse(beans);

        return beans;
    }

}