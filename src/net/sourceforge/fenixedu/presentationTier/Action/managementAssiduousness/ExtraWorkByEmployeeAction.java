/*
 * Created on 11/Dez/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWork;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão
 * 
 */
public class ExtraWorkByEmployeeAction extends FenixDispatchAction {
    public static String TODO_EXTRA_WORK_CONSULT = "consult";

    public static String TODO_EXTRA_WORK_AUTHORIZE = "authorize";

    public ActionForward prepareInputs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("inputDateAndEmployee");
    }

    public ActionForward extraWorkSheetByEmployeeAndMoth(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors actionErrors = new ActionErrors();
        HttpSession session = request.getSession();

        IUserView userView = SessionUtils.getUserView(request);
        String usernameWhoKey = userView.getUtilizador();

        DynaActionForm extraWorkByEmployeeFormBean = (DynaActionForm) form;
        Integer employeeNumber = (Integer) extraWorkByEmployeeFormBean
                .get("employeeNumber");
        Integer moth = (Integer) extraWorkByEmployeeFormBean.get("moth");
        Integer year = (Integer) extraWorkByEmployeeFormBean.get("year");

        Calendar firstDayCalendar = Calendar.getInstance();
        firstDayCalendar.set(year.intValue(), moth.intValue() - 1, 1, 00, 00,
                00);
        Timestamp firstDay = new Timestamp(firstDayCalendar.getTimeInMillis());

        int lastDayMoth = firstDayCalendar
                .getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar lastDayCalendar = Calendar.getInstance();
        lastDayCalendar.set(year.intValue(), moth.intValue() - 1, lastDayMoth,
                00, 00, 00);
        Timestamp lastDay = new Timestamp(lastDayCalendar.getTimeInMillis());

        Locale locale = request.getLocale();

        List infoExtraWorkList = null;
        Object[] args = { employeeNumber, firstDay, lastDay, locale };
        try {
            infoExtraWorkList = (List) ServiceManagerServiceFactory
                    .executeService(userView, "ExtraWorkSheet", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            actionErrors.add("error.impossivel.extraWork.sheet",
                    new ActionError("error.impossivel.extraWork.sheet"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }
        if (infoExtraWorkList == null || infoExtraWorkList.size() <= 1) {
            actionErrors.add("error.impossivel.extraWork.sheet",
                    new ActionError("error.impossivel.extraWork.sheet"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }

        request.setAttribute("infoEmployee", infoExtraWorkList.get(0));
        request.setAttribute("infoExtraWorkList", infoExtraWorkList.subList(1,
                infoExtraWorkList.size()));
        request.setAttribute("year", year);
        request.setAttribute("moth", moth);
        System.out.println("--->" + infoExtraWorkList.size());	

        return (mapping.findForward("extraWorkSheet"));
    }

    public ActionForward writeExtraWork(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors actionErrors = new ActionErrors();
        HttpSession session = request.getSession();

        IUserView userView = SessionUtils.getUserView(request);
        String usernameWhoKey = userView.getUtilizador();

        DynaActionForm extraWorkByEmployeeFormBean = (DynaActionForm) form;
        Integer employeeNumber = (Integer) extraWorkByEmployeeFormBean
                .get("employeeNumber");
        String compensation = (String) extraWorkByEmployeeFormBean
                .get("compensation");

        List infoExtraWorkList = null;
        if (extraWorkByEmployeeFormBean.get("size") != null) {
            Integer sizeList = (Integer) extraWorkByEmployeeFormBean
                    .get("size");

            infoExtraWorkList = new ArrayList();
            for (int i = 0; i < sizeList.intValue(); i++) {
                InfoExtraWork infoExtraWork = getExtraWork(request, i);
                if (infoExtraWork != null) {
                    infoExtraWorkList.add(infoExtraWork);
                }
            }
        }

        List infoExtraWorkListAfterWrite = null;
        Object[] args = { usernameWhoKey, infoExtraWorkList, employeeNumber, compensation };
        try {
            infoExtraWorkListAfterWrite = (List) ServiceManagerServiceFactory
                    .executeService(userView, "WriteExtraWork", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            actionErrors.add("error.extra.work", new ActionError(
                    "error.impossivel.extraWork.write"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }
        if (infoExtraWorkListAfterWrite == null
                || infoExtraWorkListAfterWrite.size() <= 0) {
            actionErrors.add("error.extra.work", new ActionError(
                    "error.impossivel.extraWork.write"));
            saveErrors(request, actionErrors);

            return mapping.getInputForward();
        }

        request.setAttribute("infoExtraWorkList", infoExtraWorkListAfterWrite);

        return (mapping.findForward("extraWorkSheet"));
    }

    private InfoExtraWork getExtraWork(HttpServletRequest request, int index) {
        try {
            boolean existsExtraWork = false;

            Date diurnalFirstHour = null;
            Date diurnalAfterSecondHour = null;
            Date nocturnalFirstHour = null;
            Date nocturnalAfterSecondHour = null;
            Date restDay = null;
            
            SimpleDateFormat simpleHourDateFormat = new SimpleDateFormat(
            "HH:mm");
            simpleHourDateFormat.setLenient(false);
               
            if (request.getParameter("infoExtraWork[" + index
                    + "].diurnalFirstHour") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].diurnalFirstHour")
                            .length() > 0) {
                diurnalFirstHour = simpleHourDateFormat.parse(request
                        .getParameter("infoExtraWork[" + index
                                + "].diurnalFirstHour"));
                existsExtraWork = true;
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].diurnalAfterSecondHour") != null
                    && request.getParameter(
                            "infoExtraWork[" + index
                                    + "].diurnalAfterSecondHour").length() > 0) {
                diurnalAfterSecondHour = simpleHourDateFormat.parse(request
                        .getParameter("infoExtraWork[" + index
                                + "].diurnalAfterSecondHour"));
                existsExtraWork = true;
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].nocturnalFirstHour") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].nocturnalFirstHour")
                            .length() > 0) {
                nocturnalFirstHour = simpleHourDateFormat.parse(request
                        .getParameter("infoExtraWork[" + index
                                + "].nocturnalFirstHour"));
                existsExtraWork = true;
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].nocturnalAfterSecondHour") != null
                    && request.getParameter(
                            "infoExtraWork[" + index
                                    + "].nocturnalAfterSecondHour").length() > 0) {
                nocturnalAfterSecondHour = simpleHourDateFormat.parse(request
                        .getParameter("infoExtraWork[" + index
                                + "].nocturnalAfterSecondHour"));
                existsExtraWork = true;
            }
            if (request.getParameter("infoExtraWork[" + index + "].restDay") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].restDay").length() > 0) {
                restDay = simpleHourDateFormat.parse(request
                        .getParameter("infoExtraWork[" + index + "].restDay"));
                existsExtraWork = true;
            }

            if(!existsExtraWork)  {
                return null;
            }

            Integer idInternal = null;
            Date day = null;
            Date beginHour = null;
            Date endHour = null;
            Integer mealSubsidy = null;
            Boolean mealSubsidyAuthorized = new Boolean(false);
            Boolean diurnalFirstHourAuthorized = new Boolean(false);
            Boolean diurnalAfterSecondHourAuthorized = new Boolean(false);
            Boolean nocturnalFirstHourAuthorized = new Boolean(false);
            Boolean nocturnalAfterSecondHourAuthorized = new Boolean(false);
            Boolean restDayAuthorized = new Boolean(false);
            

            if (request
                    .getParameter("infoExtraWork[" + index + "].idInternal") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].idInternal")
                            .length() > 0) {
                idInternal = Integer.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].idInternal"));
            }
            
            SimpleDateFormat simpleDayDateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");
            simpleDayDateFormat.setLenient(false);

            if (request.getParameter("infoExtraWork[" + index + "].day") != null
                    && request.getParameter("infoExtraWork[" + index + "].day")
                            .length() > 0) {
                day = simpleDayDateFormat.parse(request
                        .getParameter("infoExtraWork[" + index + "].day"));
            }
            if (request.getParameter("infoExtraWork[" + index + "].beginHour") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].beginHour").length() > 0) {
                beginHour = simpleHourDateFormat
                        .parse(request.getParameter("infoExtraWork[" + index
                                + "].beginHour"));
            }
            if (request.getParameter("infoExtraWork[" + index + "].endHour") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].endHour").length() > 0) {
                endHour = simpleHourDateFormat.parse(request
                        .getParameter("infoExtraWork[" + index + "].endHour"));
            }

            if (request
                    .getParameter("infoExtraWork[" + index + "].mealSubsidy") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].mealSubsidy")
                            .length() > 0) {
                mealSubsidy = Integer.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].mealSubsidy"));
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].mealSubsidyAuthorized") != null) {
                mealSubsidyAuthorized = Boolean.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].mealSubsidyAuthorized"));
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].diurnalFirstHourAuthorized") != null) {
                diurnalFirstHourAuthorized = Boolean.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].diurnalFirstHourAuthorized"));
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].diurnalAfterSecondHourAuthorized") != null) {
                diurnalAfterSecondHourAuthorized = Boolean.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].diurnalAfterSecondHourAuthorized"));
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].nocturnalFirstHourAuthorized") != null) {
                nocturnalFirstHourAuthorized = Boolean.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].nocturnalFirstHourAuthorized"));
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].nocturnalAfterSecondHourAuthorized") != null) {
                nocturnalAfterSecondHourAuthorized = Boolean.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].nocturnalAfterSecondHourAuthorized"));
            }
            if (request.getParameter("infoExtraWork[" + index
                    + "].restDayAuthorized") != null) {
                restDayAuthorized = Boolean.valueOf(request
                        .getParameter("infoExtraWork[" + index
                                + "].restDayAuthorized"));
            }
            
            InfoExtraWork infoExtraWork = new InfoExtraWork();
            infoExtraWork.setIdInternal(idInternal);
            infoExtraWork.setDay(day);
            infoExtraWork.setBeginHour(beginHour);
            infoExtraWork.setEndHour(endHour);
            infoExtraWork.setMealSubsidy(mealSubsidy);
            infoExtraWork.setMealSubsidyAuthorized(mealSubsidyAuthorized);
            infoExtraWork.setDiurnalFirstHour(diurnalFirstHour);
            infoExtraWork
                    .setDiurnalFirstHourAuthorized(diurnalFirstHourAuthorized);
            infoExtraWork.setDiurnalAfterSecondHour(diurnalAfterSecondHour);
            infoExtraWork
                    .setDiurnalAfterSecondHourAuthorized(diurnalAfterSecondHourAuthorized);
            infoExtraWork.setNocturnalFirstHour(nocturnalFirstHour);
            infoExtraWork
                    .setNocturnalFirstHourAuthorized(nocturnalFirstHourAuthorized);
            infoExtraWork.setNocturnalAfterSecondHour(nocturnalAfterSecondHour);
            infoExtraWork
                    .setNocturnalAfterSecondHourAuthorized(nocturnalAfterSecondHourAuthorized);
            infoExtraWork.setRestDay(restDay);
            infoExtraWork.setRestDayAuthorized(restDayAuthorized);

            return infoExtraWork;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private InfoExtraWork getIDExtraWork(HttpServletRequest request, int index) {
        try {
            Integer idInternal = null;
            Boolean forDelete = new Boolean(false);

            if (request.getParameter("infoExtraWork[" + index + "].idInternal") != null
                    && request.getParameter(
                            "infoExtraWork[" + index + "].idInternal").length() > 0) {
                idInternal = Integer
                        .valueOf(request.getParameter("infoExtraWork[" + index
                                + "].idInternal"));
            }

            if (idInternal != null) {
                InfoExtraWork infoExtraWork = new InfoExtraWork();

                infoExtraWork.setIdInternal(idInternal);
                return infoExtraWork;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
