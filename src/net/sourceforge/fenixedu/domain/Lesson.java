package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Manuel Pinto
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.date.TimePeriod;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class Lesson extends Lesson_Base {

    public static final Comparator<Lesson> LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME = new ComparatorChain();
    static {
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator("diaSemana.diaSemana"));
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator("beginHourMinuteSecond"));
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public Lesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo, Shift shift, 
	    Integer weekOfQuinzenalStart, FrequencyType frequency, ExecutionPeriod executionPeriod,
	    OccupationPeriod period) {

	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDiaSemana(diaSemana);
	setInicio(inicio);
	setFim(fim);
	setTipo(tipo);	
	setShift(shift);
	setFrequency(frequency);
	setWeekOfQuinzenalStart(weekOfQuinzenalStart);
	setExecutionPeriod(executionPeriod);
	setPeriod(period);
    }

    public void edit(YearMonthDay newBeginDate, DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo,
	    FrequencyType frequency, Integer weekOfQuinzenalStart) {			

	if(newBeginDate == null) {
	    throw new DomainException("error.Lesson.empty.new.begin.date");
	}

	if(!hasPeriod()) {
	    throw new DomainException("error.Lesson.already.finished");
	}

	removeExistentInstancesWithoutSummaryAfterOrEqual(newBeginDate);	
	refreshPeriodAndInstances(newBeginDate);

	setDiaSemana(diaSemana);
	setInicio(inicio);
	setFim(fim);
	setTipo(tipo);	
	setFrequency(frequency);
	setWeekOfQuinzenalStart(weekOfQuinzenalStart);	
    }

    public void delete() {

	if (getShift().hasAnyAssociatedStudentGroups()) {
	    throw new DomainException("error.deleteLesson.with.Shift.with.studentGroups", prettyPrint());
	}

	if (getShift().hasAnyStudents()) {
	    throw new DomainException("error.deleteLesson.with.Shift.with.students", prettyPrint());
	}

	if (hasAnyAssociatedSummaries()) {
	    throw new DomainException("error.deleteLesson.with.summaries", prettyPrint());
	}

	final OccupationPeriod period = getPeriod();        
	super.setPeriod(null);
	if (period != null) {
	    period.delete();
	}	

	if(hasLessonSpaceOccupation()) {
	    getLessonSpaceOccupation().delete();
	}

	while(hasAnyLessonInstances()) {
	    getLessonInstances().get(0).delete();
	}

	removeExecutionPeriod();	
	removeShift();	
	removeRootDomainObject();
	deleteDomainObject();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return (!hasLessonSpaceOccupation() && hasAnyLessonInstances() || hasPeriod())
	&& getFrequency() != null && getTipo() != null && getDiaSemana() != null	
	&& hasExecutionPeriod() && hasShift();		 
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkTimeInterval() {
	final HourMinuteSecond start = getBeginHourMinuteSecond();
	final HourMinuteSecond end = getEndHourMinuteSecond();	
	return start != null && end != null && start.isBefore(end);
    }

    public AllocatableSpace getSala() {	
	if(hasLessonSpaceOccupation()) {
	    return getLessonSpaceOccupation().getRoom();
	} else if(hasAnyLessonInstances()){
	    return getLastLessonInstance().getRoom();
	}		
	return null;
    }

    public boolean hasSala() {
	return getSala() != null;
    }

    public void refreshPeriodAndInstances(YearMonthDay newBeginDate) {

	if(newBeginDate != null && hasPeriod()) {

	    OccupationPeriod currentPeriod = getPeriod();	    

	    if(newBeginDate.isAfter(currentPeriod.getStartYearMonthDay())) {

		SortedSet<YearMonthDay> allLessonDatesBeforeRefreshPeriod = getAllLessonDatesUntil(newBeginDate.minusDays(1));

		if(!currentPeriod.getEndYearMonthDay().isBefore(newBeginDate)) {								
		    setPeriod(getNewNestedPeriods(currentPeriod, newBeginDate));			

		} else if (currentPeriod.hasNextPeriod()){				

		    OccupationPeriod currentNextPeriod = currentPeriod.getNextPeriod();		

		    if(!currentNextPeriod.getStartYearMonthDay().isBefore(newBeginDate)) {		
			setPeriod(getNewNestedPeriods(currentNextPeriod, currentNextPeriod.getStartYearMonthDay()));		    		

		    } else if(!currentNextPeriod.getEndYearMonthDay().isBefore(newBeginDate)) {
			setPeriod(getNewNestedPeriods(currentNextPeriod, newBeginDate));    

		    } else {
			if(hasLessonSpaceOccupation()) {
			    getLessonSpaceOccupation().delete();
			}
			setPeriod(null);
		    }

		} else {		
		    if(hasLessonSpaceOccupation()) {
			getLessonSpaceOccupation().delete();
		    }
		    setPeriod(null);
		}

		currentPeriod.delete();	    
		createAllLessonInstancesUntil(allLessonDatesBeforeRefreshPeriod, newBeginDate.minusDays(1));
	    }
	}
    } 

    private void createAllLessonInstancesUntil(SortedSet<YearMonthDay> allLessonDatesBeforeRefreshPeriod, YearMonthDay day) {

	if(allLessonDatesBeforeRefreshPeriod != null && day != null) {

	    List<LessonInstance> allLessonInstancesUntil = getAllLessonInstancesUntil(day);	    
	    for (LessonInstance lessonInstance : allLessonInstancesUntil) {
		allLessonDatesBeforeRefreshPeriod.remove(lessonInstance.getDay());
	    }

	    Campus lessonCampus = getLessonCampus();
	    for (YearMonthDay dateToCreate : allLessonDatesBeforeRefreshPeriod) {
		if(!Holiday.isHoliday(dateToCreate, lessonCampus) ) {
		    new LessonInstance(this, getSala(), dateToCreate);
		}
	    }	    	   
	}
    }   

    private void removeExistentInstancesWithoutSummaryAfterOrEqual(YearMonthDay newBeginDate) {		
	if(newBeginDate != null ) {
	    Map<Boolean, List<LessonInstance>> instances = getLessonInstancesWithOrWithoutSummaryAfterOrEqual(newBeginDate);
	    if(instances.get(Boolean.TRUE).isEmpty()) {
		List<LessonInstance> instancesWithoutSummary = instances.get(Boolean.FALSE);
		for (Iterator<LessonInstance> iter = instancesWithoutSummary.iterator(); iter.hasNext();) {
		    LessonInstance instance = iter.next();
		    iter.remove();
		    instance.delete();
		}
	    } else {
		throw new DomainException("error.invalid.new.begin.date");
	    }
	}
    }

    public Calendar getInicio() {
	if (this.getBegin() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getBegin());
	    return result;
	}
	return null;
    }

    public void setInicio(Calendar inicio) {
	if (inicio != null) {
	    this.setBegin(inicio.getTime());
	} else {
	    this.setBegin(null);
	}
    }

    public Calendar getFim() {
	if (this.getEnd() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnd());
	    return result;
	}
	return null;
    }

    public void setFim(Calendar fim) {
	if (fim != null) {
	    this.setEnd(fim.getTime());
	} else {
	    this.setEnd(null);
	}
    }

    public LessonSpaceOccupation getRoomOccupation() {
	return getLessonSpaceOccupation();
    }

    public double hours() {
	TimePeriod timePeriod = new TimePeriod(this.getInicio(), this.getFim());
	return timePeriod.hours().doubleValue();
    }

    public String getInicioString() {
	return String.valueOf(getInicio().get(Calendar.HOUR_OF_DAY));
    }

    public double hoursAfter(int hour) {

	final Calendar start = this.getInicio();
	final Calendar end = this.getFim();

	final Calendar specifiedHour = Calendar.getInstance();
	specifiedHour.setTime(this.getEnd());
	specifiedHour.set(Calendar.HOUR_OF_DAY, hour);
	specifiedHour.set(Calendar.MINUTE, 0);
	specifiedHour.set(Calendar.SECOND, 0);
	specifiedHour.set(Calendar.MILLISECOND, 0);

	if (!start.before(specifiedHour)) {
	    TimePeriod timePeriod = new TimePeriod(start, end);
	    return timePeriod.hours().doubleValue();

	} else if (end.after(specifiedHour)) {
	    TimePeriod timePeriod = new TimePeriod(specifiedHour, end);
	    return timePeriod.hours().doubleValue();
	}

	return 0.0;
    }

    public Summary getSummaryByDate(YearMonthDay date) {
	for (Summary summary : getAssociatedSummaries()) {
	    if (summary.getSummaryDateYearMonthDay().isEqual(date)) {
		return summary;
	    }
	}
	return null;
    }

    public List<Summary> getAssociatedSummaries() {
	List<Summary> result = new ArrayList<Summary>();
	List<LessonInstance> lessonInstances = getLessonInstances();
	for (LessonInstance lessonInstance : lessonInstances) {
	    if(lessonInstance.hasSummary()) {
		result.add(lessonInstance.getSummary());
	    }
	}
	return result;
    }

    public boolean hasAnyAssociatedSummaries() {
	return !getAssociatedSummaries().isEmpty();
    }

    public SortedSet<Summary> getSummariesSortedByDate() {
	return getSummaries(new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
    }

    public SortedSet<Summary> getSummariesSortedByNewestDate() {
	return getSummaries(Summary.COMPARATOR_BY_DATE_AND_HOUR);
    }

    public boolean isTimeValidToInsertSummary(HourMinuteSecond timeToInsert, YearMonthDay summaryDate) {	

	YearMonthDay currentDate = new YearMonthDay();	
	if(summaryDate == null || summaryDate.isAfter(currentDate)) {
	    return false;
	}

	if(currentDate.isEqual(summaryDate)) {	    	    
	    HourMinuteSecond lessonEndTime = null;
	    LessonInstance lessonInstance = getLessonInstanceFor(summaryDate);	
	    if(lessonInstance != null) {
		lessonEndTime = lessonInstance.getEndTime();
	    } else {
		lessonEndTime = getEndHourMinuteSecond();	    
	    }
	    return !lessonEndTime.isAfter(timeToInsert);
	}

	return true;
    }

    public boolean isDateValidToInsertSummary(YearMonthDay date) {	
	YearMonthDay currentDate = new YearMonthDay();	
	SortedSet<YearMonthDay> allLessonDatesEvenToday = getAllLessonDatesUntil(currentDate);
	return (allLessonDatesEvenToday.isEmpty() || date == null) ? false : allLessonDatesEvenToday.contains(date);
    }

    private YearMonthDay getLessonStartDay() {	
	if(hasPeriod()) {
	    YearMonthDay periodStart = getPeriod().getStartYearMonthDay();		
	    int weekOfQuinzenalStart = (getWeekOfQuinzenalStart() != null) ? getWeekOfQuinzenalStart().intValue() - 1: 0;
	    YearMonthDay lessonStart = periodStart.plusDays(7 * weekOfQuinzenalStart);	
	    int lessonStartDayOfWeek = lessonStart.toDateTimeAtMidnight().getDayOfWeek();
	    return lessonStart.plusDays(getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat() - lessonStartDayOfWeek);
	}	
	return null;	
    }

    private YearMonthDay getLessonEndDay() {	
	if(hasPeriod()) {
	    YearMonthDay periodEnd = getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay();		  	
	    int lessonEndDayOfWeek = periodEnd.toDateTimeAtMidnight().getDayOfWeek();
	    return periodEnd.minusDays(lessonEndDayOfWeek - getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat());    
	} 
	return null;	
    }

    private Campus getLessonCampus() {	
	if(hasPeriod()) {
	    return hasSala() ? getSala().getSpaceCampus() : null;
	} else {
	    LessonInstance lastLessonInstance = getLastLessonInstance();
	    return lastLessonInstance != null && lastLessonInstance.getRoom() != null ?
		    lastLessonInstance.getRoom().getSpaceCampus() : null;
	}	
    }

    public YearMonthDay getNextPossibleSummaryDate() {

	YearMonthDay currentDate = new YearMonthDay();
	Summary lastSummary = getLastSummary();

	if (lastSummary != null) {

	    boolean foundDate = false;
	    HourMinuteSecond now = new HourMinuteSecond();	    
	    YearMonthDay lastSummaryDate = lastSummary.getSummaryDateYearMonthDay();	    	
	    SortedSet<YearMonthDay> datesEvenToday = getAllLessonDatesUntil(currentDate);

	    for (YearMonthDay lessonDate : datesEvenToday) {
		if(foundDate) {
		    if(isTimeValidToInsertSummary(now, lessonDate)) {
			return lessonDate;
		    } 
		    break;
		}
		if(lessonDate.isEqual(lastSummaryDate)) {
		    foundDate = true;
		}
	    }

	} else {	
	    YearMonthDay nextPossibleDate = null;
	    if(hasAnyLessonInstances()) {
		nextPossibleDate = getFirstLessonInstance().getDay();
	    } else {
		nextPossibleDate = getLessonStartDay();
	    }	    
	    return (nextPossibleDate.isAfter(currentDate)) ? null : nextPossibleDate;
	}

	return null;
    }

    public SortedSet<YearMonthDay> getAllPossibleDatesToInsertSummary() {	

	HourMinuteSecond now = new HourMinuteSecond();
	YearMonthDay currentDate = new YearMonthDay();
	SortedSet<YearMonthDay> datesToInsert = getAllLessonDatesUntil(currentDate);	

	for (Summary summary : getAssociatedSummaries()) {
	    YearMonthDay summaryDate = summary.getSummaryDateYearMonthDay();
	    datesToInsert.remove(summaryDate);
	}	

	for (Iterator<YearMonthDay> iter = datesToInsert.iterator(); iter.hasNext();) {
	    YearMonthDay date = iter.next();
	    if(!isTimeValidToInsertSummary(now, date)) {
		iter.remove();
	    }
	}	

	return datesToInsert;
    }    

    public SortedSet<YearMonthDay> getAllLessonDatesWithoutInstanceDates() {
	SortedSet<YearMonthDay> dates = new TreeSet<YearMonthDay>();	
	if(hasPeriod()) {	   	    
	    YearMonthDay endDateToSearch = getLessonEndDay();
	    dates.addAll(getAllValidLessonDatesUntil(endDateToSearch));
	}	
	return dates;
    }

    public SortedSet<YearMonthDay> getAllLessonDates() {
	SortedSet<YearMonthDay> dates = getAllLessonInstanceDates();	
	if(hasPeriod()) {	   	    
	    YearMonthDay endDateToSearch = getLessonEndDay();
	    dates.addAll(getAllValidLessonDatesUntil(endDateToSearch));
	}	
	return dates;
    }

    private SortedSet<YearMonthDay> getAllLessonDatesUntil(YearMonthDay day) {			
	SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();				

	if(day != null) {	  	    

	    result.addAll(getAllLessonInstanceDatesUntil(day));

	    if(hasPeriod() && !getLessonStartDay().isAfter(day)) {
		YearMonthDay lessonEndDay = getLessonEndDay();	
		YearMonthDay endDateToSearch = (lessonEndDay.isAfter(day)) ? day : lessonEndDay;		    
		result.addAll(getAllValidLessonDatesUntil(endDateToSearch));
	    }	    	   	    
	}
	return result;
    }

    public SortedSet<YearMonthDay> getAllLessonInstanceDates(){
	SortedSet<YearMonthDay> dates = new TreeSet<YearMonthDay>();	
	for (LessonInstance instance: getLessonInstances()) {	    
	    dates.add(instance.getDay());	    
	}
	return dates;
    }

    public List<LessonInstance> getAllLessonInstancesUntil(YearMonthDay day){
	List<LessonInstance> result = new ArrayList<LessonInstance>();	
	if (day != null) {
	    for (LessonInstance instance: getLessonInstances()) {	    
		if(!instance.getDay().isAfter(day)) {
		    result.add(instance);
		}
	    }
	}
	return result;
    }  

    public SortedSet<YearMonthDay> getAllLessonInstanceDatesUntil(YearMonthDay day) {
	SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();
	if (day != null) {	    			
	    for (LessonInstance instance: getLessonInstances()) {
		YearMonthDay instanceDay = instance.getDay();
		if(!instanceDay.isAfter(day)) {
		    result.add(instanceDay);
		}
	    }
	}
	return result;
    }  

    private SortedSet<YearMonthDay> getAllValidLessonDatesUntil(YearMonthDay endDateToSearch) {			

	SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();	
	YearMonthDay startDateToSearch = getLessonStartDay();

	if (hasPeriod() && !startDateToSearch.isAfter(endDateToSearch)) {	    		    	    

	    Campus lessonCampus = getLessonCampus();	    
	    while (true) {
		if (!Holiday.isHoliday(startDateToSearch, lessonCampus) 
			&& getPeriod().nestedOccupationPeriodsContainsDay(startDateToSearch)) {
		    result.add(startDateToSearch);
		}		
		startDateToSearch = startDateToSearch.plusDays(getNumberOfDaysToSumBetweenTwoLessons());
		if (startDateToSearch.isAfter(endDateToSearch)) {
		    break;
		}
	    }
	}	
	return result;
    }

    private Summary getLastSummary() {
	SortedSet<Summary> summaries = getSummariesSortedByNewestDate();
	return (summaries.isEmpty()) ? null : summaries.first();
    }

    private SortedSet<Summary> getSummaries(Comparator<Summary> comparator) {
	SortedSet<Summary> lessonSummaries = new TreeSet<Summary>(comparator);
	lessonSummaries.addAll(getAssociatedSummaries());
	return lessonSummaries;
    }

    private int getNumberOfDaysToSumBetweenTwoLessons() {
	return getFrequency().getNumberOfDays();	
    }

    public LessonInstance getLessonInstanceFor(YearMonthDay date) {
	List<LessonInstance> lessonInstances = getLessonInstances();
	for (LessonInstance lessonInstance : lessonInstances) {
	    if(lessonInstance.getDay().isEqual(date)) {
		return lessonInstance;
	    }
	}
	return null;
    }

    private Map<Boolean, List<LessonInstance>> getLessonInstancesWithOrWithoutSummaryAfterOrEqual(YearMonthDay afterDate){
	Map<Boolean, List<LessonInstance>> result = new HashMap<Boolean, List<LessonInstance>>();

	result.put(Boolean.TRUE, new ArrayList<LessonInstance>());
	result.put(Boolean.FALSE, new ArrayList<LessonInstance>());

	List<LessonInstance> lessonInstances = getLessonInstances();
	for (LessonInstance lessonInstance : lessonInstances) {
	    if(lessonInstance.hasSummary() && !lessonInstance.getDay().isBefore(afterDate)) {
		List<LessonInstance> list = result.get(Boolean.TRUE);
		list.add(lessonInstance);
	    } else if(!lessonInstance.hasSummary() && !lessonInstance.getDay().isBefore(afterDate)) {
		List<LessonInstance> list = result.get(Boolean.FALSE);
		list.add(lessonInstance);
	    }
	}	

	return result;
    }

    public LessonInstance getLastLessonInstance() {
	SortedSet<LessonInstance> result = new TreeSet<LessonInstance>(LessonInstance.COMPARATOR_BY_BEGIN_DATE_TIME);	
	result.addAll(getLessonInstances());
	return !result.isEmpty() ? result.last() : null;
    }

    public LessonInstance getFirstLessonInstance() {
	SortedSet<LessonInstance> result = new TreeSet<LessonInstance>(LessonInstance.COMPARATOR_BY_BEGIN_DATE_TIME);
	result.addAll(getLessonInstances());
	return !result.isEmpty() ? result.first() : null;
    }

    private OccupationPeriod getNewNestedPeriods(OccupationPeriod currentPeriod, YearMonthDay newBeginDate) {
	OccupationPeriod newPeriod = new OccupationPeriod(newBeginDate, currentPeriod.getEndYearMonthDay());		
	while(currentPeriod.hasNextPeriod()) {
	    OccupationPeriod currentNextPeriod = currentPeriod.getNextPeriod();
	    OccupationPeriod newNextPeriod = new OccupationPeriod(currentNextPeriod.getStartYearMonthDay(), currentNextPeriod.getEndYearMonthDay());
	    newPeriod.setNextPeriod(newNextPeriod);
	    currentPeriod = currentPeriod.getNextPeriod();
	}	
	return newPeriod;
    }


    public boolean contains(Interval interval) {
	return contains(interval, getAllLessonDates());
    }

    public boolean containsWithoutCheckInstanceDates(Interval interval) {
	return contains(interval, getAllLessonDatesWithoutInstanceDates());
    }

    private boolean contains(Interval interval, SortedSet<YearMonthDay> allLessonDates) {

	YearMonthDay intervalStartDate = interval.getStart().toYearMonthDay();
	YearMonthDay intervalEndDate = interval.getEnd().toYearMonthDay();

	HourMinuteSecond intervalBegin = new HourMinuteSecond(interval.getStart().getHourOfDay(), interval.getStart().getMinuteOfHour(), interval.getStart().getSecondOfMinute());
	HourMinuteSecond intervalEnd = new HourMinuteSecond(interval.getEnd().getHourOfDay(), interval.getEnd().getMinuteOfHour(), interval.getEnd().getSecondOfMinute());

	for (YearMonthDay day : allLessonDates) {	    
	    if(intervalStartDate.isEqual(intervalEndDate)){
		if(day.isEqual(intervalStartDate) 
			&& !intervalBegin.isAfter(getEndHourMinuteSecond()) 
			&& !intervalEnd.isBefore(getBeginHourMinuteSecond())){
		    return true;
		}		
	    } else {
		if((day.isAfter(intervalStartDate) && day.isBefore(intervalEndDate)) 
			|| day.isEqual(intervalStartDate) && !getEndHourMinuteSecond().isBefore(intervalBegin)
			|| (day.isEqual(intervalEndDate) && !getBeginHourMinuteSecond().isAfter(intervalEnd))){
		    return true;
		}
	    }	      
	}	    	    	        	        	        	     	
	return false;
    }   

    public String prettyPrint() {
	final StringBuilder result = new StringBuilder();
	result.append(getDiaSemana().getDiaSemanaString());
	result.append(", ");
	result.append(getInicio().get(Calendar.HOUR_OF_DAY));
	result.append(":");
	result.append(getInicio().get(Calendar.MINUTE));
	result.append(", ");
	result.append(hasSala() ? ((AllocatableSpace)getSala()).getName() : "");
	return result.toString();
    }        
}
