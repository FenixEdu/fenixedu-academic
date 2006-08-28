package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoChangeMadeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidChangeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoChangeMadeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonValidChangeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *  
 */
public class EditGuideDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEditSituation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm editGuideForm = (DynaActionForm) form;

            IUserView userView = getUserView(request);

            // Get the Information
            Integer guideNumber = new Integer(request.getParameter("number"));
            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideVersion = new Integer(request.getParameter("version"));

            Object args[] = { guideNumber, guideYear, guideVersion };

            InfoGuide infoGuide = null;
            try {
                infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                        "ChooseGuide", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Versão da Guia", e);
            }

            editGuideForm.set("paymentDateDay", Data.OPTION_DEFAULT);
            editGuideForm.set("paymentDateMonth", Data.OPTION_DEFAULT);
            editGuideForm.set("paymentDateYear", Data.OPTION_DEFAULT);
            session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
            session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
            session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
            session.setAttribute(SessionConstants.GUIDE, infoGuide);

            return mapping.findForward("EditReady");
        }
        throw new Exception();
    }

    public ActionForward editGuideSituation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = getUserView(request);
            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideNumber = new Integer(request.getParameter("number"));
            Integer guideVersion = new Integer(request.getParameter("version"));

            String remarks = request.getParameter("remarks");
            String situationOfGuide = request.getParameter("guideSituation");

            String day = request.getParameter("paymentDateDay");
            String month = request.getParameter("paymentDateMonth");
            String year = request.getParameter("paymentDateYear");

            String paymentType = request.getParameter("paymentType");

            // Final form Check

            Integer paymentDateYear = null;
            Integer paymentDateMonth = null;
            Integer paymentDateDay = null;

            ActionErrors actionErrors = new ActionErrors();

            Calendar calendar = Calendar.getInstance();
            if (situationOfGuide.equals(GuideState.PAYED)) {
                if ((day == null) || (day.length() == 0) || (month == null) || (month.length() == 0)
                        || (year == null) || (year.length() == 0)) {

                    ActionError actionError = new ActionError("error.required.paymentDate");
                    actionErrors.add("UnNecessary1", actionError);
                } else {
                    paymentDateYear = new Integer(request.getParameter("paymentDateYear"));
                    paymentDateMonth = new Integer(request.getParameter("paymentDateMonth"));
                    paymentDateDay = new Integer(request.getParameter("paymentDateDay"));

                    if (!Data.validDate(paymentDateDay, paymentDateMonth, paymentDateYear)) {
                        ActionError actionError = new ActionError("error.required.paymentDate");
                        actionErrors.add("UnNecessary1", actionError);
                    } else {
                        calendar.set(paymentDateYear.intValue(), paymentDateMonth.intValue(),
                                paymentDateDay.intValue());
                    }
                }
            }

            if ((situationOfGuide.equals(GuideState.PAYED))
                    && (paymentType.equals(""))) {
                ActionError actionError = new ActionError("error.required.paymentType");
                actionErrors.add("UnNecessary2", actionError);
            }
            if (actionErrors.size() != 0) {
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            Object args[] = { guideNumber, guideYear, guideVersion, calendar.getTime(), remarks,
                    situationOfGuide, paymentType, userView };

            try {
                ServiceManagerServiceFactory.executeService(userView, "ChangeGuideSituation", args);
            } catch (NonValidChangeServiceException e) {
                throw new NonValidChangeActionException(e);
            } catch (ExistingServiceException e) {
                ActionError actionError = new ActionError(e.getMessage());
                actionErrors.add("existing", actionError);
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            session.removeAttribute(SessionConstants.GUIDE);
            session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
            session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
            session.removeAttribute(SessionConstants.YEARS_KEY);
            session.removeAttribute(SessionConstants.PAYMENT_TYPE);
            session.removeAttribute(SessionConstants.GUIDE_SITUATION_LIST);

            return mapping.findForward("SituationChanged");

        }
        throw new Exception();
    }

    public ActionForward prepareEditInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = getUserView(request);
            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideNumber = new Integer(request.getParameter("number"));
            Integer guideVersion = new Integer(request.getParameter("version"));

            Object args[] = { guideNumber, guideYear, guideVersion };

            // Read the Guide

            InfoGuide infoGuide = null;
            List contributors = null;
            try {
                infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                        "ChooseGuide", args);
                contributors = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadAllContributors", null);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            List contributorList = new ArrayList();
            Iterator iterator = contributors.iterator();
            while (iterator.hasNext()) {
                InfoContributor infoContributor = (InfoContributor) iterator.next();
                contributorList.add(new LabelValueBean("Nr. " + infoContributor.getContributorNumber() + ", " + infoContributor.getContributorName(), infoContributor.getContributorNumber()));
            }

            session.setAttribute(SessionConstants.GUIDE, infoGuide);
            session.setAttribute(SessionConstants.CONTRIBUTOR_LIST, contributorList);

            request.setAttribute("othersQuantity", new Integer(0));
            request.setAttribute("othersPrice", new Double(0));

            return mapping.findForward("PrepareReady");

        }
        throw new Exception();
    }

    public ActionForward editGuideInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm editGuideForm = (DynaActionForm) form;

            IUserView userView = getUserView(request);

            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideNumber = new Integer(request.getParameter("number"));
            Integer guideVersion = new Integer(request.getParameter("version"));

            String othersQuantityString = request.getParameter("othersQuantity");
            String othersPriceString = request.getParameter("othersPrice");

            Integer othersQuantity = null;
            Double othersPrice = null;

            String othersRemarks = request.getParameter("othersRemarks");

            // Read the Guide

            InfoGuide infoGuide = null;
            try {
                Object args[] = { guideNumber, guideYear, guideVersion };
                infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                        "ChooseGuide", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            String contributorString = (String) editGuideForm.get("contributor");

            Integer contributorNumber = null;
            if ((contributorString != null) && (contributorString.length() != 0))
                contributorNumber = new Integer(contributorString);

            // Fill in the quantity List
            Enumeration arguments = request.getParameterNames();

            String[] quantityList = new String[infoGuide.getInfoGuideEntries().size()];

            try {
                if ((othersPriceString != null) && (othersPriceString.length() != 0))
                    othersPrice = new Double(othersPriceString);

                if ((othersQuantityString != null) && (othersQuantityString.length() != 0))
                    othersQuantity = new Integer(othersQuantityString);

                // Check for invalid quantities and prices
                if (((othersPrice != null) && (othersPrice.floatValue() < 0))
                        || ((othersQuantity != null) && (othersQuantity.intValue() < 0)))
                    throw new InvalidInformationInFormActionException(new Throwable());

                while (arguments.hasMoreElements()) {
                    String parameter = (String) arguments.nextElement();
                    if (parameter.startsWith("quantityList")) {
                        int arrayPosition = "quantityList".length();
                        String position = parameter.substring(arrayPosition);

                        // This is made to verify if the argument is a valid
                        // one. If it's not
                        // it will give a NumberFormatException
                        String quantityString = request.getParameter(parameter);
                        if (quantityString.equals(null) || (quantityString.length() == 0)) {
                            quantityList[new Integer(position).intValue()] = new Integer(0).toString();
                        } else {
                            Integer value = new Integer(request.getParameter(parameter));
                            if (value.intValue() < 0)
                                throw new InvalidInformationInFormActionException(new Throwable());

                            quantityList[new Integer(position).intValue()] = String.valueOf(value);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                throw new InvalidInformationInFormActionException(e);
            }

            InfoGuide result = null;
            try {
                Object args[] = { infoGuide, quantityList, contributorNumber, othersRemarks,
                        othersQuantity, othersPrice };
                result = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                        "EditGuideInformation", args);
            } catch (InvalidChangeServiceException e) {
                throw new InvalidChangeActionException(e);
            } catch (NoChangeMadeServiceException e) {
                throw new NoChangeMadeActionException(e);
            }

            // Read The new Guide

            List guideList = null;
            try {
                Object args[] = { guideNumber, guideYear };
                guideList = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide",
                        args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Guia", e);
            }
            request.setAttribute(SessionConstants.GUIDE_LIST, guideList);

            request.setAttribute(SessionConstants.GUIDE, result);
            return mapping.findForward("EditInformationSuccess");

        }
        throw new Exception();
    }

}