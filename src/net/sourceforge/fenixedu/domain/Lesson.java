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
    public static int NUMBER_OF_DAYS_IN_WEEK = 7;

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
	setPeriod(period);

	checkShiftLoad(shift);	
    }

    public void edit(YearMonthDay newBeginDate, YearMonthDay newEndDate, DiaSemana diaSemana, Calendar inicio, Calendar fim, 
	    FrequencyType frequency, Boolean createLessonInstances) {			

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

	refreshPeriodAndInstancesInEditOperation(newBeginDate, newEndDate, createLessonInstances, maxLessonsPeriod);

	if(wasFinished() && (hasLessonSpaceOccupation() || !hasAnyLessonInstances())) {
	    throw new DomainException("error.Lesson.empty.period");
	}	

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

	super.setShift(null);	
	removeRootDomainObject();
	deleteDomainObject();
    }

    @Override
    public void setShift(Shift shift) {
	if (shift == null) {
	    throw new DomainException("error.Lesson.empty.shift");
	}
	super.setShift(shift);
    }

    @Override
    public void setFrequency(FrequencyType frequency) {
	if (frequency == null) {
	    throw new DomainException("error.Lesson.empty.type");
	}
	super.setFrequency(frequency);
    }

    @Override
    public void setPeriod(OccupationPeriod period) {
	if (period == null) {
	    throw new DomainException("error.Lesson.empty.period");
	}
	super.setPeriod(period);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return getFrequency() != null && getDiaSemana() != null;		 
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkTimeInterval() {
	final HourMinuteSecond start = getBeginHourMinuteSecond();
	final HourMinuteSecond end = getEndHourMinuteSecond();	
	return start != null && end != null && start.isBefore(end);
    }

    public boolean wasFinished() {
	return !hasPeriod();
    }

    public ExecutionCourse getExecutionCourse() {
	return getShift().getExecutionCourse();
    }

    public ExecutionPeriod getExecutionPeriod() {
	return getShift().getExecutionPeriod();
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
	if(!wasFinished() && newBeginDate != null && newBeginDate.isAfter(getPeriod().getStartYearMonthDay())) {						
	    SortedSet<YearMonthDay> instanceDates = getAllLessonInstancesDatesToCreate(getLessonStartDay(), newBeginDate.minusDays(1), true);
	    refreshPeriod(newBeginDate, getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay());	    		
	    createAllLessonInstances(instanceDates);
	}
    }
   
    private void refreshPeriodAndInstancesInEditOperation(YearMonthDay newBeginDate, YearMonthDay newEndDate, 
	    Boolean createLessonInstances, GenericPair<YearMonthDay, YearMonthDay> maxLessonsPeriod) {
				
	if(!wasFinished()) {
	    removeExistentInstancesWithoutSummaryAfterOrEqual(newBeginDate);	    
	    SortedSet<YearMonthDay> instanceDates = getAllLessonInstancesDatesToCreate(getLessonStartDay(), newBeginDate.minusDays(1), createLessonInstances);
	    refreshPeriod(newBeginDate, newEndDate);
	    createAllLessonInstances(instanceDates);
	}
    }

    private void createAllLessonInstances(SortedSet<YearMonthDay> instanceDates) {
	for (YearMonthDay day : instanceDates) {
	    new LessonInstance(this, day);
	}	
    }
    
    private SortedSet<YearMonthDay> getAllLessonInstancesDatesToCreate(YearMonthDay startDate, YearMonthDay endDate, Boolean createLessonInstances) {	
	
	if(startDate != null && endDate != null && !startDate.isAfter(endDate) && createLessonInstances) {	    	    
	    
	    SortedSet<YearMonthDay> possibleLessonDates = getAllValidLessonDatesWithoutInstancesDates(startDate, endDate);	    	    
	    List<LessonInstance> allLessonInstancesUntil = getAllLessonInstancesUntil(endDate);	    
	    
	    for (LessonInstance lessonInstance : allLessonInstancesUntil) {
		possibleLessonDates.remove(lessonInstance.getDay());
	    }	    
	    return possibleLessonDates;
	}	
	return new TreeSet<YearMonthDay>();
    }  

    private void removeExistentInstancesWithoutSummaryAfterOrEqual(YearMonthDay newBeginDate) {				   
	Map<Boolean, List<LessonInstance>> instances = getLessonInstancesAfterOrEqual(newBeginDate);	
	if(instances.get(Boolean.TRUE).isEmpty()) {
	    List<LessonInstance> instancesWithoutSummary = instances.get(Boolean.FALSE);
	    for (Iterator<LessonInstance> iter = instancesWithoutSummary.iterator(); iter.hasNext();) {
		LessonInstance instance = iter.next();
		iter.remove();
		instance.delete();
	    }	    
	} else {
	    throw new DomainException("error.Lesson.invalid.new.begin.date");
	}	
    }

    private Map<Boolean, List<LessonInstance>> getLessonInstancesAfterOrEqual(YearMonthDay day){

	Map<Boolean, List<LessonInstance>> result = new HashMap<Boolean, List<LessonInstance>>();
	result.put(Boolean.TRUE, new ArrayList<LessonInstance>());
	result.put(Boolean.FALSE, new ArrayList<LessonInstance>());

	if(day != null) {	
	    List<LessonInstance> lessonInstances = getLessonInstances();
	    for (LessonInstance lessonInstance : lessonInstances) {
		if(lessonInstance.hasSummary() && !lessonInstance.getDay().isBefore(day)) {
		    List<LessonInstance> list = result.get(Boolean.TRUE);
		    list.add(lessonInstance);
		} else if(!lessonInstance.hasSummary() && !lessonInstance.getDay().isBefore(day)) {
		    List<LessonInstance> list = result.get(Boolean.FALSE);
		    list.add(lessonInstance);
		}
	    }	
	}
	
	return result;
    }

    private void refreshPeriod(YearMonthDay newBeginDate, YearMonthDay newEndDate) {	

	if(!wasFinished() && newBeginDate != null && newEndDate != null && !newBeginDate.isAfter(newEndDate)) {

	    OccupationPeriod currentPeriod = getPeriod();
	    
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
    }

    private void removeLessonSpaceOccupationAndPeriod() {
	if(hasLessonSpaceOccupation()) {
	    getLessonSpaceOccupation().delete();
	}
	super.setPeriod(null);
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
	if(timeToInsert == null || summaryDate == null || summaryDate.isAfter(currentDate)) {
	    return false;
	}

	if(currentDate.isEqual(summaryDate)) {	    	    
	    HourMinuteSecond lessonEndTime = null;
	    LessonInstance lessonInstance = getLessonInstanceFor(summaryDate);	
	    lessonEndTime = lessonInstance != null ? lessonInstance.getEndTime() : getEndHourMinuteSecond();	    	    
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
	if(!wasFinished()) {	    
	    YearMonthDay periodBegin = getPeriod().getStartYearMonthDay();	    
	    return getValidBeginDate(periodBegin);	    
	}	
	return null;	
    }

    private YearMonthDay getLessonEndDay() {	
	if(!wasFinished()) {
	    YearMonthDay periodEnd = getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay();
	    return getValidEndDate(periodEnd);
	} 
	return null;	
    }

    private YearMonthDay getValidBeginDate(YearMonthDay startDate) {
	YearMonthDay lessonBegin = startDate.toDateTimeAtMidnight().withDayOfWeek(getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();	    
	if(lessonBegin.isBefore(startDate)) {
	    lessonBegin = lessonBegin.plusDays(NUMBER_OF_DAYS_IN_WEEK);
	}
	return lessonBegin;
    }

    private YearMonthDay getValidEndDate(YearMonthDay endDate) {
	YearMonthDay lessonEnd = endDate.toDateTimeAtMidnight().withDayOfWeek(getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();	    
	if(lessonEnd.isAfter(endDate)) {
	    lessonEnd = lessonEnd.minusDays(NUMBER_OF_DAYS_IN_WEEK);
	}
	return lessonEnd;
    }

    public Campus getLessonCampus() {	
	if(!wasFinished()) {
	    return hasSala() ? getSala().getSpaceCampus() : null;
	} else {
	    LessonInstance lastLessonInstance = getLastLessonInstance();
	    return lastLessonInstance != null && lastLessonInstance.getRoom() != null ?
		    lastLessonInstance.getRoom().getSpaceCampus() : null;
	}	
    }

    public YearMonthDay getNextPossibleSummaryDate() {

	YearMonthDay currentDate = new YearMonthDay();
	HourMinuteSecond now = new HourMinuteSecond();
	Summary lastSummary = getLastSummary();

	if (lastSummary != null) {
	    
	    SortedSet<YearMonthDay> datesEvenToday = getAllLessonDatesUntil(currentDate);    	   	    
	    SortedSet<YearMonthDay> possibleDates = datesEvenToday.tailSet(lastSummary.getSummaryDateYearMonthDay());
	    
	    possibleDates.remove(lastSummary.getSummaryDateYearMonthDay());
	    if(!possibleDates.isEmpty()) {		
		YearMonthDay nextPossibleDate = possibleDates.first();
		return isTimeValidToInsertSummary(now, nextPossibleDate) ? nextPossibleDate : null;
	    }
	    
	} else {	
	    YearMonthDay nextPossibleDate = hasAnyLessonInstances() ? getFirstLessonInstance().getDay() : getLessonStartDay();	   	 
	    return isTimeValidToInsertSummary(now, nextPossibleDate) ? nextPossibleDate : null;
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
	if(!wasFinished()) {	 
	    YearMonthDay startDateToSearch = getLessonStartDay();
	    YearMonthDay endDateToSearch = getLessonEndDay();
	    dates.addAll(getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch));
	}	
	return dates;
    }

    public SortedSet<YearMonthDay> getAllLessonDates() {
	SortedSet<YearMonthDay> dates = getAllLessonInstanceDates();	
	if(!wasFinished()) {	   	    
	    YearMonthDay startDateToSearch = getLessonStartDay();
	    YearMonthDay endDateToSearch = getLessonEndDay();
	    dates.addAll(getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch));
	}	
	return dates;
    }

    public int getFinalNumberOfLessonInstances() {
	int count = getLessonInstancesCount();
	if(!wasFinished()) {	   	    
	    YearMonthDay startDateToSearch = getLessonStartDay();
	    YearMonthDay endDateToSearch = getLessonEndDay();	    	   
	    count += getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch).size();
	}		
	return count;
    }

    private SortedSet<YearMonthDay> getAllLessonDatesUntil(YearMonthDay day) {			
	SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();				
	if(day != null) {	  	    
	    result.addAll(getAllLessonInstanceDatesUntil(day));
	    if(!wasFinished()) {
		YearMonthDay startDateToSearch = getLessonStartDay();
		YearMonthDay lessonEndDay = getLessonEndDay();	
		YearMonthDay endDateToSearch = (lessonEndDay.isAfter(day)) ? day : lessonEndDay;		    
		result.addAll(getAllValidLessonDatesWithoutInstancesDates(startDateToSearch, endDateToSearch));
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

    private SortedSet<YearMonthDay> getAllValidLessonDatesWithoutInstancesDates(YearMonthDay startDateToSearch, YearMonthDay endDateToSearch) {			
	
	SortedSet<YearMonthDay> result = new TreeSet<YearMonthDay>();	
	startDateToSearch = startDateToSearch != null ? getValidBeginDate(startDateToSearch) : null;
	
	if (!wasFinished() && startDateToSearch != null && endDateToSearch != null && !startDateToSearch.isAfter(endDateToSearch)) {
	  
	    Campus lessonCampus = getLessonCampus();
	    while (true) {
		if (isDayValid(startDateToSearch, lessonCampus)) {
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
    
    private boolean isDayValid(YearMonthDay day, Campus lessonCampus) {	
	return !Holiday.isHoliday(day, lessonCampus) && getPeriod().nestedOccupationPeriodsContainsDay(day);
    }

    public YearMonthDay getNextPossibleLessonInstanceDate() {
		
	SortedSet<YearMonthDay> allLessonDates = getAllLessonDates();
	LessonInstance lastLessonInstance = getLastLessonInstance();
	
	if(lastLessonInstance != null) {
	    YearMonthDay day = lastLessonInstance.getDay();
	    SortedSet<YearMonthDay> nextLessonDates = allLessonDates.tailSet(day);
	    nextLessonDates.remove(day);
	    return nextLessonDates.isEmpty() ? null : nextLessonDates.first();
	}		
	
	return allLessonDates.isEmpty() ? null : allLessonDates.first();
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
		    throw new DomainException("error.Lesson.shift.load.unit.quantity.exceeded", 
			    getUnitHours().toString(), courseLoad.getUnitQuantity().toString());
		}		

		if(shift.getTotalHours().compareTo(courseLoad.getTotalQuantity()) == 1) {
		    throw new DomainException("error.Lesson.shift.load.total.quantity.exceeded",
			    shift.getTotalHours().toString(), courseLoad.getTotalQuantity().toString());
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
}
