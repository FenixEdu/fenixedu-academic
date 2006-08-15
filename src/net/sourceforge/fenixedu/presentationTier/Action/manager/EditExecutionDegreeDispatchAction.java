/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
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
public class EditExecutionDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        InfoExecutionDegree oldInfoExecutionDegree = null;
        Object args[] = { executionDegreeId };

        try {
            oldInfoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(userView,
                    "ReadExecutionDegree", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingExecutionDegree", mapping
                    .findForward("readDegreeCurricularPlan"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        /*
         * Needed service and creation of bean of InfoExecutionYears for use in
         * jsp
         */
        List infoExecutionYearList = null;
        List infoCampusList;
        try {
            infoExecutionYearList = (List) ServiceUtils.executeService(userView,
                    "ReadAllExecutionYears", null);
            infoCampusList = (List) ServiceUtils.executeService(userView, "ReadAllCampus", null);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoExecutionYearsList", infoExecutionYearList);
        request.setAttribute("infoCampusList", infoCampusList);

        dynaForm.set("tempExamMap", oldInfoExecutionDegree.getTemporaryExamMap().toString());
        //        dynaForm.set("coordinatorNumber",
        // oldInfoExecutionDegree.getInfoCoordinator().getTeacherNumber()
        //                .toString());
        dynaForm.set("executionYearId", oldInfoExecutionDegree.getInfoExecutionYear().getIdInternal()
                .toString());
        dynaForm.set("campusId", oldInfoExecutionDegree.getInfoCampus().getIdInternal().toString());

        InfoPeriod infoPeriodLessonsFirstSemester = oldInfoExecutionDegree
                .getInfoPeriodLessonsFirstSemester();
        InfoPeriod infoPeriodLessonsSecondSemester = oldInfoExecutionDegree
                .getInfoPeriodLessonsSecondSemester();
        InfoPeriod infoPeriodExamsFirstSemester = oldInfoExecutionDegree
                .getInfoPeriodExamsFirstSemester();
        InfoPeriod infoPeriodExamsSecondSemester = oldInfoExecutionDegree
                .getInfoPeriodExamsSecondSemester();

        if (infoPeriodLessonsFirstSemester != null) {
            fillPeriodInForm(dynaForm, infoPeriodLessonsFirstSemester, "lessonsFirst");
        }

        if (infoPeriodLessonsSecondSemester != null) {
            fillPeriodInForm(dynaForm, infoPeriodLessonsSecondSemester, "lessonsSecond");
        }

        if (infoPeriodExamsFirstSemester != null) {
            fillPeriodInForm(dynaForm, infoPeriodExamsFirstSemester, "examsFirst");
        }

        if (infoPeriodExamsSecondSemester != null) {
            fillPeriodInForm(dynaForm, infoPeriodExamsSecondSemester, "examsSecond");
        }

        return mapping.findForward("editExecutionDegree");
    }

    private void fillPeriodInForm(DynaActionForm dynaForm, InfoPeriod infoPeriod, String string) {
        InfoPeriod infoPeriodAux = infoPeriod;
        ArrayList periodsList = new ArrayList();
        while (infoPeriodAux != null) {
            periodsList.add(infoPeriodAux);
            infoPeriodAux = infoPeriodAux.getNextPeriod();
        }
        int size = periodsList.size();
        String[] startDay = new String[size];
        String[] startMonth = new String[size];
        String[] startYear = new String[size];
        String[] endDay = new String[size];
        String[] endMonth = new String[size];
        String[] endYear = new String[size];
        for (int i = 0; i < size; i++) {
            InfoPeriod period = (InfoPeriod) periodsList.get(i);
            startDay[i] = new Integer(period.getStartDate().get(Calendar.DAY_OF_MONTH)).toString();
            startMonth[i] = new Integer(period.getStartDate().get(Calendar.MONTH) + 1).toString();
            startYear[i] = new Integer(period.getStartDate().get(Calendar.YEAR)).toString();
            endDay[i] = new Integer(period.getEndDate().get(Calendar.DAY_OF_MONTH)).toString();
            endMonth[i] = new Integer(period.getEndDate().get(Calendar.MONTH) + 1).toString();
            endYear[i] = new Integer(period.getEndDate().get(Calendar.YEAR)).toString();
        }
        dynaForm.set(string + "StartDay", startDay);
        dynaForm.set(string + "StartMonth", startMonth);
        dynaForm.set(string + "StartYear", startYear);

        dynaForm.set(string + "EndDay", endDay);
        dynaForm.set(string + "EndMonth", endMonth);
        dynaForm.set(string + "EndYear", endYear);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String executionYearString = (String) dynaForm.get("executionYearId");
        //String coordinatorNumberString = (String)
        // dynaForm.get("coordinatorNumber");
        String campusIdString = (String) dynaForm.get("campusId");
        String tempExamMapString = (String) dynaForm.get("tempExamMap");

        InfoExecutionDegreeEditor infoExecutionDegree = new InfoExecutionDegreeEditor();

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear(rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYearString)));
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

        //InfoTeacher infoTeacher = new InfoTeacher();
        //infoTeacher.setTeacherNumber(new Integer(coordinatorNumberString));
        //infoExecutionDegree.setInfoCoordinator(infoTeacher);

        infoExecutionDegree.setTemporaryExamMap(new Boolean(tempExamMapString));

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(degreeCurricularPlan);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoExecutionDegree.setIdInternal(executionDegreeId);

        InfoCampus infoCampus = new InfoCampus(Integer.valueOf(campusIdString));
        infoExecutionDegree.setInfoCampus(infoCampus);

        Object args[] = { infoExecutionDegree };

        try {
            ServiceUtils.executeService(userView, "EditExecutionDegree", args);

        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.execution.degree");
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("readDegreeCurricularPlan"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        return mapping.findForward("readDegreeCurricularPlan");
    }

    public ActionForward editPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        DynaValidatorForm dynaForm = (DynaValidatorForm) form;

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
                return prepareEdit(mapping, form, request, response);
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
                    return prepareEdit(mapping, form, request, response);
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
                return prepareEdit(mapping, form, request, response);
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
                    return prepareEdit(mapping, form, request, response);
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
                return prepareEdit(mapping, form, request, response);
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
                    return prepareEdit(mapping, form, request, response);
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
                return prepareEdit(mapping, form, request, response);
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
                    return prepareEdit(mapping, form, request, response);
                }
            }
        }
        InfoExecutionDegreeEditor infoExecutionDegree = new InfoExecutionDegreeEditor();
        Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));
        infoExecutionDegree.setIdInternal(executionDegreeId);

        infoExecutionDegree.setInfoPeriodLessonsFirstSemester(periodLessonFirstSemester[0]);
        infoExecutionDegree.setInfoPeriodLessonsSecondSemester(periodLessonSecondSemester[0]);
        infoExecutionDegree.setInfoPeriodExamsFirstSemester(periodExamsFirstSemester[0]);
        infoExecutionDegree.setInfoPeriodExamsSecondSemester(periodExamsSecondSemester[0]);

        Object args[] = { infoExecutionDegree };

        try {
            ServiceUtils.executeService(userView, "EditExecutionDegreePeriods", args);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
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

        return mapping.findForward("editExecutionDegree");
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

        return mapping.findForward("editExecutionDegree");
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