/*
 * Created on 13/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.EnrollStudentInShifts.StudentNotFoundServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * @author jmota Modified by Fernanda Quitério
 */
public class EnrollStudentInShiftsAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors actionErrors = new ActionErrors();
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) actionForm;

        Integer studentId = (Integer) enrollmentForm.get("studentId");
        Map shiftsToEnroll = (Map) enrollmentForm.get("shiftMap");

        List shiftList = buildShiftList(shiftsToEnroll);

        Object[] args = { studentId, shiftList };
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

    /**
     * @param shiftsToEnroll
     * @return
     */
    private List buildShiftList(Map shiftsToEnroll) {
        List list = new ArrayList();
        Iterator iterator = shiftsToEnroll.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Integer shiftId = Integer.valueOf((String) entry.getValue());
            if (shiftId != null) {
                list.add(shiftId);
            }
        }
        return list;
    }
}