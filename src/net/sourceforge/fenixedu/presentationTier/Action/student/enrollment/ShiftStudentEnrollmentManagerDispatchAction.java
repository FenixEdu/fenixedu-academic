/*
 * Created on 16/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoNewShiftEnrollment;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.InfoShiftEnrollment;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 * @author tdi-dev (bruno) Modified by Tânia Pousão Modified by Fernanda
 *         Quitério
 * 
 */
public class ShiftStudentEnrollmentManagerDispatchAction extends TransactionalDispatchAction {
    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("prepareEnrollmentViewWarning");
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.createToken(request);

        return prepareShiftEnrollment(mapping, form, request, response);
    }

    public ActionForward prepareShiftEnrollment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        String classID = request.getParameter("classId");
        if(classID != null){
            Integer studentID = new Integer((String)request.getParameter("studentId"));
            request.setAttribute("studentId",studentID);
            request.setAttribute("classId",new Integer(classID));
            return mapping.findForward("showEnrollmentPage");
        }
        
        DynaActionForm enrolmentForm = (DynaActionForm) form;
        Integer executionDegreeIdChosen = (Integer) enrolmentForm.get("degree");

        String studentNumber = obtainStudentNumber(request, userView);

        InfoShiftEnrollment infoShiftEnrollment = null;
        Object[] args = { Integer.valueOf(studentNumber), executionDegreeIdChosen };

        infoShiftEnrollment = (InfoShiftEnrollment) ServiceManagerServiceFactory.executeService(
                userView, "PrepareInfoShiftEnrollmentByStudentNumber", args);

        // inicialize the form with the degree chosen and student number
        enrolmentForm.set("degree", infoShiftEnrollment.getInfoExecutionDegree().getIdInternal());
        enrolmentForm.set("studentId", infoShiftEnrollment.getInfoStudent().getIdInternal());

        request.setAttribute("infoShiftEnrollment", infoShiftEnrollment);

        Object[] args1 = { Integer.valueOf(studentNumber) };
        List<InfoNewShiftEnrollment> infoNewShiftsEnrollment = (List) ServiceManagerServiceFactory
                .executeService(userView, "ReadShiftsToEnroll", args1);

        List<InfoNewShiftEnrollment> infoEnrolledNewShiftsEnrollment = (List<InfoNewShiftEnrollment>) CollectionUtils
                .select(infoNewShiftsEnrollment, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoNewShiftEnrollment infoNewShiftEnrollment = (InfoNewShiftEnrollment) arg0;
                        return infoNewShiftEnrollment.getEnrolled();
                    }
                });

        List<InfoNewShiftEnrollment> infoNotEnrolledNewShiftsEnrollment = (List<InfoNewShiftEnrollment>) CollectionUtils.subtract(
                infoNewShiftsEnrollment, infoEnrolledNewShiftsEnrollment);

        request.setAttribute("infoEnrolledNewShiftEnrollmentList", infoEnrolledNewShiftsEnrollment);
        request.setAttribute("infoNotEnrolledNewShiftEnrollmentList", infoNotEnrolledNewShiftsEnrollment);
        String selectCourses = checkParameter(request);

        // order degree's list and format them names
        if (selectCourses != null) {
            if (infoShiftEnrollment.getInfoExecutionDegreesList() != null
                    && infoShiftEnrollment.getInfoExecutionDegreesList().size() > 0) {
                Collections.sort(infoShiftEnrollment.getInfoExecutionDegreesList(),
                        new ComparatorByNameForInfoExecutionDegree());

                MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
                infoShiftEnrollment.setInfoExecutionDegreesLabelsList(ExecutionDegreesFormat
                        .buildExecutionDegreeLabelValueBean(infoShiftEnrollment
                                .getInfoExecutionDegreesList(), messageResources));
            }
            return mapping.findForward("selectCourses");
        }

        return mapping.findForward("showShiftsEnrollment");
    }

    private InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers getAttend(List infoAttends,
            final InfoExecutionCourse infoExecutionCourse) {

        return (InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers) CollectionUtils.find(
                infoAttends, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers infoAttend = 
                            (InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers) arg0;
                        return infoAttend.getDisciplinaExecucao().equals(infoExecutionCourse);
                    }
                });
    }

    private String checkParameter(HttpServletRequest request) {
        String selectCourses = request.getParameter("selectCourses");
        if (selectCourses != null) {
            request.setAttribute("selectCourses", selectCourses);
        }
        return selectCourses;
    }

    private String obtainStudentNumber(HttpServletRequest request, IUserView userView)
            throws FenixActionException, FenixFilterException {
        String studentNumber = getStudent(request);
        if (studentNumber == null) {
            InfoStudent infoStudent = obtainStudent(request, userView);
            studentNumber = infoStudent.getNumber().toString();
        }
        return studentNumber;
    }

    private String getStudent(HttpServletRequest request) {
        String studentNumber = request.getParameter("studentNumber");
        if (studentNumber == null) {
            studentNumber = (String) request.getAttribute("studentNumber");
        }
        return studentNumber;
    }

    public ActionForward unEnroleStudentFromShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        String studentIDString = request.getParameter("studentId");
        String shiftIDString = request.getParameter("shiftId");
        String executionCourseID = request.getParameter("executionCourseID");
        if(executionCourseID != null && !executionCourseID.equals("")){
            request.setAttribute("executionCourseID", executionCourseID);
        }

        Integer studentId = new Integer(studentIDString);
        Integer shiftId = new Integer(shiftIDString);

        try {
            Object args[] = { studentId, shiftId };
            ServiceManagerServiceFactory.executeService(userView, "UnEnrollStudentFromShift", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return start(mapping, form, request, response);
    }

    private InfoStudent obtainStudent(HttpServletRequest request, IUserView userView)
            throws FenixActionException, FenixFilterException {
        InfoStudent infoStudent = null;
        try {
            Object args[] = { userView.getUtilizador() };
            infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentByUsername", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoStudent;
    }

}