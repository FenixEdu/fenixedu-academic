/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.contributor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to create a Contributor
 *  
 */
public class CreateContributorDispatchAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm createContributorForm = (DynaActionForm) form;

            createContributorForm.set("contributorNumber", null);
            createContributorForm.set("contributorName", null);
            createContributorForm.set("contributorAddress", null);

            return mapping.findForward("PrepareReady");
        }
        throw new Exception();

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            DynaActionForm createContributorForm = (DynaActionForm) form;

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            // Get the Information
            Integer contributorNumber = new Integer((String) createContributorForm
                    .get("contributorNumber"));
            String contributorName = (String) createContributorForm.get("contributorName");
            String contributorAddress = (String) createContributorForm.get("contributorAddress");

            if (contributorNumber.equals(new Integer(0))) {
                ActionErrors errors = new ActionErrors();
                errors.add("error.invalid.contributorNumber", new ActionError(
                        "error.invalid.contributorNumber"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }

            // Create the new Contributor
            InfoContributor infoContributor = new InfoContributor();
            infoContributor.setContributorNumber(contributorNumber);
            infoContributor.setContributorName(contributorName);
            infoContributor.setContributorAddress(contributorAddress);

            Object args[] = { infoContributor };

            try {
                ServiceManagerServiceFactory.executeService(userView, "CreateContributor", args);
            } catch (ExistingServiceException e) {
                throw new ExistingActionException("O Contribuinte", e);
            }

            return mapping.findForward("CreateSuccess");
        }
        throw new Exception();
    }

}