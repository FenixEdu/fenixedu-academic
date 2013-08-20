/*
 * Created on 24/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide.EditReimbursementGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide.ViewReimbursementGuide;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidGuideSituationActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
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
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/editReimbursementGuideSituation",
        input = "/editReimbursementGuideSituation.do?page=0&method=prepare", attribute = "editReimbursementGuideSituationForm",
        formBean = "editReimbursementGuideSituationForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "error", path = "df.page.reimbursementGuide_Error", tileProperties = @Tile(title = "teste66")),
        @Forward(name = "start", path = "df.page.editReimbursementGuideSituation", tileProperties = @Tile(title = "teste67")),
        @Forward(name = "success", path = "df.page.editReimbursementGuideSituation_Success", tileProperties = @Tile(
                title = "teste68")) })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException.class,
                key = "resources.Action.exceptions.FenixActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSituationActionException.class,
                key = "resources.Action.exceptions.InvalidSituationActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoEntryChosenActionException.class,
                key = "resources.Action.exceptions.NoEntryChosenActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditReimbursementGuideSituationDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        IUserView userView = UserView.getUser();

        DynaActionForm editReimbursementGuideSituationForm = (DynaActionForm) form;

        String reimbursementGuideId = this.getFromRequest("id", request);

        InfoReimbursementGuide infoReimbursementGuide = null;

        try {
            infoReimbursementGuide = ViewReimbursementGuide.run(reimbursementGuideId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        editReimbursementGuideSituationForm.set("officialDateDay", Data.OPTION_DEFAULT);
        editReimbursementGuideSituationForm.set("officialDateMonth", Data.OPTION_DEFAULT);
        editReimbursementGuideSituationForm.set("officialDateYear", Data.OPTION_DEFAULT);

        request.setAttribute(PresentationConstants.MONTH_DAYS_KEY, Data.getMonthDays());
        request.setAttribute(PresentationConstants.MONTH_LIST_KEY, Data.getMonths());
        request.setAttribute(PresentationConstants.YEARS_KEY, Data.getYears());
        request.setAttribute(PresentationConstants.REIMBURSEMENT_GUIDE, infoReimbursementGuide);
        request.setAttribute(PresentationConstants.REIMBURSEMENT_GUIDE_STATES_LIST, ReimbursementGuideState.values());

        return mapping.findForward("start");

    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        IUserView userView = UserView.getUser();
        DynaActionForm editReimbursementGuideSituationForm = (DynaActionForm) form;

        String remarks = (String) editReimbursementGuideSituationForm.get("remarks");
        String reimbursementGuideID = (String) editReimbursementGuideSituationForm.get("id");
        String situation = (String) editReimbursementGuideSituationForm.get("situation");
        Integer officialDateDay = (Integer) editReimbursementGuideSituationForm.get("officialDateDay");
        Integer officialDateMonth = (Integer) editReimbursementGuideSituationForm.get("officialDateMonth");
        Integer officialDateYear = (Integer) editReimbursementGuideSituationForm.get("officialDateYear");

        Date officialDate = null;

        if ((officialDateDay.intValue() > 0) && (officialDateMonth.intValue() > 0) && (officialDateYear.intValue() > 0)) {
            Calendar officialDateCalendar =
                    new GregorianCalendar(officialDateYear.intValue(), officialDateMonth.intValue(), officialDateDay.intValue());
            officialDate = officialDateCalendar.getTime();
        }

        try {

            EditReimbursementGuide.run(reimbursementGuideID, situation, officialDate, remarks, userView);

            request.setAttribute(PresentationConstants.REIMBURSEMENT_GUIDE, reimbursementGuideID);

        } catch (InvalidGuideSituationServiceException e) {
            throw new InvalidGuideSituationActionException(mapping.findForward("error"));
        } catch (InvalidReimbursementValueServiceException e) {
            throw new InvalidGuideSituationActionException(mapping.findForward("error"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("start"));
        }

        return mapping.findForward("success");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}