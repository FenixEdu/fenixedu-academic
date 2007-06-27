package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

public class WrittenEvaluationSpaceOccupation extends WrittenEvaluationSpaceOccupation_Base {
      
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public WrittenEvaluationSpaceOccupation(AllocatableSpace allocatableSpace, HourMinuteSecond startTime,
	    HourMinuteSecond endTime, DiaSemana dayOfWeek, 
	    OccupationPeriod period, WrittenEvaluation writtenEvaluation) {	
        
	super();	
	
	if(period != null && allocatableSpace != null && startTime != null && endTime != null && dayOfWeek != null) {
	    if(!allocatableSpace.isFree(period.getStartYearMonthDay(), period.getEndYearMonthDay(),
		    startTime, endTime, dayOfWeek, null, null, Boolean.TRUE, Boolean.TRUE, this)) {		
		throw new DomainException("error.roomOccupied", getRoom().getName());
	    }
	}
	
        setResource(allocatableSpace);
        setOccupationInterval(startTime, endTime);
        setDayOfWeek(dayOfWeek);
        setWrittenEvaluation(writtenEvaluation);
        setPeriod(period);
    }    
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void edit(HourMinuteSecond startTime,
	    HourMinuteSecond endTime, DiaSemana dayOfWeek, OccupationPeriod period) {
	
	if(period != null && startTime != null && endTime != null && dayOfWeek != null) {
	    if(!getRoom().isFree(period.getStartYearMonthDay(), period.getEndYearMonthDay(),
		    startTime, endTime, dayOfWeek, null, null, Boolean.TRUE, Boolean.TRUE, this)) {		
		throw new DomainException("error.roomOccupied", getRoom().getName());
	    }
	}
	
	setOccupationInterval(startTime, endTime);
	setDayOfWeek(dayOfWeek);
	setPeriod(period);
    }
          
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void delete() {
	super.setWrittenEvaluation(null);
	super.delete();    
    }
    
    public void setOccupationInterval(final HourMinuteSecond begin, final HourMinuteSecond end) {
	if(begin == null || end == null || !begin.isBefore(end)) {
	    throw new DomainException("error.EventSpaceOccupation.invalid.time");
	}	
	super.setStartTimeDateHourMinuteSecond(begin);
	super.setEndTimeDateHourMinuteSecond(end);
    }
    
    @Override
    public boolean isWrittenEvaluationSpaceOccupation() {
	return true;
    }
       
    @Override
    public void setWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
	if(writtenEvaluation == null) {
	    throw new DomainException("error.WrittenEvaluationSpaceOccupation.empty.writtenEvaluation");
	}
	super.setWrittenEvaluation(writtenEvaluation);
    }
    
    @Override
    public Integer getFrequency() {
	return null;
    }

    @Override
    public Integer getWeekOfQuinzenalStart() {
	return null;
    }

    @Override
    public void setFrequency(Integer frequency) {
	// Do Nothing
    }

    @Override
    public void setFrequency(FrequencyType type) {
	// Do Nothing
    }

    @Override
    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
	// Do Nothing
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getWrittenEvaluationOccupationsAccessGroupWithChainOfResponsibility();
    }      
}
