package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoContributor;
import DataBeans.InfoGuide;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.NoChangeMadeServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidChangeActionException;
import ServidorApresentacao.Action.exceptions.InvalidInformationInFormActionException;
import ServidorApresentacao.Action.exceptions.NoChangeMadeActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.exceptions.NonValidChangeActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.PaymentType;
import Util.SituationOfGuide;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class EditGuideDispatchAction extends DispatchAction
{

    public ActionForward prepareEditSituation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            DynaActionForm editGuideForm = (DynaActionForm) form;
            GestorServicos serviceManager = GestorServicos.manager();

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            // Get the Information
            Integer guideNumber = new Integer(request.getParameter("number"));
            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideVersion = new Integer(request.getParameter("version"));

            Object args[] = { guideNumber, guideYear, guideVersion };

            InfoGuide infoGuide = null;
            try
            {
                infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
            } catch (NonExistingServiceException e)
            {
                throw new NonExistingActionException("A Versão da Guia", e);
            }

            editGuideForm.set("paymentDateDay", Data.OPTION_DEFAULT);
            editGuideForm.set("paymentDateMonth", Data.OPTION_DEFAULT);
            editGuideForm.set("paymentDateYear", Data.OPTION_DEFAULT);
            session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
            session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
            session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
            session.setAttribute(SessionConstants.PAYMENT_TYPE, PaymentType.toArrayList());
            session.setAttribute(SessionConstants.GUIDE, infoGuide);
            session.setAttribute(SessionConstants.GUIDE_SITUATION_LIST, SituationOfGuide.toArrayList());

            return mapping.findForward("EditReady");
        } else
            throw new Exception();
    }

    public ActionForward editGuideSituation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            GestorServicos serviceManager = GestorServicos.manager();

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
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
            if (situationOfGuide.equals(SituationOfGuide.PAYED_STRING))
            {
                if ((day == null)
                    || (day.length() == 0)
                    || (month == null)
                    || (month.length() == 0)
                    || (year == null)
                    || (year.length() == 0))
                {

                    ActionError actionError = new ActionError("error.required.paymentDate");
                    actionErrors.add("UnNecessary1", actionError);
                } else
                {
                    paymentDateYear = new Integer(request.getParameter("paymentDateYear"));
                    paymentDateMonth = new Integer(request.getParameter("paymentDateMonth"));
                    paymentDateDay = new Integer(request.getParameter("paymentDateDay"));

                    if (!Data.validDate(paymentDateDay, paymentDateMonth, paymentDateYear))
                    {
                        ActionError actionError = new ActionError("error.required.paymentDate");
                        actionErrors.add("UnNecessary1", actionError);
                    } else
                    {
                        calendar.set(
                            paymentDateYear.intValue(),
                            paymentDateMonth.intValue(),
                            paymentDateDay.intValue());
                    }
                }
            }

            if ((situationOfGuide.equals(SituationOfGuide.PAYED_STRING))
                && (paymentType.equals(PaymentType.DEFAULT_STRING)))
            {
                ActionError actionError = new ActionError("error.required.paymentType");
                actionErrors.add("UnNecessary2", actionError);
            }
            if (actionErrors.size() != 0)
            {
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            Object args[] =
                {
                    guideNumber,
                    guideYear,
                    guideVersion,
                    calendar.getTime(),
                    remarks,
                    situationOfGuide,
                    paymentType };

            try
            {
                serviceManager.executar(userView, "ChangeGuideSituation", args);
            } catch (NonValidChangeServiceException e)
            {
                throw new NonValidChangeActionException(e);
            }

            session.removeAttribute(SessionConstants.GUIDE);
            session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
            session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
            session.removeAttribute(SessionConstants.YEARS_KEY);
            session.removeAttribute(SessionConstants.PAYMENT_TYPE);
            session.removeAttribute(SessionConstants.GUIDE_SITUATION_LIST);

            return mapping.findForward("SituationChanged");

        } else
            throw new Exception();
    }

    public ActionForward prepareEditInformation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            GestorServicos serviceManager = GestorServicos.manager();

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideNumber = new Integer(request.getParameter("number"));
            Integer guideVersion = new Integer(request.getParameter("version"));

            Object args[] = { guideNumber, guideYear, guideVersion };

            // Read the Guide 

            InfoGuide infoGuide = null;
            List contributors = null;
            try
            {
                infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
                contributors = (List) serviceManager.executar(userView, "ReadAllContributors", null);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

            ArrayList contributorList = new ArrayList();
            Iterator iterator = contributors.iterator();
            while (iterator.hasNext())
            {
                InfoContributor infoContributor = (InfoContributor) iterator.next();
                contributorList.add(
                    new LabelValueBean(
                        infoContributor.getContributorName(),
                        infoContributor.getContributorNumber().toString()));
            }

            session.setAttribute(SessionConstants.GUIDE, infoGuide);
            session.setAttribute(SessionConstants.CONTRIBUTOR_LIST, contributorList);

            request.setAttribute("othersQuantity", new Integer(0));
            request.setAttribute("othersPrice", new Double(0));

            return mapping.findForward("PrepareReady");

        } else
            throw new Exception();
    }

    public ActionForward editGuideInformation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            DynaActionForm editGuideForm = (DynaActionForm) form;
            GestorServicos serviceManager = GestorServicos.manager();

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

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
            try
            {
                Object args[] = { guideNumber, guideYear, guideVersion };
                infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

            String contributorString = (String) editGuideForm.get("contributor");

            Integer contributorNumber = null;
            if ((contributorString != null) && (contributorString.length() != 0))
                contributorNumber = new Integer(contributorString);

            // Fill in the quantity List
            Enumeration arguments = request.getParameterNames();

            String[] quantityList = new String[infoGuide.getInfoGuideEntries().size()];

            try
            {
                if ((othersPriceString != null) && (othersPriceString.length() != 0))
                    othersPrice = new Double(othersPriceString);

                if ((othersQuantityString != null) && (othersQuantityString.length() != 0))
                    othersQuantity = new Integer(othersQuantityString);

                // Check for invalid quantities and prices
                if (((othersPrice != null) && (othersPrice.floatValue() < 0))
                    || ((othersQuantity != null) && (othersQuantity.intValue() < 0)))
                    throw new InvalidInformationInFormActionException(new Throwable());

                while (arguments.hasMoreElements())
                {
                    String parameter = (String) arguments.nextElement();
                    if (parameter.startsWith("quantityList"))
                    {
                        int arrayPosition = "quantityList".length();
                        String position = parameter.substring(arrayPosition);

                        // This is made to verify if the argument is a valid one. If it's not
                        // it will give a NumberFormatException 
                        String quantityString = request.getParameter(parameter);
                        if (quantityString.equals(null) || (quantityString.length() == 0))
                        {
                            quantityList[new Integer(position).intValue()] = new Integer(0).toString();
                        } else
                        {
                            Integer value = new Integer(request.getParameter(parameter));
                            if (value.intValue() < 0)
                                throw new InvalidInformationInFormActionException(new Throwable());

                            quantityList[new Integer(position).intValue()] = String.valueOf(value);
                        }
                    }
                }
            } catch (NumberFormatException e)
            {
                throw new InvalidInformationInFormActionException(e);
            }

            InfoGuide result = null;
            try
            {
                Object args[] =
                    {
                        infoGuide,
                        quantityList,
                        contributorNumber,
                        othersRemarks,
                        othersQuantity,
                        othersPrice };
                result = (InfoGuide) serviceManager.executar(userView, "EditGuideInformation", args);
            } catch (InvalidChangeServiceException e)
            {
                throw new InvalidChangeActionException(e);
            } catch (NoChangeMadeServiceException e)
            {
                throw new NoChangeMadeActionException(e);
            }

            // Read The new Guide

            List guideList = null;
            try
            {
                Object args[] = { guideNumber, guideYear };
                guideList = (List) serviceManager.executar(userView, "ChooseGuide", args);
            } catch (NonExistingServiceException e)
            {
                throw new NonExistingActionException("A Guia", e);
            }
            request.setAttribute(SessionConstants.GUIDE_LIST, guideList);

            // Add the new Version to the Guide List
            //			if (!oldGuideVersion.equals(result.getVersion())){
            //				List guides = (List) session.getAttribute(SessionConstants.GUIDE_LIST);
            //				guides.add(result);
            //				session.setAttribute(SessionConstants.GUIDE_LIST, guides);
            //			}

            request.setAttribute(SessionConstants.GUIDE, result);
            return mapping.findForward("EditInformationSuccess");

        } else
            throw new Exception();
    }

}
