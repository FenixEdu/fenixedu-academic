/*
 * Created on 13/Fev/2004
 */
package ServidorApresentacao.Action.student.enrollment;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import DataBeans.InfoShift;
import DataBeans.enrollment.shift.ShiftEnrollmentErrorReport;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jmota
 */
public class EnrollStudentInShiftsAction extends FenixAction
{

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = {};
        ActionMessages actionErrors = new ActionMessages();
        try
        {
            ShiftEnrollmentErrorReport errorReport = (ShiftEnrollmentErrorReport) ServiceUtils
                    .executeService(userView, "EnrollStudentInShifts", args);
            Iterator iter = errorReport.getUnAvailableShifts().iterator();
           
            while (iter.hasNext())
            {
                InfoShift infoShift = (InfoShift) iter.next();
                ActionMessage actionMessage = new ActionMessage("shift.enrollment.capacityExceded", infoShift.getNome());               
                actionErrors.add("capacityExceded",actionMessage);
            }
            iter = errorReport.getUnExistingShifts().iterator();
            while (iter.hasNext())
            {
                Integer shiftId = (Integer) iter.next();
                ActionMessage actionMessage = new ActionMessage("shift.enrollment.nonExistingShift", shiftId);               
                actionErrors.add("nonExisting",actionMessage);
            }
        }
        catch (FenixServiceException e)
        {
            
            throw new FenixActionException(e);
        }

        return null;
    }

}
