/*
 * Created on 16/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.InfoShiftEnrollment;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;

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

        DynaActionForm enrolmentForm = (DynaActionForm) form;
        Integer executionDegreeIdChosen = (Integer) enrolmentForm.get("degree");

        String studentNumber = obtainStudentNumber(request, userView);

        InfoShiftEnrollment infoShiftEnrollment = null;
        Object[] args = { Integer.valueOf(studentNumber), executionDegreeIdChosen };
       
        infoShiftEnrollment = (InfoShiftEnrollment) ServiceManagerServiceFactory.executeService(
                userView, "PrepareInfoShiftEnrollmentByStudentNumber", args);
        
        //inicialize the form with the degree chosen and student number
        enrolmentForm.set("degree", infoShiftEnrollment.getInfoExecutionDegree().getIdInternal());
        enrolmentForm.set("studentId", infoShiftEnrollment.getInfoStudent().getIdInternal());

        request.setAttribute("infoShiftEnrollment", infoShiftEnrollment);

        String selectCourses = checkParameter(request);

        //order degree's list and format them names
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
        String shidtIDString = request.getParameter("shiftId");

        Integer studentId = new Integer(studentIDString);
        Integer shiftId = new Integer(shidtIDString);

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