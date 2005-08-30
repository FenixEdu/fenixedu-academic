/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManagePreEnrollmentInquiriesDispatchAction extends FenixDispatchAction {

    public ActionForward preparePersonalDataInquiry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentByUsername", new Object[] { userView.getUtilizador() });

        Object[] args = { infoStudent.getIdInternal() };
        StudentPersonalDataAuthorizationChoice spdaChoice = (StudentPersonalDataAuthorizationChoice) ServiceManagerServiceFactory
                .executeService(userView, "ReadActualPersonalDataAuthorizationAnswer", args);

        if (spdaChoice != null) {
            return mapping.findForward("proceedToEnrollment");
        }

        return mapping.findForward("showAuthorizationInquiry");
    }

    public ActionForward registerPersonalDataInquiryAnswer(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentByUsername", new Object[] { userView.getUtilizador() });

        DynaActionForm inquiryForm = (DynaActionForm) form;
        String answer = (String) inquiryForm.get("answer");
        if(answer == null || answer.equals("")){
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("error.enrollment.personalInquiry.mandatory"));
            saveErrors(request,actionErrors);
            return mapping.getInputForward();
        }
        
        Object[] args = { infoStudent.getIdInternal(), answer };
        ServiceManagerServiceFactory.executeService(userView,
                "WriteStudentPersonalDataAuthorizationAnswer", args);

        return mapping.findForward("proceedToEnrollment");
    }
}
