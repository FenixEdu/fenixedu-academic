/*
 * Created on 16/Mai/2003
 * 
 *  
 */
package ServidorApresentacao.Action.student.enrollment;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoStudent;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import DataBeans.enrollment.shift.InfoShiftEnrollment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.ExecutionDegreesFormat;
import framework.factory.ServiceManagerServiceFactory;

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
        ActionErrors errors = new ActionErrors();
        
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
                infoShiftEnrollment.setInfoExecutionDegreesLabelsList(ExecutionDegreesFormat
                        .buildExecutionDegreeLabelValueBean(infoShiftEnrollment
                                .getInfoExecutionDegreesList()));
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
            throws FenixActionException {
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
            throws FenixActionException {
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