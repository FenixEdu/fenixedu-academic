package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.YearMonthDay;

public class GenericEventSpaceOccupation extends GenericEventSpaceOccupation_Base {
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")   
    public GenericEventSpaceOccupation(AllocatableSpace allocatableSpace, YearMonthDay beginDate, YearMonthDay endDate,
	    HourMinuteSecond beginTime, HourMinuteSecond endTime, FrequencyType frequencyType, GenericEvent genericEvent,
	    Boolean markSaturday, Boolean markSunday) {	
	
	super();          
	
	if(beginDate == null || endDate == null || beginTime == null || endTime == null
		|| beginDate.isAfter(endDate) || !beginTime.isBefore(endTime)) {
	    throw new DomainException("error.GenericEventSpaceOccupation.invalid.dates.or.time");
	}
	
        DiaSemana diaSemana = new DiaSemana(DiaSemana.getDiaSemana(beginDate));
        Integer frequency = (frequencyType != null) ? frequencyType.ordinal() + 1 : null;        

        OccupationPeriod occupationPeriod = OccupationPeriod.readOccupationPeriod(beginDate, endDate);
        if(occupationPeriod == null) {
            occupationPeriod = new OccupationPeriod(beginDate, endDate);
        }
                       
        if(frequencyType != null && frequencyType.equals(FrequencyType.DAILY) && (markSaturday == null || markSunday == null)) {
            throw new DomainException("error.roomOccupation.invalid.dailyFrequency");
        }
        
        if(!allocatableSpace.isFree(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay(),
        	beginTime, endTime, diaSemana, frequency, 1, markSaturday, markSunday, this)) {
            throw new DomainException("error.roomOccupation.room.is.not.free");
        }
        
        setDailyFrequencyMarkSaturday(markSaturday);
        setDailyFrequencyMarkSunday(markSunday);
        setResource(allocatableSpace);
        setGenericEvent(genericEvent);
        setStartTimeDateHourMinuteSecond(beginTime.setSecondOfMinute(0));
        setEndTimeDateHourMinuteSecond(endTime.setSecondOfMinute(0));
        setDayOfWeek(diaSemana);                       
        setPeriod(occupationPeriod);
        setFrequency(frequencyType);               
    }   
    

    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")   
    public void delete() {	
	super.setGenericEvent(null);
	super.delete();
    }
            
    @Override
    public void setGenericEvent(GenericEvent genericEvent) {
	if(genericEvent == null) {
	    throw new DomainException("error.GenericEventSpaceOccupation.empty.genericEvent");
	}
	super.setGenericEvent(genericEvent);
    }
    
    @Override
    public boolean isGenericEventSpaceOccupation() {
	return true;
    }
        
    @Override
    public Integer getFrequency() {
	return getGenericEvent().getFrequency() != null ? getGenericEvent().getFrequency().ordinal() + 1 : null;
    }
    
    @Override
    public void setFrequency(Integer frequency) {}

    @Override
    public void setFrequency(FrequencyType type) {
	getGenericEvent().setFrequency(type);
    }

    @Override
    public Integer getWeekOfQuinzenalStart() {
	return null;
    }
    
    @Override
    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {}

    @Override
    public Group getAccessGroup() {
	return getSpace().getGenericEventOccupationsAccessGroupWithChainOfResponsibility();
    }
    
}
