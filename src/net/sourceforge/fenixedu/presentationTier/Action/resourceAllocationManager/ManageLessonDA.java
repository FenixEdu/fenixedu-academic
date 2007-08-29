package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.CreateLesson;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.EditLesson;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupationEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.comparators.RoomAlphabeticComparator;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InterceptingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidTimeIntervalActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.YearMonthDay;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageLessonDA extends FenixLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public static String INVALID_TIME_INTERVAL = "errors.lesson.invalid.time.interval";

    public static String INVALID_WEEKDAY = "errors.lesson.invalid.weekDay";

    public static String UNKNOWN_ERROR = "errors.unknown";

    public ActionForward findInput(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String action = request.getParameter("action");
	if (action != null && action.equals("edit")) {
	    return prepareEdit(mapping, form, request, response);
	}
	return prepareCreate(mapping, form, request, response);

    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	DynaActionForm manageLessonForm = (DynaActionForm) form;

	InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);
	Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());
	GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod = shift.getExecutionCourse().getMaxLessonsPeriod();
		
	if(maxLessonsPeriod != null) {
	    request.setAttribute("executionDegreeLessonsStartDate", maxLessonsPeriod.getLeft().toString("dd/MM/yyyy"));
	    request.setAttribute("executionDegreeLessonsEndDate", maxLessonsPeriod.getRight().toString("dd/MM/yyyy"));
	    manageLessonForm.set("newBeginDate", maxLessonsPeriod.getLeft().toString("dd/MM/yyyy"));
	    manageLessonForm.set("newEndDate", maxLessonsPeriod.getRight().toString("dd/MM/yyyy"));
	    manageLessonForm.set("createLessonInstances", Boolean.TRUE);
	    
	} else {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("error.executionDegree.empty.lessonsPeriod", new ActionError("error.executionDegree.empty.lessonsPeriod"));
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();
	}
	
	return mapping.findForward("ShowLessonForm");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm manageLessonForm = (DynaActionForm) form;

	InfoLesson infoLesson = (InfoLesson) request.getAttribute(SessionConstants.LESSON);

	manageLessonForm.set("diaSemana", infoLesson.getDiaSemana().getDiaSemana().toString());
	manageLessonForm.set("horaInicio", "" + infoLesson.getInicio().get(Calendar.HOUR_OF_DAY));
	manageLessonForm.set("minutosInicio", "" + infoLesson.getInicio().get(Calendar.MINUTE));
	manageLessonForm.set("horaFim", "" + infoLesson.getFim().get(Calendar.HOUR_OF_DAY));
	manageLessonForm.set("minutosFim", "" + infoLesson.getFim().get(Calendar.MINUTE));

	if (infoLesson.getInfoRoomOccupation() != null) {
	    manageLessonForm.set("nomeSala", "" + infoLesson.getInfoRoomOccupation().getInfoRoom().getNome());
	}

	if (infoLesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
	    manageLessonForm.set("quinzenal", Boolean.TRUE);	    
	}

	if(infoLesson.getLessonBegin() != null) {	    
	    manageLessonForm.set("newBeginDate", infoLesson.getLessonBegin().toString("dd/MM/yyyy"));
	}
	if(infoLesson.getLessonEnd() != null) {	    
	    manageLessonForm.set("newEndDate", infoLesson.getLessonEnd().toString("dd/MM/yyyy"));
	}
	
	manageLessonForm.set("createLessonInstances", Boolean.TRUE);
	
	Lesson lesson = rootDomainObject.readLessonByOID(infoLesson.getIdInternal());
	GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod = lesson.getShift().getExecutionCourse().getMaxLessonsPeriod();	
	if(maxLessonsPeriod != null) {
	    request.setAttribute("executionDegreeLessonsStartDate", maxLessonsPeriod.getLeft().toString("dd/MM/yyyy"));
	    request.setAttribute("executionDegreeLessonsEndDate", maxLessonsPeriod.getRight().toString("dd/MM/yyyy"));	
	}
	
	request.setAttribute("action", "edit");
	return mapping.findForward("ShowLessonForm");
    }

    public ActionForward chooseRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm manageLessonForm = (DynaActionForm) form;
	ContextUtils.setExecutionPeriodContext(request);
	
	DiaSemana weekDay = new DiaSemana(new Integer(formDay2EnumerateDay((String) manageLessonForm.get("diaSemana"))));

	Calendar inicio = Calendar.getInstance();
	inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaInicio")));
	inicio.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
	inicio.set(Calendar.SECOND, 0);
	
	Calendar fim = Calendar.getInstance();
	fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaFim")));
	fim.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosFim")));
	fim.set(Calendar.SECOND, 0);

	Boolean quinzenal = (Boolean) manageLessonForm.get("quinzenal");
	if (quinzenal == null) {
	    quinzenal = new Boolean(false);
	}
		
	ActionErrors actionErrors = checkTimeIntervalAndWeekDay(inicio, fim, weekDay);
	
	if (actionErrors.isEmpty()) {

	    FrequencyType frequency = null;
	    if (quinzenal.booleanValue()) {
		frequency = FrequencyType.BIWEEKLY;
	    } else {
		frequency = FrequencyType.WEEKLY;
	    }

	    InfoLesson infoLesson = (InfoLesson) request.getAttribute(SessionConstants.LESSON);
	    InfoShift infoShift = (InfoShift) request.getAttribute(SessionConstants.SHIFT);
	    
	    GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod = null; 	    
	    String action = request.getParameter("action");	    
	    	    
	    if (action != null && action.equals("edit")) {
		Lesson lesson = rootDomainObject.readLessonByOID(infoLesson.getIdInternal());
		maxLessonsPeriod = lesson.getShift().getExecutionCourse().getMaxLessonsPeriod();
	    } else {				
		Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());
		maxLessonsPeriod = shift.getExecutionCourse().getMaxLessonsPeriod();
	    }	    	    
			    	      
	    YearMonthDay lessonNewBeginDate = getDateFromForm(manageLessonForm, "newBeginDate");
	    YearMonthDay lessonEndDate = getDateFromForm(manageLessonForm, "newEndDate");
	    
	    List<InfoRoom> emptyRoomsList = null;	    
	    	    	   
	    if(lessonEndDate != null) {		

		YearMonthDay executionDegreeLessonsBeginDate = maxLessonsPeriod.getLeft();
		YearMonthDay executionDegreeLessonsEndDate = maxLessonsPeriod.getRight();
		
		if(lessonNewBeginDate == null || lessonNewBeginDate.isAfter(lessonEndDate) || lessonNewBeginDate.isBefore(executionDegreeLessonsBeginDate)) {			
		    actionErrors.add("error.Lesson.invalid.new.begin.date", new ActionError("error.Lesson.invalid.new.begin.date"));
		    saveErrors(request, actionErrors);    
		    return mapping.getInputForward();
		}
		
		if(lessonEndDate.isAfter(executionDegreeLessonsEndDate)) {			
		    actionErrors.add("error.Lesson.invalid.new.end.date", new ActionError("error.Lesson.invalid.new.end.date"));
		    saveErrors(request, actionErrors);    
		    return mapping.getInputForward();
		}
		
		Object args[] = { lessonNewBeginDate, lessonEndDate, HourMinuteSecond.fromCalendarFields(inicio), HourMinuteSecond.fromCalendarFields(fim),
			weekDay, null, frequency, Boolean.TRUE };

		emptyRoomsList = (List<InfoRoom>) ServiceUtils.executeService(SessionUtils.getUserView(request), "ReadAvailableRoomsForExam", args);
		
	    } else if (action != null && action.equals("edit")) {
		actionErrors.add("error.Lesson.already.finished", new ActionError("error.Lesson.already.finished"));
		saveErrors(request, actionErrors);
		return mapping.getInputForward();
	    
	    } else {
		actionErrors.add("error.executionDegree.empty.lessonsPeriod", new ActionError("error.executionDegree.empty.lessonsPeriod"));
		saveErrors(request, actionErrors);
		return mapping.getInputForward();
	    }
	    
	    if (emptyRoomsList == null || emptyRoomsList.isEmpty()) {
		actionErrors.add("search.empty.rooms.no.rooms", new ActionError("search.empty.rooms.no.rooms"));
		saveErrors(request, actionErrors);
		return mapping.getInputForward();
	    }
	    	   	    
	    if (action != null && action.equals("edit")
		    && ((InfoLesson) request.getAttribute(SessionConstants.LESSON)).getInfoRoomOccupation() != null) {
				
		emptyRoomsList.add(infoLesson.getInfoRoomOccupation().getInfoRoom());		
		manageLessonForm.set("nomeSala", infoLesson.getInfoRoomOccupation().getInfoRoom().getNome());
	    }
	    	   
	    Collections.sort(emptyRoomsList, new RoomAlphabeticComparator());
	    List<LabelValueBean> listaSalas = new ArrayList<LabelValueBean>();
	    for (int i = 0; i < emptyRoomsList.size(); i++) {
		InfoRoom elem = emptyRoomsList.get(i);
		listaSalas.add(new LabelValueBean(elem.getNome(), elem.getNome()));
	    }
	    listaSalas.add(0, new LabelValueBean("- Sem Sala -", ""));
	    
	    request.setAttribute("action", action);
	    request.setAttribute("listaSalas", listaSalas);
	    request.setAttribute("manageLessonForm", manageLessonForm);

	    return mapping.findForward("ShowChooseRoomForm");
	}
	
	saveErrors(request, actionErrors);
	return mapping.getInputForward();

    }

    public ActionForward createEditLesson(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	DynaActionForm manageLessonForm = (DynaActionForm) form;
	request.setAttribute("manageLessonForm", manageLessonForm);

	ContextUtils.setExecutionPeriodContext(request);
	
	DiaSemana weekDay = new DiaSemana(new Integer(formDay2EnumerateDay((String) manageLessonForm.get("diaSemana"))));	
	YearMonthDay newBeginDate = getDateFromForm(manageLessonForm, "newBeginDate");
	YearMonthDay newEndDate = getDateFromForm(manageLessonForm, "newEndDate");        	             	     
	
	Boolean quinzenal = (Boolean) manageLessonForm.get("quinzenal");
	if (quinzenal == null) {
	    quinzenal = new Boolean(false);
	}

	Calendar inicio = Calendar.getInstance();
	inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaInicio")));
	inicio.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosInicio")));
	inicio.set(Calendar.SECOND, 0);
	inicio.set(Calendar.MILLISECOND, 0);
	
	Calendar fim = Calendar.getInstance();
	fim.setTimeInMillis(inicio.getTimeInMillis());
	fim.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) manageLessonForm.get("horaFim")));
	fim.set(Calendar.MINUTE, Integer.parseInt((String) manageLessonForm.get("minutosFim")));
	fim.set(Calendar.SECOND, 0);
	fim.set(Calendar.MILLISECOND, 0);

	InfoRoom infoSala = null;
	if (!StringUtils.isEmpty((String) manageLessonForm.get("nomeSala"))) {
	    infoSala = new InfoRoom(AllocatableSpace.findAllocatableSpaceForEducationByName((String) manageLessonForm.get("nomeSala")));
	}
	
	ActionErrors actionErrors = checkTimeIntervalAndWeekDay(inicio, fim, weekDay);
	
	if (actionErrors.isEmpty()) {
	  
	    InfoShift infoShift = (InfoShift) (request.getAttribute(SessionConstants.SHIFT));

	    InfoRoomOccupationEditor infoRoomOccupation = null;
	    if (infoSala != null) {
		infoRoomOccupation = new InfoRoomOccupationEditor();
		infoRoomOccupation.setDayOfWeek(weekDay);
		infoRoomOccupation.setEndTime(fim);
		infoRoomOccupation.setStartTime(inicio);
		infoRoomOccupation.setInfoRoom(infoSala);
	    }

	    final FrequencyType frequency;	    	   
	    if (quinzenal.booleanValue()) {		
		frequency = FrequencyType.BIWEEKLY;					
	    } else {
		frequency = FrequencyType.WEEKLY;	    
	    }
	    
	    if (infoRoomOccupation != null) {
		infoRoomOccupation.setFrequency(frequency);
	    }
	    
	    String action = request.getParameter("action");
	    if (action != null && action.equals("edit")) {
		
		InfoLesson infoLessonOld = (InfoLesson) request.getAttribute(SessionConstants.LESSON);
		Boolean createLessonInstances = (Boolean) manageLessonForm.get("createLessonInstances");
		
		try {
		    
		    final Object argsEditLesson[] = { infoLessonOld, weekDay, inicio, fim, frequency, infoRoomOccupation, 
			    infoShift, newBeginDate, newEndDate, createLessonInstances };
		    
		    ServiceUtils.executeService(SessionUtils.getUserView(request), "EditLesson", argsEditLesson);
		    
		} catch (EditLesson.InvalidLoadException ex) {
		    if (ex.getMessage().endsWith("REACHED")) {
			actionErrors.add("errors.shift.hours.limit.reached", new ActionError("errors.shift.hours.limit.reached"));
		    } else {
			actionErrors.add("errors.shift.hours.limit.exceeded", new ActionError("errors.shift.hours.limit.exceeded"));
		    }
		    saveErrors(request, actionErrors);
		    return mapping.getInputForward();
		    
		} catch (ExistingServiceException ex) {
		    actionErrors.add("error.existing.service", new ActionError("error.existing.service"));
		    throw new ExistingActionException("A aula", ex);
		
		} catch (InterceptingServiceException ex) {
		    actionErrors.add("error.intercepting.service", new ActionError("error.intercepting.service"));
		    throw new InterceptingActionException(infoSala.getNome(), ex);
		
		} catch (InvalidTimeIntervalServiceException ex) {
		    actionErrors.add("error.invalid.time.service", new ActionError("error.invalid.time.service"));
		    throw new InvalidTimeIntervalActionException(ex);
		
		} catch (DomainException domainException) {
		    actionErrors.add(domainException.getMessage(), new ActionError(domainException.getMessage(), domainException.getArgs()));
		    saveErrors(request, actionErrors);   
		}

	    } else {				
		try {
		    final Object argsCreateLesson[] = { weekDay, inicio, fim, frequency, infoRoomOccupation, infoShift };		   
		    ServiceUtils.executeService(SessionUtils.getUserView(request), "CreateLesson", argsCreateLesson);
		    
		} catch (CreateLesson.InvalidLoadException ex) {
		    if (ex.getMessage().endsWith("REACHED")) {
			actionErrors.add("errors.shift.hours.limit.reached", new ActionError("errors.shift.hours.limit.reached"));
		    } else {
			actionErrors.add("errors.shift.hours.limit.exceeded", new ActionError("errors.shift.hours.limit.exceeded"));
		    }
		    saveErrors(request, actionErrors);
		    return mapping.getInputForward();
		
		}  catch (DomainException domainException) {
		    actionErrors.add(domainException.getMessage(), new ActionError(domainException.getMessage(), domainException.getArgs()));
		    saveErrors(request, actionErrors);   
		}
	    }

	    return mapping.findForward("EditShift");
	}
	saveErrors(request, actionErrors);
	return mapping.getInputForward();

    }
    
    public ActionForward deleteLesson(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	List<Integer> lessons = new ArrayList<Integer>();
	lessons.add(Integer.valueOf(request.getParameter(SessionConstants.LESSON_OID)));

	final Object argsApagarAula[] = { lessons };

	try {
	    ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteLessons", argsApagarAula);
	    
	} catch (FenixServiceMultipleException e) {
	    final ActionErrors actionErrors = new ActionErrors();
	    for (final DomainException domainException : e.getExceptionList()) {
		actionErrors.add(domainException.getMessage(), new ActionError(domainException.getMessage(), domainException.getArgs()));
	    }
	    saveErrors(request, actionErrors);
	    return mapping.findForward("LessonDeleted");
	}

	request.removeAttribute(SessionConstants.LESSON_OID);
	return mapping.findForward("LessonDeleted");
    }
    
    private YearMonthDay getDateFromForm(DynaActionForm manageLessonForm, String property) {		 
	String newDateString = (String) manageLessonForm.get(property);	
        if(!StringUtils.isEmpty(newDateString)) {
            try {
        	int day = Integer.parseInt(newDateString.substring(0, 2));
        	int month = Integer.parseInt(newDateString.substring(3, 5));
        	int year = Integer.parseInt(newDateString.substring(6, 10));
        	if (year == 0 || month == 0 || day == 0) {
        	    return null;
        	} else {
        	    return new YearMonthDay(year, month, day);    
        	}
	    } catch (NumberFormatException e) {
		return null;
	    }            
        }
        return null;
    }
    
    private String formDay2EnumerateDay(String string) {
	String result = string;
	if (string.equalsIgnoreCase("2")) {
	    result = "2";
	}
	if (string.equalsIgnoreCase("3")) {
	    result = "3";
	}
	if (string.equalsIgnoreCase("4")) {
	    result = "4";
	}
	if (string.equalsIgnoreCase("5")) {
	    result = "5";
	}
	if (string.equalsIgnoreCase("6")) {
	    result = "6";
	}
	if (string.equalsIgnoreCase("S")) {
	    result = "7";
	}
	return result;
    }

    private ActionErrors checkTimeIntervalAndWeekDay(Calendar begining, Calendar end, DiaSemana weekday) {
	
	ActionErrors actionErrors = new ActionErrors();
	String beginMinAppend = "";
	String endMinAppend = "";

	if (begining.get(Calendar.MINUTE) == 0) {
	    beginMinAppend = "0";
	}
	
	if (end.get(Calendar.MINUTE) == 0) {
	    endMinAppend = "0";
	}

	if (begining.getTime().getTime() >= end.getTime().getTime()) {
	    actionErrors.add(INVALID_TIME_INTERVAL, new ActionError(INVALID_TIME_INTERVAL, ""
		    + begining.get(Calendar.HOUR_OF_DAY) + ":" + begining.get(Calendar.MINUTE)
		    + beginMinAppend + " - " + end.get(Calendar.HOUR_OF_DAY) + ":"
		    + end.get(Calendar.MINUTE) + endMinAppend));
	}

	if (weekday.getDiaSemana().intValue() < 1 || weekday.getDiaSemana().intValue() > 7) {
	    actionErrors.add(INVALID_WEEKDAY, new ActionError(INVALID_WEEKDAY, ""));
	}

	return actionErrors;
    }
}