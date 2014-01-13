/*
 * Created on 22/Abr/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide.ViewReimbursementGuide;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/viewReimbursementGuideDetails",
        input = "/viewReimbursementGuideDetails.do?page=0&method=view", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "error", path = "df.page.reimbursementGuide_Error", tileProperties = @Tile(title = "teste69")),
        @Forward(name = "start", path = "df.page.viewReimbursementGuideDetails", tileProperties = @Tile(title = "teste70")) })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException.class,
                key = "resources.Action.exceptions.FenixActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSituationActionException.class,
                key = "resources.Action.exceptions.InvalidSituationActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ViewReimbursementGuideDetailsDispatchAction extends FenixDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();

        String reimbursementGuideId = this.getFromRequest("id", request);

        InfoReimbursementGuide infoReimbursementGuide = null;

        try {
            infoReimbursementGuide = ViewReimbursementGuide.run(reimbursementGuideId);

            request.setAttribute(PresentationConstants.REIMBURSEMENT_GUIDE, infoReimbursementGuide);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        return mapping.findForward("start");

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}