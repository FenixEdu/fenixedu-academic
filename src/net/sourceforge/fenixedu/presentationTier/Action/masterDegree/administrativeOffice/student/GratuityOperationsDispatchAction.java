package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuity;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidChangeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.GratuityState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to display all the master degrees.
 *  
 */
public class GratuityOperationsDispatchAction extends DispatchAction {

    public ActionForward getInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String studentCPIDString = request.getParameter("studentCPID");
        Integer studentCPID = null;

        if ((studentCPIDString != null) && (studentCPIDString.length() != 0)) {
            studentCPID = new Integer(studentCPIDString);
        } else {
            studentCPID = (Integer) request.getAttribute("studentCPID");
        }

        InfoGratuity result = null;

        try {
            Object args[] = { studentCPID };

            result = (InfoGratuity) ServiceManagerServiceFactory.executeService(userView,
                    "ReadActiveGratuityByStudentCurricularPlanID", args);

        } catch (NonExistingServiceException e) {

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List gratuityInformationFromGuide = null;
        if (result != null) {
            request.setAttribute("gratuityInformation", result);
        }

        try {
            Object args[] = { studentCPID };
            gratuityInformationFromGuide = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadGratuityInformationByStudentCurricularPlanID", args);
        } catch (NonExistingServiceException e) {

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (gratuityInformationFromGuide != null) {
            request.setAttribute("gratuityInformationFromGuide", gratuityInformationFromGuide);
        }

        request.setAttribute("studentCPID", studentCPID);
        return mapping.findForward("ShowGratuityInformation");
    }

    public ActionForward prepareChange(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm gratuityForm = (DynaActionForm) form;

        Integer studentCPID = (Integer) request.getAttribute("studentCPID");

        if (studentCPID == null) {
            studentCPID = (Integer) gratuityForm.get("studentCPID");
        }

        gratuityForm.set("studentCPID", studentCPID);

        InfoStudentCurricularPlan studentCurricularPlan = null;
        try {
            Object args[] = { studentCPID };
            studentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentCurricularPlan", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("situationList", GratuityState.getEnumList());

        request.setAttribute("studentCurricularPlan", studentCurricularPlan);
        return mapping.findForward("ShowGratuityInformationReady");
    }

    public ActionForward changeGratuityStatus(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm gratuityForm = (DynaActionForm) form;

        Integer studentCPID = (Integer) gratuityForm.get("studentCPID");
        GratuityState gratuityState = GratuityState.getEnum(new Integer((String) gratuityForm
                .get("situation")).intValue());
        String remarks = (String) gratuityForm.get("othersRemarks");

        if ((gratuityState.equals(GratuityState.OTHER))
                && ((remarks == null) || (remarks.length() == 0))) {
            ActionError actionError = new ActionError("error.required.gratuityRemarks");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("UnNecessary", actionError);
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        try {
            Object args[] = { studentCPID, gratuityState, remarks };
            ServiceManagerServiceFactory.executeService(userView, "ChangeGratuityState", args);
        } catch (InvalidChangeServiceException e) {
            throw new InvalidChangeActionException("error.exception.invalid.invalidGratuityChange", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("ChangeSuccess");
    }

}