/*
 * Created on 13/Fev/2004
 */
package ServidorApresentacao.Action.student.enrollment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
import ServidorAplicacao.Servico.enrolment.shift.EnrollStudentInShifts.StudentNotFoundServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jmota
 * Modified by Fernanda Quitério
 */
public class EnrollStudentInShiftsAction extends FenixAction
{

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors actionErrors = new ActionErrors();
        DynaValidatorForm enrollmentForm = (DynaValidatorForm) actionForm;
        
        Integer studentId = (Integer) enrollmentForm.get("studentId");
        Integer[] shiftsToEnroll = (Integer[]) enrollmentForm.get("shifts");
        List shiftsList = Arrays.asList(shiftsToEnroll);
        
        Object[] args = {studentId, shiftsList};
        try
        {
            ShiftEnrollmentErrorReport errorReport = (ShiftEnrollmentErrorReport) ServiceUtils
                    .executeService(userView, "EnrollStudentInShifts", args);
            Iterator iter = errorReport.getUnAvailableShifts().iterator();
           
            while (iter.hasNext())
            {
                InfoShift infoShift = (InfoShift) iter.next();
                ActionError actionError = new ActionError("error.shift.enrollment.capacityExceded", infoShift.getNome());               
                actionErrors.add("capacityExceded",actionError);
            }
            if(errorReport.getUnExistingShifts() != null && errorReport.getUnExistingShifts().size() > 0) {
            	ActionError actionError = new ActionError("error.shift.enrollment.nonExistingShift");               
            	actionErrors.add("nonExisting",actionError);
            }
        }catch (StudentNotFoundServiceException e)
        {
        	ActionError actionError = new ActionError("error.shift.enrollment.nonExistingStudent");               
        	actionErrors.add("nonExisting",actionError);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        if(!actionErrors.isEmpty()) {
        	saveErrors(request, actionErrors);
        	return mapping.getInputForward();
        }
        return mapping.findForward("enrollmentConfirmation");
    }
}