/*
 * Created on 13/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.EnrollStudentInShifts.StudentNotFoundServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author jmota Modified by Fernanda Quitério Modified by Ricardo Rodrigues
 */
public class EnrollStudentInShiftsAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors actionErrors = new ActionErrors();

        Integer shiftId = Integer.valueOf((String) request.getParameter("shiftId"));
        Integer studentId = Integer.valueOf((String) request.getParameter("studentId"));
        String executionCourseID = (String) request.getParameter("executionCourseID");
        if( executionCourseID != null && !executionCourseID.equals("")){            
            request.setAttribute("executionCourseID", executionCourseID);
        }        

        Object[] args = { studentId, shiftId };
        try {
            ShiftEnrollmentErrorReport errorReport = (ShiftEnrollmentErrorReport) ServiceUtils
                    .executeService(userView, "EnrollStudentInShifts", args);

            if (errorReport.getUnAvailableShifts() != null
                    && errorReport.getUnAvailableShifts().size() > 0) {
                Iterator iter = errorReport.getUnAvailableShifts().iterator();
                while (iter.hasNext()) {
                    InfoShift infoShift = (InfoShift) iter.next();
                    ActionError actionError = new ActionError("error.shift.enrollment.capacityExceded",
                            infoShift.getNome());
                    actionErrors.add("capacityExceded", actionError);
                }
            }
            if (errorReport.getUnExistingShifts() != null
                    && errorReport.getUnExistingShifts().size() > 0) {
                ActionError actionError = new ActionError("error.shift.enrollment.nonExistingShift");
                actionErrors.add("nonExisting", actionError);
            }
        } catch (StudentNotFoundServiceException e) {
            e.printStackTrace();
            ActionError actionError = new ActionError("error.shift.enrollment.nonExistingStudent");
            actionErrors.add("nonExisting", actionError);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            actionErrors.add("error", new ActionError(e.getMessage()));
        }
        if (!actionErrors.isEmpty()) {
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }
        return mapping.findForward("enrollmentConfirmation");
    }
}