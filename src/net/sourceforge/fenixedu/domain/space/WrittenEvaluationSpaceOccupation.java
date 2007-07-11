package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.YearMonthDay;

public class WrittenEvaluationSpaceOccupation extends WrittenEvaluationSpaceOccupation_Base {
      
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public WrittenEvaluationSpaceOccupation(AllocatableSpace allocatableSpace, WrittenEvaluation writtenEvaluation) {	
        
	super();		

	setWrittenEvaluation(writtenEvaluation);
	
	if(allocatableSpace != null && !allocatableSpace.isFree(this)) {		
	    throw new DomainException("error.roomOccupied", getRoom().getName());
	}

        setResource(allocatableSpace);                		       		  
    }    
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void edit() {	
	if(!getRoom().isFree(this)) {		
	    throw new DomainException("error.roomOccupied", getRoom().getName());
	}	
    }
          
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void delete() {
	super.setWrittenEvaluation(null);
	super.delete();    
    }
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return hasWrittenEvaluation();	
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
    public Group getAccessGroup() {
	return getSpace().getWrittenEvaluationOccupationsAccessGroupWithChainOfResponsibility();
    }
        
    @Override
    public YearMonthDay getBeginDate() {
	return getWrittenEvaluation().getDayDateYearMonthDay();
    }

    @Override
    public YearMonthDay getEndDate() {
	return getWrittenEvaluation().getDayDateYearMonthDay();
    }
    
    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
	return getWrittenEvaluation().getBeginningDateHourMinuteSecond();
    }
       
    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
	return getWrittenEvaluation().getEndDateHourMinuteSecond();
    }

    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
	return null;
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
	return null;
    }

    @Override
    public DiaSemana getDayOfWeek() {
	return null;
    }    
    
    @Override
    public FrequencyType getFrequency() {
	return null;
    }

    @Override
    public Integer getWeekOfQuinzenalStart() {
	return null;
    }
}
