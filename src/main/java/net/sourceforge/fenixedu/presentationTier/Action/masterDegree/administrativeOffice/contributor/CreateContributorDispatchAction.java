/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.contributor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor.ContributorType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
@Mapping(module = "academicAdministration", path = "/createContributorDispatchAction", input = "contributor.createContributor",
        attribute = "createContributorForm", formBean = "createContributorForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "PrepareReady", path = "/academicAdminOffice/contributor/createContributor.jsp"),
        @Forward(name = "CreateSuccess", path = "/academicAdminOffice/contributor/createContributorSuccess.jsp") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class CreateContributorDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

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

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm createContributorForm = (DynaActionForm) form;
        IUserView userView = getUserView(request);

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

        if (StringUtils.isEmpty(createContributorForm.getString("contributorType"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("error.invalid.contributorType", new ActionError("error.invalid.contributorType"));
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
            infoContributor.createContributor();
        } catch (DomainException e) {
            //Contributor number already exists
            ActionMessages errors = new ActionMessages();
            errors.add(e.getKey(), new ActionMessage(e.getKey()));
            saveErrors(request, errors);
            return mapping.findForward("PrepareReady");
        } catch (InvalidArgumentsServiceException e) {
            throw new ExistingActionException("O Contribuinte", e);
        }

        return mapping.findForward("CreateSuccess");

    }

}
