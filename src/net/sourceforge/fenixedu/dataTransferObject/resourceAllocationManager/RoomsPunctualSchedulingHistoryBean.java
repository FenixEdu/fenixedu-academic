package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class RoomsPunctualSchedulingHistoryBean extends ResourceAllocationBean {

    private Partial year;
    
    private Partial month;
    
    public RoomsPunctualSchedulingHistoryBean() {
	setYear(new Partial(DateTimeFieldType.year(), new YearMonthDay().getYear()));
	setMonth(new Partial(DateTimeFieldType.monthOfYear(), new YearMonthDay().getMonthOfYear()));
    }

    public AllocatableSpace getAllocatableSpace() {
	return (AllocatableSpace) getResource();
    }
    
    public void setAllocatableSpace(AllocatableSpace resource) {
	setResource(resource);
    }
        
    public Partial getMonth() {
        return month;
    }

    public void setMonth(Partial month) {
        this.month = month;
    }

    public Partial getYear() {
        return year;
    }

    public void setYear(Partial year) {
        this.year = year;
    }
}
