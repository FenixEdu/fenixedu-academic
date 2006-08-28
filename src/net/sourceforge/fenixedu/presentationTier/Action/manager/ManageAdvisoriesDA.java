/*
 * Created on 2003/09/06
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Luis Crus
 */
public class ManageAdvisoriesDA extends FenixDispatchAction {

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("Manage");
    }

    public ActionForward createAdvisory(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaValidatorForm advisoryForm = (DynaValidatorForm) form;
                       
        InfoAdvisoryEditor infoAdvisory = new InfoAdvisoryEditor();
        infoAdvisory.setSender((String) advisoryForm.get("sender"));
        infoAdvisory.setSubject((String) advisoryForm.get("subject"));
        infoAdvisory.setMessage((String) advisoryForm.get("message"));
        
        if(advisoryForm.get("recipients") == null){
            setErrorMessage(mapping, request, "message.manager.advisory.recipients");
            return mapping.getInputForward();
        }
        
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date expires = null;
        try{
            expires = format.parse((String) advisoryForm.get("experationDate"));
        }
        catch(ParseException e){
            setErrorMessage(mapping, request, "message.manager.advisory.expirationDate"); 
            return mapping.getInputForward();
        }
        
        infoAdvisory.setExpires(expires);
        infoAdvisory.setCreated(Calendar.getInstance().getTime());
        
        Object[] args = { infoAdvisory, new AdvisoryRecipients((Integer) advisoryForm.get("recipients")) };
        ServiceUtils.executeService(userView, "CreateAdvisory", args);

        setErrorMessage(mapping, request, "label.success");
        return mapping.findForward("success");
    }


    private void setErrorMessage(ActionMapping mapping, HttpServletRequest request, String label) {
        
        ActionErrors actionMessages = new ActionErrors();
        actionMessages.add("error", new ActionError(label));
        saveErrors(request, actionMessages);
    }
}