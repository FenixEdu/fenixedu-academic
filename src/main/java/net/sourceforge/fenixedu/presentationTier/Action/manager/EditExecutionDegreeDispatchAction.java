/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditExecutionDegreePeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadAllExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.places.campus.ReadAllCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author lmac1
 */
@Mapping(module = "manager", path = "/editExecutionDegree", input = "/editExecutionDegree.do?method=prepareEdit&page=0",
        attribute = "executionDegreeForm", formBean = "executionDegreeForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "readDegreeCurricularPlan", path = "/readDegreeCurricularPlan.do"),
        @Forward(name = "editExecutionDegree", path = "/manager/editExecutionDegree_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")) })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
                key = "resources.Action.exceptions.ExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditExecutionDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoExecutionDegree oldInfoExecutionDegree = null;
        try {
            oldInfoExecutionDegree = ReadExecutionDegree.runReadExecutionDegree(request.getParameter("executionDegreeId"));
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingExecutionDegree",
                    mapping.findForward("readDegreeCurricularPlan"));
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
            infoExecutionYearList = ReadAllExecutionYears.run();
            infoCampusList = ReadAllCampus.run();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoExecutionYearsList", infoExecutionYearList);
        request.setAttribute("infoCampusList", infoCampusList);
        ExecutionSemester executionSemester =
                (ExecutionSemester) ExecutionInterval.getExecutionInterval(oldInfoExecutionDegree.getExecutionDegree()
                        .getAcademicInterval());
        dynaForm.set("tempExamMap", String.valueOf(oldInfoExecutionDegree.isPublishedExam(executionSemester)));
        // dynaForm.set("coordinatorNumber",
        // oldInfoExecutionDegree.getInfoCoordinator().getTeacherNumber()
        // .toString());
        dynaForm.set("executionYearId", oldInfoExecutionDegree.getInfoExecutionYear().getExternalId().toString());
        dynaForm.set("campusId", oldInfoExecutionDegree.getInfoCampus().getExternalId().toString());

        InfoPeriod infoPeriodLessonsFirstSemester = oldInfoExecutionDegree.getInfoPeriodLessonsFirstSemester();
        InfoPeriod infoPeriodLessonsSecondSemester = oldInfoExecutionDegree.getInfoPeriodLessonsSecondSemester();
        InfoPeriod infoPeriodExamsFirstSemester = oldInfoExecutionDegree.getInfoPeriodExamsFirstSemester();
        InfoPeriod infoPeriodExamsSecondSemester = oldInfoExecutionDegree.getInfoPeriodExamsSecondSemester();

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

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanId");
        final DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanId);
        String executionDegreeId = request.getParameter("executionDegreeId");

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String executionYearString = (String) dynaForm.get("executionYearId");
        // String coordinatorNumberString = (String)
        // dynaForm.get("coordinatorNumber");
        String campusIdString = (String) dynaForm.get("campusId");
        String tempExamMapString = (String) dynaForm.get("tempExamMap");

        InfoExecutionDegreeEditor infoExecutionDegree = new InfoExecutionDegreeEditor();

        InfoExecutionYear infoExecutionYear =
                new InfoExecutionYear(AbstractDomainObject.<ExecutionYear> fromExternalId(executionYearString));
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

        // InfoTeacher infoTeacher = new InfoTeacher();
        // infoTeacher.setTeacherNumber(new Integer(coordinatorNumberString));
        // infoExecutionDegree.setInfoCoordinator(infoTeacher);

        infoExecutionDegree.setTemporaryExamMap(new Boolean(tempExamMapString));

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(degreeCurricularPlan);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoExecutionDegree.setExternalId(executionDegreeId);

        InfoCampus infoCampus = new InfoCampus(campusIdString);
        infoExecutionDegree.setInfoCampus(infoCampus);

        try {
            EditExecutionDegree.run(infoExecutionDegree);

        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.execution.degree");
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping.findForward("readDegreeCurricularPlan"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        return mapping.findForward("readDegreeCurricularPlan");
    }

    public ActionForward editPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaValidatorForm dynaForm = (DynaValidatorForm) form;

        Integer day, month, year;

        // periodLessonFirstSemester
        // ---------------------------------------
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
                addErrorMessage(request, "error.dateSwitched.lessons.one", "error.dateSwitched.lessons.one");
                return prepareEdit(mapping, form, request, response);
            }

            periodLessonFirstSemester[i] = new InfoPeriod();
            periodLessonFirstSemester[i].setStartDate(startPeriodLessonsFirstSemester);
            periodLessonFirstSemester[i].setEndDate(endPeriodLessonsFirstSemester);
            if (i > 0) {
                periodLessonFirstSemester[i - 1].setNextPeriod(periodLessonFirstSemester[i]);
                if (periodLessonFirstSemester[i - 1].getEndDate().after(startPeriodLessonsFirstSemester)) {
                    addErrorMessage(request, "error.dateSwitched.periods", "error.dateSwitched.periods");
                    return prepareEdit(mapping, form, request, response);
                }
            }
        }

        // periodLessonSecondSemester
        // ---------------------------------------
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
                addErrorMessage(request, "error.dateSwitched.lessons.two", "error.dateSwitched.lessons.two");
                return prepareEdit(mapping, form, request, response);
            }

            periodLessonSecondSemester[i] = new InfoPeriod();
            periodLessonSecondSemester[i].setStartDate(startPeriodLessonsSecondSemester);
            periodLessonSecondSemester[i].setEndDate(endPeriodLessonsSecondSemester);
            if (i > 0) {
                periodLessonSecondSemester[i - 1].setNextPeriod(periodLessonSecondSemester[i]);
                if (periodLessonSecondSemester[i - 1].getEndDate().after(startPeriodLessonsSecondSemester)) {
                    addErrorMessage(request, "error.dateSwitched.periods", "error.dateSwitched.periods");
                    return prepareEdit(mapping, form, request, response);
                }
            }
        }

        // periodExamsFirstSemester
        // ---------------------------------------
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
                addErrorMessage(request, "error.dateSwitched.exams.one", "error.dateSwitched.exams.one");
                return prepareEdit(mapping, form, request, response);
            }

            periodExamsFirstSemester[i] = new InfoPeriod();
            periodExamsFirstSemester[i].setStartDate(startPeriodExamsFirstSemester);
            periodExamsFirstSemester[i].setEndDate(endPeriodExamsFirstSemester);
            if (i > 0) {
                periodExamsFirstSemester[i - 1].setNextPeriod(periodExamsFirstSemester[i]);
                if (periodExamsFirstSemester[i - 1].getEndDate().after(startPeriodExamsFirstSemester)) {
                    addErrorMessage(request, "error.dateSwitched.periods", "error.dateSwitched.periods");
                    return prepareEdit(mapping, form, request, response);
                }
            }
        }

        // periodExamsSecondSemester
        // ---------------------------------------
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
                addErrorMessage(request, "error.dateSwitched.exams.two", "error.dateSwitched.exams.two");
                return prepareEdit(mapping, form, request, response);
            }

            periodExamsSecondSemester[i] = new InfoPeriod();
            periodExamsSecondSemester[i].setStartDate(startPeriodExamsSecondSemester);
            periodExamsSecondSemester[i].setEndDate(endPeriodExamsSecondSemester);
            if (i > 0) {
                periodExamsSecondSemester[i - 1].setNextPeriod(periodExamsSecondSemester[i]);
                if (periodExamsSecondSemester[i - 1].getEndDate().after(startPeriodExamsSecondSemester)) {
                    addErrorMessage(request, "error.dateSwitched.periods", "error.dateSwitched.periods");
                    return prepareEdit(mapping, form, request, response);
                }
            }
        }
        InfoExecutionDegreeEditor infoExecutionDegree = new InfoExecutionDegreeEditor();
        infoExecutionDegree.setExternalId(request.getParameter("executionDegreeId"));

        infoExecutionDegree.setInfoPeriodLessonsFirstSemester(periodLessonFirstSemester[0]);
        infoExecutionDegree.setInfoPeriodLessonsSecondSemester(periodLessonSecondSemester[0]);
        infoExecutionDegree.setInfoPeriodExamsFirstSemester(periodExamsFirstSemester[0]);
        infoExecutionDegree.setInfoPeriodExamsSecondSemester(periodExamsSecondSemester[0]);

        EditExecutionDegreePeriods.run(infoExecutionDegree);

        return mapping.findForward("readDegreeCurricularPlan");

    }

    public ActionForward addLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

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
            infoExecutionYearList = ReadAllExecutionYears.run();
            infoCampusList = ReadAllCampus.run();
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
            HttpServletResponse response) throws FenixActionException {

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
            infoExecutionYearList = ReadAllExecutionYears.run();
            infoCampusList = ReadAllCampus.run();
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