/*
 * Created on 2003/09/06
 * 
 */
package ServidorApresentacao.Action.manager;

import java.text.DateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoAdvisory;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.AdvisoryRecipients;

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

        InfoAdvisory infoAdvisory = new InfoAdvisory();
        infoAdvisory.setSender((String) advisoryForm.get("sender"));
        infoAdvisory.setSubject((String) advisoryForm.get("subject"));
        infoAdvisory.setMessage((String) advisoryForm.get("message"));
        infoAdvisory.setExpires(DateFormat.getDateInstance(DateFormat.DATE_FIELD).parse(
                (String) advisoryForm.get("experationDate")));

        Object[] args = { infoAdvisory, new AdvisoryRecipients((Integer) advisoryForm.get("recipients")) };
        ServiceUtils.executeService(userView, "CreateAdvisory", args);

        return mapping.findForward("Manage");
    }

}