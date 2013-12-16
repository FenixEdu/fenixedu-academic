/*
 * Created on 22/Abr/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.ChooseGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidGuideSituationActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;
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
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/viewReimbursementGuides",
        input = "/viewReimbursementGuides.do?page=0&method=view", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "error", path = "df.page.reimbursementGuide_Error", tileProperties = @Tile(title = "teste71")),
        @Forward(name = "start", path = "df.page.viewReimbursementGuides", tileProperties = @Tile(title = "teste72")) })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException.class,
                key = "resources.Action.exceptions.FenixActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSituationActionException.class,
                key = "resources.Action.exceptions.InvalidSituationActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ViewReimbursementGuidesDispatchAction extends FenixDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();

        Integer guideNumber = new Integer(this.getFromRequest("number", request));
        Integer guideYear = new Integer(this.getFromRequest("year", request));
        Integer guideVersion = new Integer(this.getFromRequest("version", request));

        InfoGuide infoGuide = null;

        try {
            infoGuide = ChooseGuide.runChooseGuide(guideNumber, guideYear, guideVersion);

            request.setAttribute(PresentationConstants.REIMBURSEMENT_GUIDES_LIST, infoGuide.getInfoReimbursementGuides());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        if (infoGuide.getInfoReimbursementGuides().isEmpty() == true) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistingReimbursementGuides",
                    mapping.findForward("error"));
        }

        if (infoGuide.getInfoGuideSituation().getSituation().equals(GuideState.PAYED)) {
            return mapping.findForward("start");
        }

        throw new InvalidGuideSituationActionException(mapping.findForward("error"));

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}