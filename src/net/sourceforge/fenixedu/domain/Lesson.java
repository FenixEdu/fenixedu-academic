package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Manuel Pinto
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.YearMonthDay;

public class Lesson extends Lesson_Base {

    public static int NUMBER_OF_MINUTES_IN_HOUR = 60;
    public static final Comparator<Lesson> LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME = new ComparatorChain();
    static {
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator("diaSemana.diaSemana"));
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator("beginHourMinuteSecond"));
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public Lesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, Shift shift, FrequencyType frequency, ExecutionPeriod executionPeriod) {
	super();
	
	OccupationPeriod period = null;
	if(shift != null) {
	    GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod = shift.getExecutionCourse().getMaxLessonsPeriod();
	    period = OccupationPeriod.readOccupationPeriod(maxLessonsPeriod.getLeft(), maxLessonsPeriod.getRight());
	    if(period == null) {
		period = new OccupationPeriod(maxLessonsPeriod.getLeft(), maxLessonsPeriod.getRight());
	    }	    
	}

	setRootDomainObject(RootDomainObject.getInstance());
	setDiaSemana(diaSemana);
	setInicio(inicio);
	setFim(fim);	
	setShift(shift);
	setFrequency(frequency);	
	setExecutionPeriod(executionPeriod);
	setPeriod(period);

	checkShiftLoad(shift);	
    }

    public void edit(YearMonthDay newBeginDate, YearMonthDay newEndDate, DiaSemana diaSemana, Calendar inicio, Calendar fim, FrequencyType frequency) {			

	if(wasFinished()) {
	    throw new DomainException("error.Lesson.already.finished");
	}
	
	if(newBeginDate != null && newEndDate != null && newBeginDate.isAfter(newEndDate)) {
	    throw new DomainException("error.Lesson.new.begin.date.after.new.end.date");
	}

	GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod = getShift().getExecutionCourse().getMaxLessonsPeriod();	
	if(newBeginDate == null || newBeginDate.isBefore(maxLessonsPeriod.getLeft())) {
	    throw new DomainException("error.Lesson.invalid.new.begin.date");
	}	
	if(newEndDate == null || newEndDate.isAfter(maxLessonsPeriod.getRight())) {
	    throw new DomainException("error.invalid.new.end.date");
	}

	refreshPeriodAndInstancesInEditOperation(newBeginDate, newEndDate);

	setDiaSemana(diaSemana);
	setInicio(inicio);
	setFim(fim);	
	setFrequency(frequency);

	checkShiftLoad(getShift());	
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

    public boolean wasFinished() {
	return !hasPeriod();
    }
    
    public ExecutionCourse getExecutionCourse() {
	return getShift().getExecutionCourse();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return (!hasLessonSpaceOccupation() && hasAnyLessonInstances() || hasPeriod())
		&& getFrequency() != null && getDiaSemana() != null && hasExecutionPeriod() && hasShift();		 
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
	} else if(hasAnyLessonInstances() && wasFinished()){
	    return getLastLessonInstance().getRoom();
	}		
	return null;
    }

    public boolean hasSala() {
	return getSala() != null;
    }

    public void refreshPeriodAndInstancesInSummaryCreation(YearMonthDay newBeginDate) {
	if(hasPeriod()) {	    
	    OccupationPeriod currentPeriod = getPeriod();	    		    
	    if(newBeginDate != null && newBeginDate.isAfter(currentPeriod.getStartYearMonthDay())) {						
		createAllLessonInstancesUntil(newBeginDate.minusDays(1));
		refreshPeriod(newBeginDate, getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay(), currentPeriod);	    				
	    }
	}
    }

    private void refreshPeriodAndInstancesInEditOperation(YearMonthDay newBeginDate, YearMonthDay newEndDate) {
	if(hasPeriod()) {	    
	    OccupationPeriod currentPeriod = getPeriod();	    		    
	    if(newBeginDate != null && newEndDate != null && newBeginDate.isBefore(newEndDate)) {					
		removeExistentInstancesWithoutSummaryAfterOrEqual(newBeginDate);				
		createAllLessonInstancesUntil(newBeginDate.minusDays(1));
		refreshPeriod(newBeginDate, newEndDate, currentPeriod);	    				
	    }
	}
    }

    private void createAllLessonInstancesUntil(YearMonthDay day) {	
	if(day != null) {
	    
	    SortedSet<YearMonthDay> allLessonDatesBeforeRefreshPeriod = getAllLessonDatesUntil(day);	    	    
	
	    List<LessonInstance> allLessonInstancesUntil = getAllLessonInstancesUntil(day);	    
	    for (LessonInstance lessonInstance : allLessonInstancesUntil) {
		allLessonDatesBeforeRefreshPeriod.remove(lessonInstance.getDay());
	    }	    	   
	    
	    for (YearMonthDay dateToCreate : allLessonDatesBeforeRefreshPeriod) {		
		new LessonInstance(this, dateToCreate);		
	    }	    	   
	}
    }  
    
    private void removeExistentInstancesWithoutSummaryAfterOrEqual(YearMonthDay newBeginDate) {				   
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

    private void refreshPeriod(YearMonthDay newBeginDate, YearMonthDay newEndDate, OccupationPeriod currentPeriod) {	

	if(!currentPeriod.getEndYearMonthDay().isBefore(newBeginDate)) {								
	    setPeriod(getNewNestedPeriods(currentPeriod, newBeginDate, newEndDate));			

	} else if (currentPeriod.hasNextPeriod()){				

	    OccupationPeriod currentNextPeriod = currentPeriod.getNextPeriod();		

	    if(!currentNextPeriod.getStartYearMonthDay().isAfter(newEndDate)) {
		
		if(!currentNextPeriod.getStartYearMonthDay().isBefore(newBeginDate)) {		
		    setPeriod(getNewNestedPeriods(currentNextPeriod, currentNextPeriod.getStartYearMonthDay(), newEndDate));		    		
		    
		} else if(!currentNextPeriod.getEndYearMonthDay().isBefore(newBeginDate)) {
		    setPeriod(getNewNestedPeriods(currentNextPeriod, newBeginDate, newEndDate));    
		}
		
	    } else {
		removeLessonSpaceOccupationAndPeriod();
	    }
	} else {		
	    removeLessonSpaceOccupationAndPeriod();
	}	
	currentPeriod.delete();
    }

    private void removeLessonSpaceOccupationAndPeriod() {
	if(hasLessonSpaceOccupation()) {
	    getLessonSpaceOccupation().delete();
	}
	setPeriod(null);
    } 

    private OccupationPeriod getNewNestedPeriods(OccupationPeriod currentPeriod, YearMonthDay newBeginDate, YearMonthDay newEndDate) {	
	if(!currentPeriod.getEndYearMonthDay().isBefore(newEndDate)) {
	    return new OccupationPeriod(newBeginDate, newEndDate); 
	} else {	    
	    OccupationPeriod newPeriod = new OccupationPeriod(newBeginDate, currentPeriod.getEndYearMonthDay());
	    OccupationPeriod newPeriodPointer = newPeriod;	    
	    while(currentPeriod.hasNextPeriod()) {			
		if(currentPeriod.getNextPeriod().getStartYearMonthDay().isAfter(newEndDate)) {
		    break;
		}		
		if(!currentPeriod.getNextPeriod().getEndYearMonthDay().isBefore(newEndDate)) {		    
		    OccupationPeriod newNextPeriod = new OccupationPeriod(currentPeriod.getNextPeriod().getStartYearMonthDay(), newEndDate);
		    newPeriodPointer.setNextPeriod(newNextPeriod);
		    break;		    
		
		} else {
		    OccupationPeriod newNextPeriod = new OccupationPeriod(
			    currentPeriod.getNextPeriod().getStartYearMonthDay(), 
			    currentPeriod.getNextPeriod().getEndYearMonthDay());
		    newPeriodPointer.setNextPeriod(newNextPeriod);
		    newPeriodPointer = newNextPeriod;
		    currentPeriod = currentPeriod.getNextPeriod();
		}
	    }
	    return newPeriod;
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

    private int getUnitMinutes() {	
	return Minutes.minutesBetween(getBeginHourMinuteSecond(), getEndHourMinuteSecond()).getMinutes();
    }

    public BigDecimal getTotalHours() {
	return getUnitHours().multiply(BigDecimal.valueOf(getFinalNumberOfLessonInstances()));
    }

    public BigDecimal getUnitHours() {	
	return BigDecimal.valueOf(getUnitMinutes()).divide(BigDecimal.valueOf(NUMBER_OF_MINUTES_IN_HOUR));
    }

    public String getInicioString() {
	return String.valueOf(getInicio().get(Calendar.HOUR_OF_DAY));
    }

    public double hoursAfter(int hour) {

	HourMinuteSecond afterHour = new HourMinuteSecond(hour, 0, 0);
	
	if(!getBeginHourMinuteSecond().isBefore(afterHour)) {
	    return getUnitHours().doubleValue();

	} else if(getEndHourMinuteSecond().isAfter(afterHour)) {
	    return BigDecimal.valueOf(Minutes.minutesBetween(afterHour, getEndHourMinuteSecond()).getMinutes()).
	    	divide(BigDecimal.valueOf(NUMBER_OF_MINUTES_IN_HOUR)).doubleValue();	    
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
	    YearMonthDay periodBegin = getPeriod().getStartYearMonthDay();
	    YearMonthDay lessonBegin = periodBegin.toDateTimeAtMidnight().withDayOfWeek(getDiaSemana()
		    .getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();	    
	    if(lessonBegin.isBefore(periodBegin)) {
		lessonBegin = lessonBegin.plusDays(7);
	    }     
	    return lessonBegin;
	}	
	return null;	
    }

    private YearMonthDay getLessonEndDay() {	
	if(hasPeriod()) {
	    YearMonthDay periodEnd = getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay();
	    YearMonthDay lessonEnd = periodEnd.toDateTimeAtMidnight().withDayOfWeek(getDiaSemana().
		    getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();	    
	    if(lessonEnd.isAfter(periodEnd)) {
		lessonEnd = lessonEnd.minusDays(7);
	    } 
	    return lessonEnd;
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

    public int getFinalNumberOfLessonInstances() {
	int count = getLessonInstancesCount();
	if(hasPeriod()) {	   	    
	    YearMonthDay endDateToSearch = getLessonEndDay();	    	   
	    count += getAllValidLessonDatesUntil(endDateToSearch).size();
	}		
	return count;
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
	result.append(getDiaSemana().toString()).append(" (");
	result.append(getBeginHourMinuteSecond().toString("HH:mm")).append("-");
	result.append(getEndHourMinuteSecond().toString("HH:mm")).append(") ");
	result.append(hasSala() ? ((AllocatableSpace)getSala()).getName().toString() : "");	
	return result.toString();
    }

    private void checkShiftLoad(Shift shift) {

	if(shift != null) { 
	   
	    List<CourseLoad> courseLoads = shift.getCourseLoads();

	    if(courseLoads.size() == 1) {

		CourseLoad courseLoad = courseLoads.get(0);
		if(getUnitHours().compareTo(courseLoad.getUnitQuantity()) != 0) {
		    throw new DomainException("error.Lesson.shift.load.unit.quantity.exceeded");
		}		
		
		BigDecimal totalHours = shift.getTotalHours();
		if(totalHours.compareTo(courseLoad.getTotalQuantity()) == 1) {
		    throw new DomainException("error.Lesson.shift.load.total.quantity.exceeded");
		}

	    } else if(courseLoads.size() > 1){
		
		boolean unitValid = false, totalValid = false;
		BigDecimal lessonHours = getUnitHours();
		BigDecimal totalHours = shift.getTotalHours();
		
		for (CourseLoad courseLoad : courseLoads) {		    
		    unitValid = false;
		    totalValid = false;		    
		    		   
		    if(lessonHours.compareTo(courseLoad.getUnitQuantity()) == 0) {
			unitValid = true;			
		    }		    		    		    
		    if(totalHours.compareTo(courseLoad.getTotalQuantity()) != 1) {
			totalValid = true;
			if(unitValid) {
			    break;
			}
		    }
		}
		
		if(!totalValid || !unitValid) {
		    throw new DomainException("error.Lesson.shift.load.exceeded");
		}
	    }
	}
    }
}
