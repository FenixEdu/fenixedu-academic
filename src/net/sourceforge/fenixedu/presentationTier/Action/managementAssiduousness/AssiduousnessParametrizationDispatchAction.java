package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory;
import net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory.WorkScheduleTypeFactoryCreator;
import net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory.WorkScheduleTypeFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AssiduousnessParametrizationDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsertJustificationMotive(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        return mapping.findForward("edit-justification-motive");
    }

    public ActionForward prepareEditJustificationMotive(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        Integer justificationMotiveId = new Integer(request.getParameter("idInternal"));
        JustificationMotive justificationMotive = rootDomainObject
                .readJustificationMotiveByOID(justificationMotiveId);
        request.setAttribute("justificationMotive", justificationMotive);
        return mapping.findForward("edit-justification-motive");
    }

    public ActionForward sendErrorToEditJustificationMotive(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        RenderUtils.invalidateViewState();
        setError(request, "message", "error.acronymAlreadyExists");
        return mapping.findForward("edit-justification-motive");
    }

    public ActionForward prepareInsertRegularizationMotive(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        return mapping.findForward("edit-regularization-motive");
    }

    public ActionForward prepareEditRegularizationMotive(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        Integer justificationMotiveId = new Integer(request.getParameter("idInternal"));
        JustificationMotive justificationMotive = rootDomainObject
                .readJustificationMotiveByOID(justificationMotiveId);
        request.setAttribute("justificationMotive", justificationMotive);
        return mapping.findForward("edit-regularization-motive");
    }

    public ActionForward sendErrorToEditRegularizationMotive(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        RenderUtils.invalidateViewState();
        setError(request, "message", "error.acronymAlreadyExists");
        return mapping.findForward("edit-regularization-motive");
    }

    public ActionForward prepareInsertSchedule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        request.setAttribute("workScheduleTypeFactory", new WorkScheduleTypeFactoryCreator());
        return mapping.findForward("insert-schedule");
    }

    public ActionForward prepareEditSchedule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        Integer scheculeId = new Integer(request.getParameter("idInternal"));
        request.setAttribute("workScheduleTypeFactory", new WorkScheduleTypeFactoryEditor(
                rootDomainObject.readWorkScheduleTypeByOID(scheculeId)));
        return mapping.findForward("insert-schedule");
    } // vefificar que se tem data incio tem de ter data fim

    public ActionForward insertSchedule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        WorkScheduleTypeFactoryCreator workScheduleTypeFactory = (WorkScheduleTypeFactoryCreator) getFactoryObject();
        if (workScheduleTypeFactory == null) {
            setError(request, "timeout", "error.timeout");
            return mapping.findForward("insert-schedule");
        }
        if (validateWorkScheduleAcronym(request, workScheduleTypeFactory)
                && validateWorkScheduleTypeFactory(request, workScheduleTypeFactory)) {
            Object result = executeService(request, "ExecuteFactoryMethod",
                    new Object[] { workScheduleTypeFactory });
            if (result == null) {
                request.setAttribute("workScheduleList", getScheduleList());
                return mapping.findForward("show-all-schedules");
            }
            setError(request, "message", "error.existingEquivalentSchedule", result);
        }
        request.setAttribute("workScheduleTypeFactory", workScheduleTypeFactory);
        return mapping.findForward("insert-schedule");
    }

    public ActionForward editSchedule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        WorkScheduleTypeFactoryEditor workScheduleTypeFactory = (WorkScheduleTypeFactoryEditor) getFactoryObject();
        if (workScheduleTypeFactory == null) {
            setError(request, "timeout", "error.timeout");
            return mapping.findForward("edit-schedule");
        }
        if (validateWorkScheduleTypeFactory(request, workScheduleTypeFactory)) {
            Object result = executeService(request, "ExecuteFactoryMethod",
                    new Object[] { workScheduleTypeFactory });
            if (result == null) {
                request.setAttribute("workScheduleList", getScheduleList());
                return mapping.findForward("show-all-schedules");
            }
            setError(request, "message", (ActionMessage) result);
        }
        request.setAttribute("workScheduleTypeFactory", workScheduleTypeFactory);
        return mapping.findForward("insert-schedule");
    }

    private List<WorkScheduleType> getScheduleList() {
        List<WorkScheduleType> workScheduleList = new ArrayList<WorkScheduleType>();
        for (WorkScheduleType workScheduleType : rootDomainObject.getWorkScheduleTypes()) {
            workScheduleList.add(workScheduleType);
        }
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("ojbConcreteClass"));
        comparatorChain.addComparator(new BeanComparator("acronym"));
        Collections.sort(workScheduleList, comparatorChain);
        return workScheduleList;
    }

    private boolean validateWorkScheduleAcronym(HttpServletRequest request,
            WorkScheduleTypeFactory workScheduleTypeFactory) {
        if (alreadyExistsWorkScheduleTypeAcronym(workScheduleTypeFactory.getAcronym())) {
            setError(request, "validation", "error.acronymAlreadyExists");
            return false;
        }
        return true;
    }

    private boolean validateWorkScheduleTypeFactory(HttpServletRequest request,
            WorkScheduleTypeFactory workScheduleTypeFactory) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
                LanguageUtils.getLocale());

        if (!workScheduleTypeFactory.getBeginValidDate().isBefore(
                workScheduleTypeFactory.getEndValidDate())) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// datas
            // de
            // validade
            return false;
        }
        Interval emptyInterval = new Interval(0, 0);
        Interval dayInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
                .getBeginDayTime(), workScheduleTypeFactory.getEndDayTime(), workScheduleTypeFactory
                .getEndDayNextDay());
        if (dayInterval == null || dayInterval.equals(emptyInterval)) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// horário
            // dia
            return false;
        }

        Interval clockingInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
                .getBeginClockingTime(), workScheduleTypeFactory.getEndClockingTime(),
                workScheduleTypeFactory.getEndClockingNextDay());
        if (clockingInterval == null || clockingInterval.equals(emptyInterval)) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// horário
            // marcações
            return false;
        }

        Interval overlap = clockingInterval.overlap(dayInterval);
        if (!overlap.equals(clockingInterval)) {
            setError(request, "validation", "error.clockingIntervalNotInsideDayInterval");
            return false;
        }

        Interval firstNormalWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(
                workScheduleTypeFactory.getBeginNormalWorkFirstPeriod(), workScheduleTypeFactory
                        .getEndNormalWorkFirstPeriod(), workScheduleTypeFactory
                        .getEndNormalWorkFirstPeriodNextDay());
        if (firstNormalWorkPeriodInterval == null || dayInterval.equals(emptyInterval)) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// firstNormalWorkPeriodInterval
            return false;
        }
        overlap = firstNormalWorkPeriodInterval.overlap(clockingInterval);
        if (!overlap.equals(firstNormalWorkPeriodInterval)) {
            setError(request, "validation", "error.workPeriodNotInsideClockingInterval");
            return false;
        }
        if (firstNormalWorkPeriodInterval.toDuration().isLongerThan(
                WorkScheduleType.maximumContinuousWorkPeriod)) {
            Chronology chronology = GregorianChronology.getInstanceUTC();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
            TimeOfDay time = new TimeOfDay(WorkScheduleType.maximumContinuousWorkPeriod.getMillis(),
                    chronology);
            setError(request, "validation", "error.workPeriodLongerThanMaximumWorkPeriod", fmt
                    .print(time));
            return false;
        }

        Interval secondNormalWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(
                workScheduleTypeFactory.getBeginNormalWorkSecondPeriod(), workScheduleTypeFactory
                        .getEndNormalWorkSecondPeriod(), workScheduleTypeFactory
                        .getEndNormalWorkSecondPeriodNextDay());
        if (secondNormalWorkPeriodInterval == null) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// secondNormalWorkPeriodInterval
            return false;
        }
        if (!secondNormalWorkPeriodInterval.equals(emptyInterval)) {
            overlap = secondNormalWorkPeriodInterval.overlap(clockingInterval);
            if (!overlap.equals(secondNormalWorkPeriodInterval)) {
                setError(request, "validation", "error.workPeriodNotInsideClockingInterval");
                return false;
            }
            if (firstNormalWorkPeriodInterval.getEnd()
                    .isAfter(secondNormalWorkPeriodInterval.getStart())) {
                setError(request, "validation", "error.workPeriodsOverided");
                return false;
            }
            if (secondNormalWorkPeriodInterval.toDuration().isLongerThan(
                    WorkScheduleType.maximumContinuousWorkPeriod)) {
                Chronology chronology = GregorianChronology.getInstanceUTC();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
                TimeOfDay time = new TimeOfDay(WorkScheduleType.maximumContinuousWorkPeriod.getMillis(),
                        chronology);
                setError(request, "validation", "error.workPeriodLongerThanMaximumWorkPeriod", fmt
                        .print(time));
                return false;
            }
        }
        Interval firstFixedWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
                .getBeginFixedWorkFirstPeriod(), workScheduleTypeFactory.getEndFixedWorkFirstPeriod(),
                workScheduleTypeFactory.getEndFixedWorkFirstPeriodNextDay());
        if (firstFixedWorkPeriodInterval == null) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// firstFixedWorkPeriodInterval
            return false;
        }
        if (!firstFixedWorkPeriodInterval.equals(emptyInterval)) {
            overlap = firstFixedWorkPeriodInterval.overlap(firstNormalWorkPeriodInterval);
            if (!overlap.equals(firstFixedWorkPeriodInterval)) {
                setError(request, "validation", "error.fixedWorkPeriodNotInsideNormalWorkPeriod");
                return false;
            }
        }
        Interval secondFixedWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(
                workScheduleTypeFactory.getBeginFixedWorkSecondPeriod(), workScheduleTypeFactory
                        .getEndFixedWorkSecondPeriod(), workScheduleTypeFactory
                        .getEndFixedWorkSecondPeriodNextDay());
        if (secondFixedWorkPeriodInterval == null) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// secondFixedWorkPeriodInterval
            return false;
        }
        if (!secondFixedWorkPeriodInterval.equals(emptyInterval)) {
            overlap = secondFixedWorkPeriodInterval.overlap(secondNormalWorkPeriodInterval);
            if (!overlap.equals(secondFixedWorkPeriodInterval)) {
                setError(request, "validation", "error.fixedWorkPeriodNotInsideNormalWorkPeriod");
                return false;
            }
            if (firstFixedWorkPeriodInterval.getEnd().isAfter(secondFixedWorkPeriodInterval.getStart())) {
                setError(request, "validation", "error.workPeriodsOverided");
                return false;
            }
        }

        Interval mealInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
                .getMealBeginTime(), workScheduleTypeFactory.getMealEndTime(), false);
        if (mealInterval == null) {
            setError(request, "validation", "error.invalidDates", bundle.getString("label.date"));// mealInterval
            return false;
        }
        if (!mealInterval.equals(emptyInterval)) {
            overlap = mealInterval.overlap(clockingInterval);
            if (!overlap.equals(mealInterval)) {
                setError(request, "validation", "error.mealIntervalNotInsideClockingInterval");
                return false;
            }
            if (!firstFixedWorkPeriodInterval.equals(emptyInterval)) {
                if (mealInterval.overlap(firstFixedWorkPeriodInterval) != null) {
                    setError(request, "validation", "error.mealIntervalOverlapsFixedPeriod");
                    return false;
                }
            }
            if (!secondFixedWorkPeriodInterval.equals(emptyInterval)) {
                if (mealInterval.overlap(secondFixedWorkPeriodInterval) != null) {
                    setError(request, "validation", "error.mealIntervalOverlapsFixedPeriod");
                    return false;
                }
            }
        }
        return true;
    }

    private Interval verifyTimeOfDayAndReturnInterval(TimeOfDay beginTime, TimeOfDay endTime,
            Boolean endNextDay) {
        if (beginTime == null && endTime == null) {
            return new Interval(0, 0);
        }
        if (beginTime == null || endTime == null) {
            return null;
        }
        DateTime endDate = endTime.toDateTimeToday();
        if (endNextDay) {
            endDate = endDate.plusDays(1);
        } else {
            if (!beginTime.isBefore(endTime)) {
                return null;
            }
        }
        return new Interval(beginTime.toDateTimeToday(), endDate);
    }

    private boolean alreadyExistsWorkScheduleTypeAcronym(String acronym) {
        for (WorkScheduleType workScheduleType : RootDomainObject.getInstance().getWorkScheduleTypes()) {
            if (workScheduleType.getAcronym().equalsIgnoreCase(acronym)) {
                return true;
            }
        }
        return false;
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }

    private void setError(HttpServletRequest request, String error, String errorMsg, Object arg) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, new ActionMessage(errorMsg, arg));
        saveMessages(request, actionMessages);
    }

    private void setError(HttpServletRequest request, String error, String errorMsg) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, new ActionMessage(errorMsg));
        saveMessages(request, actionMessages);
    }

}