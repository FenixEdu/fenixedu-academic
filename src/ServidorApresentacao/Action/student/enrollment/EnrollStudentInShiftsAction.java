/*
 * Created on 13/Fev/2004
 */
package ServidorApresentacao.Action.student.enrollment;

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

import DataBeans.InfoShift;
import DataBeans.enrollment.shift.ShiftEnrollmentErrorReport;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.enrollment.shift.EnrollStudentInShifts.StudentNotFoundServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jmota Modified by Fernanda Quitério
 */
public class EnrollStudentInShiftsAction extends FenixAction
{

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors actionErrors = new ActionErrors();
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) actionForm;

        Integer studentId = (Integer) enrollmentForm.get("studentId");
        Map shiftsToEnroll = (Map) enrollmentForm.get("shiftMap");

        List shiftList =buildShiftList(shiftsToEnroll);


        Object[] args = { studentId, shiftList };
        try
        {
            ShiftEnrollmentErrorReport errorReport =
                (ShiftEnrollmentErrorReport) ServiceUtils.executeService(
                    userView,
                    "EnrollStudentInShifts",
                    args);
            Iterator iter = errorReport.getUnAvailableShifts().iterator();

            while (iter.hasNext())
            {
                InfoShift infoShift = (InfoShift) iter.next();
                ActionError actionError =
                    new ActionError("error.shift.enrollment.capacityExceded", infoShift.getNome());
                actionErrors.add("capacityExceded", actionError);
            }
            if (errorReport.getUnExistingShifts() != null
                && errorReport.getUnExistingShifts().size() > 0)
            {
                ActionError actionError = new ActionError("error.shift.enrollment.nonExistingShift");
                actionErrors.add("nonExisting", actionError);
            }
        } catch (StudentNotFoundServiceException e)
        {
            ActionError actionError = new ActionError("error.shift.enrollment.nonExistingStudent");
            actionErrors.add("nonExisting", actionError);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        if (!actionErrors.isEmpty())
        {
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }
        return mapping.findForward("enrollmentConfirmation");
    }

    /**
     * @param shiftsToEnroll
     * @return
     */
    private List buildShiftList(Map shiftsToEnroll)
    {
        List list = new ArrayList();
        Iterator iterator = shiftsToEnroll.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            Integer shiftId = Integer.valueOf((String) entry.getValue());
            if (shiftId != null)
            {
                list.add(shiftId);
            }
        }
        return list;
    }
}