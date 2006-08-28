/*
 * ChangeCandidateApplicationFormAction.java
 * 
 * 
 * Created on 14 de Dezembro de 2002, 12:31
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ChangePersonalInfoDispatchAction extends FenixDispatchAction {
    
    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
	
        HttpSession session = request.getSession(false);

        DynaActionForm changePersonalInformationForm = (DynaActionForm) form;

        IUserView userView = getUserView(request);

        // Clear the Session
        session.removeAttribute(SessionConstants.NATIONALITY_LIST_KEY);
        session.removeAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY);
        session.removeAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY);
        session.removeAttribute(SessionConstants.SEX_LIST_KEY);
        session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
        session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
        session.removeAttribute(SessionConstants.YEARS_KEY);
        session.removeAttribute(SessionConstants.EXPIRATION_YEARS_KEY);
        session.removeAttribute(SessionConstants.CANDIDATE_SITUATION_LIST);

        // Create Dates
        InfoPersonEditor infoPerson = new InfoPersonEditor();
        infoPerson.setTelemovel((String) changePersonalInformationForm.get("mobilePhone"));
        infoPerson.setWorkPhone((String) changePersonalInformationForm.get("workPhone"));
        infoPerson.setEmail((String) changePersonalInformationForm.get("email"));
        if (changePersonalInformationForm.get("availableEmail") != null
                && changePersonalInformationForm.get("availableEmail").equals("true")) {
            infoPerson.setAvailableEmail(Boolean.TRUE);
        } else {
            infoPerson.setAvailableEmail(Boolean.FALSE);
        }
        infoPerson.setEnderecoWeb((String) changePersonalInformationForm.get("webSite"));
        if (changePersonalInformationForm.get("availableWebSite") != null
                && changePersonalInformationForm.get("availableWebSite").equals("true")) {
            infoPerson.setAvailableWebSite(Boolean.TRUE);
        } else {
            infoPerson.setAvailableWebSite(Boolean.FALSE);
        }
        if (changePersonalInformationForm.get("availablePhoto") != null
                && changePersonalInformationForm.get("availablePhoto").equals("true")) {
            infoPerson.setAvailablePhoto(Boolean.TRUE);
        } else {
            infoPerson.setAvailablePhoto(Boolean.FALSE);
        }

        Object changeArgs[] = { userView, infoPerson };

        userView = (IUserView) ServiceManagerServiceFactory.executeService(userView,
                "ChangePersonalContactInformation", changeArgs);

        session.setAttribute(SessionConstants.U_VIEW, userView);
        return mapping.findForward("Success");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        DynaActionForm changePersonalInfoForm = (DynaActionForm) form;

        IUserView userView = getUserView(request);

        Object changeArgs[] = { userView.getUtilizador() };
        Object result = null;
        try {
            result = ServiceManagerServiceFactory.executeService(userView, "ReadPersonByUsername",
                    changeArgs);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoPerson infoPerson = (InfoPerson) result;
        changePersonalInfoForm.set("mobilePhone", infoPerson.getTelemovel());
        changePersonalInfoForm.set("workPhone", infoPerson.getWorkPhone());
        changePersonalInfoForm.set("email", infoPerson.getEmail());
        if (infoPerson.getAvailableEmail() != null) {
            changePersonalInfoForm.set("availableEmail", infoPerson.getAvailableEmail().toString());
        }

        changePersonalInfoForm.set("webSite", infoPerson.getEnderecoWeb());
        if (infoPerson.getAvailableWebSite() != null) {
            changePersonalInfoForm.set("availableWebSite", infoPerson.getAvailableWebSite().toString());
        }
        if (infoPerson.getAvailablePhoto() != null) {
            changePersonalInfoForm.set("availablePhoto", infoPerson.getAvailablePhoto().toString());
        }
        session.setAttribute(SessionConstants.PERSONAL_INFO_KEY, infoPerson);
        return mapping.findForward("prepareReady");
    }
}