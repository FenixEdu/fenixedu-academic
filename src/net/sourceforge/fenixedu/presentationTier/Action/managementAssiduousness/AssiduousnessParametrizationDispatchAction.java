package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExemptionBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExemption;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory;
import net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory.WorkScheduleTypeFactoryCreator;
import net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory.WorkScheduleTypeFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
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
    public ActionForward showSchedules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	List<WorkScheduleType> workScheduleList = new ArrayList<WorkScheduleType>();
	for (WorkScheduleType workScheduleType : rootDomainObject.getWorkScheduleTypes()) {
	    if (workScheduleType.isValidWorkScheduleType()) {
		workScheduleList.add(workScheduleType);
	    }
	}
	Collections.sort(workScheduleList, new BeanComparator("acronym"));
	request.setAttribute("workScheduleList", workScheduleList);
	return mapping.findForward("show-all-schedules");
    }

    public ActionForward showJustificationMotives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	List<JustificationMotive> justificationMotives = new ArrayList<JustificationMotive>();
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getJustificationType() != null
		    && JustificationType.getJustificationTypesForJustificationMotives().contains(
			    justificationMotive.getJustificationType())) {
		justificationMotives.add(justificationMotive);
	    }
	}
	Collections.sort(justificationMotives, new BeanComparator("justificationType"));
	request.setAttribute("justificationMotives", justificationMotives);
	return mapping.findForward("show-justification-motives");
    }

    public ActionForward showRegularizationMotives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	List<JustificationMotive> regularizationMotives = new ArrayList<JustificationMotive>();
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getJustificationType() == null) {
		regularizationMotives.add(justificationMotive);
	    }
	}
	Collections.sort(regularizationMotives, new BeanComparator("acronym"));
	request.setAttribute("regularizationMotives", regularizationMotives);
	return mapping.findForward("show-regularization-motives");
    }

    public ActionForward prepareInsertJustificationMotive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	return mapping.findForward("edit-justification-motive");
    }

    public ActionForward prepareEditJustificationMotive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	Integer justificationMotiveId = new Integer(request.getParameter("idInternal"));
	JustificationMotive justificationMotive = rootDomainObject.readJustificationMotiveByOID(justificationMotiveId);
	request.setAttribute("justificationMotive", justificationMotive);
	return mapping.findForward("edit-justification-motive");
    }

    public ActionForward sendErrorToEditJustificationMotive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute("justificationMotive", getRenderedObject());
	return mapping.findForward("edit-justification-motive");
    }

    public ActionForward prepareInsertRegularizationMotive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	return mapping.findForward("edit-regularization-motive");
    }

    public ActionForward prepareEditRegularizationMotive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	Integer justificationMotiveId = new Integer(request.getParameter("idInternal"));
	JustificationMotive justificationMotive = rootDomainObject.readJustificationMotiveByOID(justificationMotiveId);
	request.setAttribute("justificationMotive", justificationMotive);
	return mapping.findForward("edit-regularization-motive");
    }

    public ActionForward sendErrorToEditRegularizationMotive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute("justificationMotive", getRenderedObject());
	return mapping.findForward("edit-regularization-motive");
    }

    public ActionForward prepareInsertSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute("workScheduleTypeFactory", new WorkScheduleTypeFactoryCreator());
	return mapping.findForward("insert-schedule");
    }

    public ActionForward prepareEditSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	Integer scheculeId = new Integer(request.getParameter("idInternal"));
	request.setAttribute("workScheduleTypeFactory", new WorkScheduleTypeFactoryEditor(rootDomainObject
		.readWorkScheduleTypeByOID(scheculeId)));
	return mapping.findForward("insert-schedule");
    } // vefificar que se tem data incio tem de ter data fim

    public ActionForward insertSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	WorkScheduleTypeFactoryCreator workScheduleTypeFactory = (WorkScheduleTypeFactoryCreator) getFactoryObject();
	if (workScheduleTypeFactory == null) {
	    setError(request, "timeout", "error.timeout");
	    return mapping.findForward("insert-schedule");
	}
	if (validateWorkScheduleAcronym(request, workScheduleTypeFactory)
		&& validateWorkScheduleTypeFactory(request, workScheduleTypeFactory)) {
	    Object result = executeService(request, "ExecuteFactoryMethod", new Object[] { workScheduleTypeFactory });
	    if (result == null) {
		request.setAttribute("workScheduleList", getScheduleList());
		return mapping.findForward("show-all-schedules");
	    }
	    setError(request, "message", (ActionMessage) result);
	}
	request.setAttribute("workScheduleTypeFactory", workScheduleTypeFactory);
	return mapping.findForward("insert-schedule");
    }

    public ActionForward editSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	WorkScheduleTypeFactoryEditor workScheduleTypeFactory = (WorkScheduleTypeFactoryEditor) getFactoryObject();
	if (workScheduleTypeFactory == null) {
	    setError(request, "timeout", "error.timeout");
	    return mapping.findForward("edit-schedule");
	}
	if (hasWorkScheduleAcronym(request, workScheduleTypeFactory)
		&& validateWorkScheduleTypeFactory(request, workScheduleTypeFactory)) {
	    Object result = executeService(request, "ExecuteFactoryMethod", new Object[] { workScheduleTypeFactory });
	    if (result == null) {
		request.setAttribute("workScheduleList", getScheduleList());
		return mapping.findForward("show-all-schedules");
	    }
	    setError(request, "message", (ActionMessage) result);
	}
	request.setAttribute("workScheduleTypeFactory", workScheduleTypeFactory);
	return mapping.findForward("insert-schedule");
    }

    public ActionForward cancelSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("workScheduleList", getScheduleList());
	return mapping.findForward("show-all-schedules");
    }

    private List<WorkScheduleType> getScheduleList() {
	List<WorkScheduleType> workScheduleList = new ArrayList<WorkScheduleType>();
	for (WorkScheduleType workScheduleType : rootDomainObject.getWorkScheduleTypes()) {
	    if (workScheduleType.isValidWorkScheduleType()) {
		workScheduleList.add(workScheduleType);
	    }
	}
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("class.name"));
	comparatorChain.addComparator(new BeanComparator("acronym"));
	Collections.sort(workScheduleList, comparatorChain);
	return workScheduleList;
    }

    private boolean hasWorkScheduleAcronym(HttpServletRequest request, WorkScheduleTypeFactory workScheduleTypeFactory) {
	if (StringUtils.isEmpty(workScheduleTypeFactory.getAcronym())) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
	    setError(request, "validation", "errors.required", bundle.getString("label.acronym"));
	    return false;
	}
	return true;
    }

    private boolean validateWorkScheduleAcronym(HttpServletRequest request, WorkScheduleTypeFactory workScheduleTypeFactory) {
	if (StringUtils.isEmpty(workScheduleTypeFactory.getAcronym())) {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
	    setError(request, "validation", "errors.required", bundle.getString("label.acronym"));
	    return false;
	}
	if (alreadyExistsWorkScheduleTypeAcronym(workScheduleTypeFactory.getAcronym())) {
	    setError(request, "validation", "error.acronymAlreadyExists");
	    return false;
	}
	return true;
    }

    private boolean validateWorkScheduleTypeFactory(HttpServletRequest request, WorkScheduleTypeFactory workScheduleTypeFactory) {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
	if (workScheduleTypeFactory.getBeginValidDate() == null
		|| (workScheduleTypeFactory.getEndValidDate() != null && !workScheduleTypeFactory.getBeginValidDate().isBefore(
			workScheduleTypeFactory.getEndValidDate()))) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.validity"));
	    return false;
	}
	Interval emptyInterval = new Interval(0, 0);
	Interval dayInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory.getBeginDayTime(),
		workScheduleTypeFactory.getEndDayTime(), workScheduleTypeFactory.getEndDayNextDay());
	if (dayInterval == null || dayInterval.equals(emptyInterval)) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.dayTimeSchedule"));
	    return false;
	}

	Interval clockingInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory.getBeginClockingTime(),
		workScheduleTypeFactory.getEndClockingTime(), workScheduleTypeFactory.getEndClockingNextDay());
	if (clockingInterval == null || clockingInterval.equals(emptyInterval)) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.clockingTimeSchedule"));
	    return false;
	}

	Interval overlap = clockingInterval.overlap(dayInterval);
	if (!overlap.equals(clockingInterval)) {
	    setError(request, "validation", "error.clockingIntervalNotInsideDayInterval");
	    return false;
	}

	Interval firstNormalWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
		.getBeginNormalWorkFirstPeriod(), workScheduleTypeFactory.getEndNormalWorkFirstPeriod(), workScheduleTypeFactory
		.getEndNormalWorkFirstPeriodNextDay());
	if (firstNormalWorkPeriodInterval == null || firstNormalWorkPeriodInterval.equals(emptyInterval)) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.normalFirstWorkPeriod"));
	    return false;
	}
	overlap = firstNormalWorkPeriodInterval.overlap(clockingInterval);
	if (!overlap.equals(firstNormalWorkPeriodInterval)) {
	    setError(request, "validation", "error.workPeriodNotInsideClockingInterval");
	    return false;
	}

	Interval secondNormalWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
		.getBeginNormalWorkSecondPeriod(), workScheduleTypeFactory.getEndNormalWorkSecondPeriod(),
		workScheduleTypeFactory.getEndNormalWorkSecondPeriodNextDay());

	if (firstNormalWorkPeriodInterval.toDuration().isLongerThan(WorkScheduleType.maximumContinuousWorkPeriod)
		&& (secondNormalWorkPeriodInterval != null && !secondNormalWorkPeriodInterval.equals(emptyInterval))) {
	    Chronology chronology = GregorianChronology.getInstanceUTC();
	    DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
	    TimeOfDay time = new TimeOfDay(WorkScheduleType.maximumContinuousWorkPeriod.getMillis(), chronology);
	    setError(request, "validation", "error.workPeriodLongerThanMaximumWorkPeriod", fmt.print(time));
	    return false;
	}

	if (secondNormalWorkPeriodInterval == null) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.normalSecondWorkPeriod"));
	    return false;
	}
	if (!secondNormalWorkPeriodInterval.equals(emptyInterval)) {
	    overlap = secondNormalWorkPeriodInterval.overlap(clockingInterval);
	    if (!overlap.equals(secondNormalWorkPeriodInterval)) {
		setError(request, "validation", "error.workPeriodNotInsideClockingInterval");
		return false;
	    }
	    if (firstNormalWorkPeriodInterval.getEnd().isAfter(secondNormalWorkPeriodInterval.getStart())) {
		setError(request, "validation", "error.workPeriodsOverided");
		return false;
	    }
	    if (secondNormalWorkPeriodInterval.toDuration().isLongerThan(WorkScheduleType.maximumContinuousWorkPeriod)) {
		Chronology chronology = GregorianChronology.getInstanceUTC();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
		TimeOfDay time = new TimeOfDay(WorkScheduleType.maximumContinuousWorkPeriod.getMillis(), chronology);
		setError(request, "validation", "error.workPeriodLongerThanMaximumWorkPeriod", fmt.print(time));
		return false;
	    }
	}
	Interval firstFixedWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
		.getBeginFixedWorkFirstPeriod(), workScheduleTypeFactory.getEndFixedWorkFirstPeriod(), workScheduleTypeFactory
		.getEndFixedWorkFirstPeriodNextDay());
	if (firstFixedWorkPeriodInterval == null) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.fixedFirstWorkPeriod"));
	    return false;
	}
	if (!firstFixedWorkPeriodInterval.equals(emptyInterval)) {
	    overlap = firstFixedWorkPeriodInterval.overlap(firstNormalWorkPeriodInterval);
	    if (!overlap.equals(firstFixedWorkPeriodInterval)) {
		setError(request, "validation", "error.fixedWorkPeriodNotInsideNormalWorkPeriod");
		return false;
	    }
	}
	Interval secondFixedWorkPeriodInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory
		.getBeginFixedWorkSecondPeriod(), workScheduleTypeFactory.getEndFixedWorkSecondPeriod(), workScheduleTypeFactory
		.getEndFixedWorkSecondPeriodNextDay());
	if (secondFixedWorkPeriodInterval == null) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.fixedSecondWorkPeriod"));
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

	Interval mealInterval = verifyTimeOfDayAndReturnInterval(workScheduleTypeFactory.getMealBeginTime(),
		workScheduleTypeFactory.getMealEndTime(), false);
	if (mealInterval == null) {
	    setError(request, "validation", "error.invalidDates", bundle.getString("label.mealPeriod"));
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

    public ActionForward showAssiduousnessExemptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	List<AssiduousnessExemption> assiduousnessExemptions = new ArrayList<AssiduousnessExemption>(rootDomainObject
		.getAssiduousnessExemptions());
	Collections.sort(assiduousnessExemptions, new BeanComparator("year"));
	request.setAttribute("assiduousnessExemptions", assiduousnessExemptions);
	return mapping.findForward("show-assiduousness-exemptions");
    }

    public ActionForward deleteAssiduousnessExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	Integer assiduousnessExemptionId = new Integer(request.getParameter("idInternal"));
	AssiduousnessExemption assiduousnessExemption = rootDomainObject
		.readAssiduousnessExemptionByOID(assiduousnessExemptionId);
	ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteAssiduousnessExemption",
		new Object[] { assiduousnessExemption });

	return showAssiduousnessExemptions(mapping, form, request, response);
    }

    public ActionForward insertAssiduousnessExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	AssiduousnessExemptionBean assiduousnessExemptionBean = (AssiduousnessExemptionBean) getFactoryObject();
	if (assiduousnessExemptionBean == null) {
	    assiduousnessExemptionBean = new AssiduousnessExemptionBean();
	} else {
	    ActionMessage actionMessage = (ActionMessage) executeService(request, "ExecuteFactoryMethod",
		    new Object[] { assiduousnessExemptionBean });
	    if (actionMessage == null) {
		return showAssiduousnessExemptions(mapping, form, request, response);
	    } else {
		setError(request, "message", actionMessage);
	    }
	}
	request.setAttribute("assiduousnessExemptionBean", assiduousnessExemptionBean);
	return mapping.findForward("edit-assiduousness-exemption");
    }

    public ActionForward insertAssiduousnessExemptionShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	AssiduousnessExemptionBean assiduousnessExemptionBean = (AssiduousnessExemptionBean) getFactoryObject();
	if (assiduousnessExemptionBean == null) {
	    assiduousnessExemptionBean = new AssiduousnessExemptionBean();
	} else {
	    assiduousnessExemptionBean.addAssiduousnessExemptionShifts();
	}
	request.setAttribute("assiduousnessExemptionBean", assiduousnessExemptionBean);
	return mapping.findForward("edit-assiduousness-exemption");
    }

    public ActionForward removeAssiduousnessExemptionShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	AssiduousnessExemptionBean assiduousnessExemptionBean = (AssiduousnessExemptionBean) getFactoryObject();
	Integer rowIndex = new Integer(request.getParameter("rowIndex"));
	if (assiduousnessExemptionBean == null) {
	    assiduousnessExemptionBean = new AssiduousnessExemptionBean();
	} else {
	    assiduousnessExemptionBean.removeAssiduousnessExemptionShifts(rowIndex);
	}
	request.setAttribute("assiduousnessExemptionBean", assiduousnessExemptionBean);
	return mapping.findForward("edit-assiduousness-exemption");
    }

    public ActionForward prepareEditAssiduousnessExemption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	Integer assiduousnessExemptionId = new Integer(request.getParameter("idInternal"));
	AssiduousnessExemption assiduousnessExemption = rootDomainObject
		.readAssiduousnessExemptionByOID(assiduousnessExemptionId);
	AssiduousnessExemptionBean assiduousnessExemptionBean = new AssiduousnessExemptionBean(assiduousnessExemption);
	request.setAttribute("assiduousnessExemptionBean", assiduousnessExemptionBean);
	return mapping.findForward("edit-assiduousness-exemption");
    }

    private Interval verifyTimeOfDayAndReturnInterval(TimeOfDay beginTime, TimeOfDay endTime, Boolean endNextDay) {
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