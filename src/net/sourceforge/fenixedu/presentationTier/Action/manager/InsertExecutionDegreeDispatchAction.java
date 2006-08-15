/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */
public class InsertExecutionDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        List infoExecutionYearList = null;
        List infoCampusList = null;
        /*
         * Needed service and creation of bean of InfoExecutionYears for use in
         * jsp
         */
        try {
            infoExecutionYearList = (List) ServiceUtils.executeService(userView,
                    "ReadAllExecutionYears", null);
            infoCampusList = (List) ServiceUtils.executeService(userView, "ReadAllCampus", null);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("tempExamMap", "true");

        dynaForm.set("lessonsFirstStartDay", new String[] { "" });
        dynaForm.set("lessonsFirstStartMonth", new String[] { "" });
        dynaForm.set("lessonsFirstStartYear", new String[] { "" });
        dynaForm.set("lessonsFirstEndDay", new String[] { "" });
        dynaForm.set("lessonsFirstEndMonth", new String[] { "" });
        dynaForm.set("lessonsFirstEndYear", new String[] { "" });

        dynaForm.set("examsFirstStartDay", new String[] { "" });
        dynaForm.set("examsFirstStartMonth", new String[] { "" });
        dynaForm.set("examsFirstStartYear", new String[] { "" });
        dynaForm.set("examsFirstEndDay", new String[] { "" });
        dynaForm.set("examsFirstEndMonth", new String[] { "" });
        dynaForm.set("examsFirstEndYear", new String[] { "" });

        dynaForm.set("lessonsSecondStartDay", new String[] { "" });
        dynaForm.set("lessonsSecondStartMonth", new String[] { "" });
        dynaForm.set("lessonsSecondStartYear", new String[] { "" });
        dynaForm.set("lessonsSecondEndDay", new String[] { "" });
        dynaForm.set("lessonsSecondEndMonth", new String[] { "" });
        dynaForm.set("lessonsSecondEndYear", new String[] { "" });

        dynaForm.set("examsSecondStartDay", new String[] { "" });
        dynaForm.set("examsSecondStartMonth", new String[] { "" });
        dynaForm.set("examsSecondStartYear", new String[] { "" });
        dynaForm.set("examsSecondEndDay", new String[] { "" });
        dynaForm.set("examsSecondEndMonth", new String[] { "" });
        dynaForm.set("examsSecondEndYear", new String[] { "" });

        request.setAttribute("infoExecutionYearsList", infoExecutionYearList);
        request.setAttribute("infoCampusList", infoCampusList);
        return mapping.findForward("insertExecutionDegree");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear(rootDomainObject.readExecutionYearByOID(new Integer((String) dynaForm.get("executionYearId"))));
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(degreeCurricularPlan);

        InfoExecutionDegreeEditor infoExecutionDegree = new InfoExecutionDegreeEditor();
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
        //infoExecutionDegree.setInfoCoordinator(infoTeacher);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        InfoCampus infoCampus = new InfoCampus();
        infoCampus.setIdInternal(Integer.valueOf((String) dynaForm.get("campusId")));

        infoExecutionDegree.setInfoCampus(infoCampus);
        infoExecutionDegree.setTemporaryExamMap(new Boolean((String) dynaForm.get("tempExamMap")));

        Integer day, month, year;

        // periodLessonFirstSemester
        //---------------------------------------
        String[] lessonsFirstStartDay = (String[]) dynaForm.get("lessonsFirstStartDay");
        String[] lessonsFirstStartMonth = (String[]) dynaForm.get("lessonsFirstStartMonth");
        String[] lessonsFirstStartYear = (String[]) dynaForm.get("lessonsFirstStartYear");
        String[] lessonsFirstEndDay = (String[]) dynaForm.get("lessonsFirstEndDay");
        String[] lessonsFirstEndMonth = (String[]) dynaForm.get("lessonsFirstEndMonth");
        String[] lessonsFirstEndYear = (String[]) dynaForm.get("lessonsFirstEndYear");
        InfoPeriod[] periodLessonFirstSemester = new InfoPeriod[lessonsFirstStartDay.length];

        for (int i = 0; i < lessonsFirstStartDay.length; i++) {
            Calendar startPeriodLessonsFirstSemester = Calendar.getInstance();
            day = new Integer(lessonsFirstStartDay[i]);
            month = new Integer(lessonsFirstStartMonth[i]);
            year = new Integer(lessonsFirstStartYear[i]);
            startPeriodLessonsFirstSemester.set(Calendar.YEAR, year.intValue());
            startPeriodLessonsFirstSemester.set(Calendar.MONTH, month.intValue() - 1);
            startPeriodLessonsFirstSemester.set(Calendar.DAY_OF_MONTH, day.intValue());

            Calendar endPeriodLessonsFirstSemester = Calendar.getInstance();
            day = new Integer(lessonsFirstEndDay[i]);
            month = new Integer(lessonsFirstEndMonth[i]);
            year = new Integer(lessonsFirstEndYear[i]);
            endPeriodLessonsFirstSemester.set(Calendar.YEAR, year.intValue());
            endPeriodLessonsFirstSemester.set(Calendar.MONTH, month.intValue() - 1);
            endPeriodLessonsFirstSemester.set(Calendar.DAY_OF_MONTH, day.intValue());
            if (startPeriodLessonsFirstSemester.after(endPeriodLessonsFirstSemester)) {
                ActionError actionError = new ActionError("error.dateSwitched.lessons.one");
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("error.dateSwitched.lessons.one", actionError);
                saveErrors(request, actionErrors);
                return prepareInsert(mapping, form, request, response);
            }

            periodLessonFirstSemester[i] = new InfoPeriod();
            periodLessonFirstSemester[i].setStartDate(startPeriodLessonsFirstSemester);
            periodLessonFirstSemester[i].setEndDate(endPeriodLessonsFirstSemester);
            if (i > 0) {
                periodLessonFirstSemester[i - 1].setNextPeriod(periodLessonFirstSemester[i]);
                if (periodLessonFirstSemester[i - 1].getEndDate().after(startPeriodLessonsFirstSemester)) {
                    ActionError actionError = new ActionError("error.dateSwitched.periods");
                    ActionErrors actionErrors = new ActionErrors();
                    actionErrors.add("error.dateSwitched.periods", actionError);
                    saveErrors(request, actionErrors);
                    return prepareInsert(mapping, form, request, response);
                }
            }
        }

        // periodLessonSecondSemester
        //---------------------------------------
        String[] lessonsSecondStartDay = (String[]) dynaForm.get("lessonsSecondStartDay");
        String[] lessonsSecondStartMonth = (String[]) dynaForm.get("lessonsSecondStartMonth");
        String[] lessonsSecondStartYear = (String[]) dynaForm.get("lessonsSecondStartYear");
        String[] lessonsSecondEndDay = (String[]) dynaForm.get("lessonsSecondEndDay");
        String[] lessonsSecondEndMonth = (String[]) dynaForm.get("lessonsSecondEndMonth");
        String[] lessonsSecondEndYear = (String[]) dynaForm.get("lessonsSecondEndYear");
        InfoPeriod[] periodLessonSecondSemester = new InfoPeriod[lessonsSecondStartDay.length];

        for (int i = 0; i < lessonsSecondStartDay.length; i++) {
            Calendar startPeriodLessonsSecondSemester = Calendar.getInstance();
            day = new Integer(lessonsSecondStartDay[i]);
            month = new Integer(lessonsSecondStartMonth[i]);
            year = new Integer(lessonsSecondStartYear[i]);
            startPeriodLessonsSecondSemester.set(Calendar.YEAR, year.intValue());
            startPeriodLessonsSecondSemester.set(Calendar.MONTH, month.intValue() - 1);
            startPeriodLessonsSecondSemester.set(Calendar.DAY_OF_MONTH, day.intValue());

            Calendar endPeriodLessonsSecondSemester = Calendar.getInstance();
            day = new Integer(lessonsSecondEndDay[i]);
            month = new Integer(lessonsSecondEndMonth[i]);
            year = new Integer(lessonsSecondEndYear[i]);
            endPeriodLessonsSecondSemester.set(Calendar.YEAR, year.intValue());
            endPeriodLessonsSecondSemester.set(Calendar.MONTH, month.intValue() - 1);
            endPeriodLessonsSecondSemester.set(Calendar.DAY_OF_MONTH, day.intValue());
            if (startPeriodLessonsSecondSemester.after(endPeriodLessonsSecondSemester)) {
                ActionError actionError = new ActionError("error.dateSwitched.lessons.two");
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("error.dateSwitched.lessons.two", actionError);
                saveErrors(request, actionErrors);
                return prepareInsert(mapping, form, request, response);
            }

            periodLessonSecondSemester[i] = new InfoPeriod();
            periodLessonSecondSemester[i].setStartDate(startPeriodLessonsSecondSemester);
            periodLessonSecondSemester[i].setEndDate(endPeriodLessonsSecondSemester);
            if (i > 0) {
                periodLessonSecondSemester[i - 1].setNextPeriod(periodLessonSecondSemester[i]);
                if (periodLessonSecondSemester[i - 1].getEndDate().after(
                        startPeriodLessonsSecondSemester)) {
                    ActionError actionError = new ActionError("error.dateSwitched.periods");
                    ActionErrors actionErrors = new ActionErrors();
                    actionErrors.add("error.dateSwitched.periods", actionError);
                    saveErrors(request, actionErrors);
                    return prepareInsert(mapping, form, request, response);
                }
            }
        }

        // periodExamsFirstSemester
        //---------------------------------------
        String[] examsFirstStartDay = (String[]) dynaForm.get("examsFirstStartDay");
        String[] examsFirstStartMonth = (String[]) dynaForm.get("examsFirstStartMonth");
        String[] examsFirstStartYear = (String[]) dynaForm.get("examsFirstStartYear");
        String[] examsFirstEndDay = (String[]) dynaForm.get("examsFirstEndDay");
        String[] examsFirstEndMonth = (String[]) dynaForm.get("examsFirstEndMonth");
        String[] examsFirstEndYear = (String[]) dynaForm.get("examsFirstEndYear");
        InfoPeriod[] periodExamsFirstSemester = new InfoPeriod[examsFirstStartDay.length];

        for (int i = 0; i < examsFirstStartDay.length; i++) {
            Calendar startPeriodExamsFirstSemester = Calendar.getInstance();
            day = new Integer(examsFirstStartDay[i]);
            month = new Integer(examsFirstStartMonth[i]);
            year = new Integer(examsFirstStartYear[i]);
            startPeriodExamsFirstSemester.set(Calendar.YEAR, year.intValue());
            startPeriodExamsFirstSemester.set(Calendar.MONTH, month.intValue() - 1);
            startPeriodExamsFirstSemester.set(Calendar.DAY_OF_MONTH, day.intValue());

            Calendar endPeriodExamsFirstSemester = Calendar.getInstance();
            day = new Integer(examsFirstEndDay[i]);
            month = new Integer(examsFirstEndMonth[i]);
            year = new Integer(examsFirstEndYear[i]);
            endPeriodExamsFirstSemester.set(Calendar.YEAR, year.intValue());
            endPeriodExamsFirstSemester.set(Calendar.MONTH, month.intValue() - 1);
            endPeriodExamsFirstSemester.set(Calendar.DAY_OF_MONTH, day.intValue());
            if (startPeriodExamsFirstSemester.after(endPeriodExamsFirstSemester)) {
                ActionError actionError = new ActionError("error.dateSwitched.exams.one");
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("error.dateSwitched.exams.one", actionError);
                saveErrors(request, actionErrors);
                return prepareInsert(mapping, form, request, response);
            }

            periodExamsFirstSemester[i] = new InfoPeriod();
            periodExamsFirstSemester[i].setStartDate(startPeriodExamsFirstSemester);
            periodExamsFirstSemester[i].setEndDate(endPeriodExamsFirstSemester);
            if (i > 0) {
                periodExamsFirstSemester[i - 1].setNextPeriod(periodExamsFirstSemester[i]);
                if (periodExamsFirstSemester[i - 1].getEndDate().after(startPeriodExamsFirstSemester)) {
                    ActionError actionError = new ActionError("error.dateSwitched.periods");
                    ActionErrors actionErrors = new ActionErrors();
                    actionErrors.add("error.dateSwitched.periods", actionError);
                    saveErrors(request, actionErrors);
                    return prepareInsert(mapping, form, request, response);
                }
            }
        }

        // periodExamsSecondSemester
        //---------------------------------------
        String[] examsSecondStartDay = (String[]) dynaForm.get("examsSecondStartDay");
        String[] examsSecondStartMonth = (String[]) dynaForm.get("examsSecondStartMonth");
        String[] examsSecondStartYear = (String[]) dynaForm.get("examsSecondStartYear");
        String[] examsSecondEndDay = (String[]) dynaForm.get("examsSecondEndDay");
        String[] examsSecondEndMonth = (String[]) dynaForm.get("examsSecondEndMonth");
        String[] examsSecondEndYear = (String[]) dynaForm.get("examsSecondEndYear");
        InfoPeriod[] periodExamsSecondSemester = new InfoPeriod[examsSecondStartDay.length];

        for (int i = 0; i < examsSecondStartDay.length; i++) {
            Calendar startPeriodExamsSecondSemester = Calendar.getInstance();
            day = new Integer(examsSecondStartDay[i]);
            month = new Integer(examsSecondStartMonth[i]);
            year = new Integer(examsSecondStartYear[i]);
            startPeriodExamsSecondSemester.set(Calendar.YEAR, year.intValue());
            startPeriodExamsSecondSemester.set(Calendar.MONTH, month.intValue() - 1);
            startPeriodExamsSecondSemester.set(Calendar.DAY_OF_MONTH, day.intValue());

            Calendar endPeriodExamsSecondSemester = Calendar.getInstance();
            day = new Integer(examsSecondEndDay[i]);
            month = new Integer(examsSecondEndMonth[i]);
            year = new Integer(examsSecondEndYear[i]);
            endPeriodExamsSecondSemester.set(Calendar.YEAR, year.intValue());
            endPeriodExamsSecondSemester.set(Calendar.MONTH, month.intValue() - 1);
            endPeriodExamsSecondSemester.set(Calendar.DAY_OF_MONTH, day.intValue());
            if (startPeriodExamsSecondSemester.after(endPeriodExamsSecondSemester)) {
                ActionError actionError = new ActionError("error.dateSwitched.exams.two");
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("error.dateSwitched.exams.two", actionError);
                saveErrors(request, actionErrors);
                return prepareInsert(mapping, form, request, response);
            }

            periodExamsSecondSemester[i] = new InfoPeriod();
            periodExamsSecondSemester[i].setStartDate(startPeriodExamsSecondSemester);
            periodExamsSecondSemester[i].setEndDate(endPeriodExamsSecondSemester);
            if (i > 0) {
                periodExamsSecondSemester[i - 1].setNextPeriod(periodExamsSecondSemester[i]);
                if (periodExamsSecondSemester[i - 1].getEndDate().after(startPeriodExamsSecondSemester)) {
                    ActionError actionError = new ActionError("error.dateSwitched.periods");
                    ActionErrors actionErrors = new ActionErrors();
                    actionErrors.add("error.dateSwitched.periods", actionError);
                    saveErrors(request, actionErrors);
                    return prepareInsert(mapping, form, request, response);
                }
            }
        }
        infoExecutionDegree.setInfoPeriodLessonsFirstSemester(periodLessonFirstSemester[0]);
        infoExecutionDegree.setInfoPeriodLessonsSecondSemester(periodLessonSecondSemester[0]);
        infoExecutionDegree.setInfoPeriodExamsFirstSemester(periodExamsFirstSemester[0]);
        infoExecutionDegree.setInfoPeriodExamsSecondSemester(periodExamsSecondSemester[0]);

        Object args[] = { infoExecutionDegree };

        try {
            ServiceUtils.executeService(userView, "InsertExecutionDegreeAtDegreeCurricularPlan", args);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException(ex.getMessage(), ex);
        } catch (NonExistingServiceException exception) {
            throw new NonExistingActionException(exception.getMessage(), mapping
                    .findForward("readDegreeCurricularPlan"));
        }

        return mapping.findForward("readDegreeCurricularPlan");
    }

    public ActionForward addLine(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        String periodToEdit = (String) dynaForm.get("periodToEdit");

        String[] startDay = (String[]) dynaForm.get(periodToEdit + "StartDay");
        String[] startMonth = (String[]) dynaForm.get(periodToEdit + "StartMonth");
        String[] startYear = (String[]) dynaForm.get(periodToEdit + "StartYear");
        String[] endDay = (String[]) dynaForm.get(periodToEdit + "EndDay");
        String[] endMonth = (String[]) dynaForm.get(periodToEdit + "EndMonth");
        String[] endYear = (String[]) dynaForm.get(periodToEdit + "EndYear");

        startDay = addEmptyElement(startDay);
        startMonth = addEmptyElement(startMonth);
        startYear = addEmptyElement(startYear);
        endDay = addEmptyElement(endDay);
        endMonth = addEmptyElement(endMonth);
        endYear = addEmptyElement(endYear);

        dynaForm.set(periodToEdit + "StartDay", startDay);
        dynaForm.set(periodToEdit + "StartMonth", startMonth);
        dynaForm.set(periodToEdit + "StartYear", startYear);
        dynaForm.set(periodToEdit + "EndDay", endDay);
        dynaForm.set(periodToEdit + "EndMonth", endMonth);
        dynaForm.set(periodToEdit + "EndYear", endYear);

        List infoExecutionYearList = null;
        List infoCampusList = null;
        /*
         * Needed service and creation of bean of InfoExecutionYears for use in
         * jsp
         */
        try {
            infoExecutionYearList = (List) ServiceUtils.executeService(userView,
                    "ReadAllExecutionYears", null);
            infoCampusList = (List) ServiceUtils.executeService(userView, "ReadAllCampus", null);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoExecutionYearsList", infoExecutionYearList);
        request.setAttribute("infoCampusList", infoCampusList);

        return mapping.findForward("insertExecutionDegree");
    }

    private String[] addEmptyElement(String[] array) {
        int size = array.length;

        String[] returnArray = new String[size + 1];

        for (int i = 0; i < size; i++) {
            returnArray[i] = array[i];
        }
        returnArray[size] = "";

        return returnArray;
    }

    public ActionForward removeLine(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaValidatorForm) form;

        String periodToEdit = (String) dynaForm.get("periodToEdit");

        String[] startDay = (String[]) dynaForm.get(periodToEdit + "StartDay");
        String[] startMonth = (String[]) dynaForm.get(periodToEdit + "StartMonth");
        String[] startYear = (String[]) dynaForm.get(periodToEdit + "StartYear");
        String[] endDay = (String[]) dynaForm.get(periodToEdit + "EndDay");
        String[] endMonth = (String[]) dynaForm.get(periodToEdit + "EndMonth");
        String[] endYear = (String[]) dynaForm.get(periodToEdit + "EndYear");

        startDay = removeLastElement(startDay);
        startMonth = removeLastElement(startMonth);
        startYear = removeLastElement(startYear);
        endDay = removeLastElement(endDay);
        endMonth = removeLastElement(endMonth);
        endYear = removeLastElement(endYear);

        dynaForm.set(periodToEdit + "StartDay", startDay);
        dynaForm.set(periodToEdit + "StartMonth", startMonth);
        dynaForm.set(periodToEdit + "StartYear", startYear);
        dynaForm.set(periodToEdit + "EndDay", endDay);
        dynaForm.set(periodToEdit + "EndMonth", endMonth);
        dynaForm.set(periodToEdit + "EndYear", endYear);

        List infoExecutionYearList = null;
        List infoCampusList = null;
        /*
         * Needed service and creation of bean of InfoExecutionYears for use in
         * jsp
         */
        try {
            infoExecutionYearList = (List) ServiceUtils.executeService(userView,
                    "ReadAllExecutionYears", null);
            infoCampusList = (List) ServiceUtils.executeService(userView, "ReadAllCampus", null);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoExecutionYearsList", infoExecutionYearList);
        request.setAttribute("infoCampusList", infoCampusList);

        return mapping.findForward("insertExecutionDegree");
    }

    private String[] removeLastElement(String[] array) {
        int size = array.length;

        String[] returnArray = new String[size - 1];

        for (int i = 0; i < size - 1; i++) {
            returnArray[i] = array[i];
        }
        return returnArray;
    }

}