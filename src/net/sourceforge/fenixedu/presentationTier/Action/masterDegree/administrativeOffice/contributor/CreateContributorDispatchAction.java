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
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor.ContributorType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class CreateContributorDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm createContributorForm = (DynaActionForm) form;

            createContributorForm.set("contributorNumber", null);
            createContributorForm.set("contributorName", null);
            createContributorForm.set("contributorAddress", null);
            createContributorForm.set("areaCode", null);
            createContributorForm.set("areaOfAreaCode", null);
            createContributorForm.set("area", null);
            createContributorForm.set("parishOfResidence", null);
            createContributorForm.set("districtSubdivisionOfResidence", null);
            createContributorForm.set("districtOfResidence", null);

            return mapping.findForward("PrepareReady");
        }
        throw new Exception();

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm createContributorForm = (DynaActionForm) form;
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            final String contributorName = (String) createContributorForm.get("contributorName");
            try {
                Integer.valueOf((String) createContributorForm.get("contributorName"));    

                ActionErrors errors = new ActionErrors();
                errors.add("error.invalid.contributorName", new ActionError("error.invalid.contributorName"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            } catch (NumberFormatException e) {
                // do nothing, name is not a number, it's correct
            }
            
            Integer contributorNumber = null;
            try {
                contributorNumber = Integer.valueOf((String) createContributorForm.get("contributorNumber"));    
                if (contributorNumber.intValue() == 0) {
                    ActionErrors errors = new ActionErrors();
                    errors.add("error.invalid.contributorNumber", new ActionError("error.invalid.contributorNumber"));
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                }
            } catch (NumberFormatException e) {
                ActionErrors errors = new ActionErrors();
                errors.add("error.invalid.contributorNumber", new ActionError("error.invalid.contributorNumber"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }

            InfoContributor infoContributor = new InfoContributor();
            infoContributor.setContributorType(ContributorType.valueOf((String) createContributorForm.get("contributorType")));
            infoContributor.setContributorName(contributorName);
            infoContributor.setContributorNumber(contributorNumber.toString());
            infoContributor.setContributorAddress((String) createContributorForm.get("contributorAddress"));
            infoContributor.setAreaCode((String) createContributorForm.get("areaCode"));
            infoContributor.setAreaOfAreaCode((String) createContributorForm.get("areaOfAreaCode"));
            infoContributor.setArea((String) createContributorForm.get("area"));
            infoContributor.setParishOfResidence((String) createContributorForm.get("parishOfResidence"));
            infoContributor.setDistrictSubdivisionOfResidence((String) createContributorForm.get("districtSubdivisionOfResidence"));
            infoContributor.setDistrictOfResidence((String) createContributorForm.get("districtOfResidence"));

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
