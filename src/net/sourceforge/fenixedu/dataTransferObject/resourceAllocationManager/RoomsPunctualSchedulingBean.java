package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.utl.fenix.utils.Pair;

public class RoomsPunctualSchedulingBean implements Serializable {

    private Boolean markSaturday;
    
    private Boolean markSunday;
    
    private DomainReference<PunctualRoomsOccupationRequest> roomsReserveRequestReference;
    
    private Integer roomsReserveRequestIdentification;
    
    private MultiLanguageString smallDescription;
    
    private MultiLanguageString completeDescription;
    
    private YearMonthDay begin;
    
    private YearMonthDay end;
    
    private Partial beginTime;

    private Partial endTime;
    
    private PeriodType periodType;
    
    private FrequencyType frequency;
    
    private List<DomainReference<AllocatableSpace>> roomsReferences;
    
    private DomainReference<AllocatableSpace> selectedRoomReference;
    
    private DomainReference<GenericEvent> genericEventReference;
    
    private Boolean ganttDiagramAvailable;
    
    private transient Locale locale = LanguageUtils.getLocale(); 
    
    public RoomsPunctualSchedulingBean() {  
	setGanttDiagramAvailable(Boolean.TRUE);	
    }       
           
    public RoomsPunctualSchedulingBean(GenericEvent genericEvent) {
	if(genericEvent.getPunctualRoomsOccupationRequest() != null) {
	    setRoomsReserveRequestIdentification(genericEvent.getPunctualRoomsOccupationRequest().getIdentification());
	}
	setRooms(genericEvent.getAssociatedRooms());
	setBegin(genericEvent.getBeginDate());
	setEnd(genericEvent.getEndDate());
	setBeginTime(new Partial(genericEvent.getStartTimeDateHourMinuteSecond()));
	setEndTime(new Partial(genericEvent.getEndTimeDateHourMinuteSecond()));	
	setFrequency(genericEvent.getFrequency());
	setSmallDescription(genericEvent.getTitle());
	setCompleteDescription(genericEvent.getDescription());
	setGenericEvent(genericEvent);
	setGanttDiagramAvailable(Boolean.TRUE);
	setMarkSaturday(genericEvent.getDailyFrequencyMarkSaturday());
	setMarkSunday(genericEvent.getDailyFrequencyMarkSunday());
    }
  
    public void editFrequencyTypeWithoutDailyFrequency(YearMonthDay begin, Partial beginTime, YearMonthDay end, Partial endTime,
	    FrequencyType frequency) {
	
	setBegin(begin);
	setEnd(end);
	setBeginTime(beginTime);
	setEndTime(endTime);
	setFrequency(frequency);
	setMarkSaturday(null);
	setMarkSunday(null);
    }
    
    public void editFrequencyTypeWithDailyFrequency(YearMonthDay begin, Partial beginTime, YearMonthDay end, Partial endTime,
	    FrequencyType frequency, Boolean markSaturday, Boolean markSunday) {
	
	setBegin(begin);
	setEnd(end);
	setBeginTime(beginTime);
	setEndTime(endTime);
	setFrequency(frequency);
	setMarkSaturday(markSaturday);
	setMarkSunday(markSunday);
    }
    
    public void editContinuousType(YearMonthDay begin, Partial beginTime, YearMonthDay end, Partial endTime) {
	setBegin(begin);
	setEnd(end);
	setBeginTime(beginTime);
	setEndTime(endTime);
	setFrequency(null);
	setMarkSaturday(null);
	setMarkSunday(null);
    }
    
    public void editDailyType(YearMonthDay begin, Partial beginTime, Partial endTime) {
	setBegin(begin);
	setEnd(begin);
	setBeginTime(beginTime);
	setEndTime(endTime);
	setFrequency(null);
	setMarkSaturday(null);
	setMarkSunday(null);
    }
    
    public enum PeriodType {
	DAILY_TYPE, WITH_FREQUENCY, CONTINUOUS;

	public String getName() {
	    return name();
	}
    }
    
    public Pair<Integer, Integer> getTotalAvailableRoomSpace(){
	Integer availableSpaceForExam = Integer.valueOf(0), availableNormalSpace = Integer.valueOf(0);
	for (AllocatableSpace room : getRooms()) {
	    Integer capacidadeExame = room.getCapacidadeExame();
	    Integer capacidadeNormal = room.getCapacidadeNormal();
	    if(capacidadeNormal != null) {
		availableNormalSpace += capacidadeNormal;
	    }
	    if(capacidadeExame != null) {
		availableSpaceForExam += capacidadeExame;
	    }	    
	}
	return new Pair<Integer, Integer>(availableNormalSpace, availableSpaceForExam);
    }
    
    public String getPresentationBeginTime() {
	return getBeginTime().toString("HH:mm");
    }
    
    public String getPresentationEndTime() {
	return getEndTime().toString("HH:mm");
    }
    
    public String getPresentationBeginDate() {
	return getBegin().toString("dd MMMM yyyy", locale) + " (" + getBegin().toDateTimeAtMidnight().toString("E", locale) + ")";
    }
    
    public String getPresentationEndDate() {
	return getEnd().toString("dd MMMM yyyy", locale) + " (" + getEnd().toDateTimeAtMidnight().toString("E", locale) + ")";
    }       

    public YearMonthDay getBegin() {
        return begin;
    }

    public void setBegin(YearMonthDay begin) {
        this.begin = begin;
    }

    public Partial getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Partial beginTime) {
        this.beginTime = beginTime;
    }

    public YearMonthDay getEnd() {
        return end;
    }

    public void setEnd(YearMonthDay end) {	
        this.end = end;
    }

    public Partial getEndTime() {
        return endTime;
    }

    public void setEndTime(Partial endTime) {
        this.endTime = endTime;
    }

    public FrequencyType getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyType frequency) {
        this.frequency = frequency;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public List<AllocatableSpace> getRooms() {
	List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();	
        if(roomsReferences == null) {
            return result;
        }
	for (DomainReference<AllocatableSpace> domainReference : roomsReferences) {
	    result.add(domainReference.getObject());
	}
	return result;
    }
    
    public void setRooms(List<AllocatableSpace> rooms) {
	if(roomsReferences == null) {
	    roomsReferences = new ArrayList<DomainReference<AllocatableSpace>>();
	}
	for (AllocatableSpace room : rooms) {
	    roomsReferences.add(new DomainReference<AllocatableSpace>(room));
	}
    }
    
    public void addNewRoom(AllocatableSpace space) {
	if(space != null) {
            if(roomsReferences == null) {
        	roomsReferences = new ArrayList<DomainReference<AllocatableSpace>>();     
            }
            for (DomainReference<AllocatableSpace> domainReference : roomsReferences) {
		if(domainReference.getObject().equals(space)) {
		    return;
		}
	    }
            roomsReferences.add(new DomainReference<AllocatableSpace>(space));
	}
    }
    
    public void removeRoom(AllocatableSpace oldRoom) {
	if(oldRoom != null) {
            if(roomsReferences == null) {
        	roomsReferences = new ArrayList<DomainReference<AllocatableSpace>>();     
            }           
            roomsReferences.remove(new DomainReference<AllocatableSpace>(oldRoom));
	}
    }
       
    public AllocatableSpace getSelectedRoom() {
        return (this.selectedRoomReference != null) ? this.selectedRoomReference.getObject() : null;
    }

    public void setSelectedRoom(AllocatableSpace selectedRoom) {
        this.selectedRoomReference = (selectedRoom != null) ? new DomainReference<AllocatableSpace>(selectedRoom) : null;        
    }

    public GenericEvent getGenericEvent() {
        return (this.genericEventReference != null) ? this.genericEventReference.getObject() : null;
    }

    public void setGenericEvent(GenericEvent genericEvent) {
        this.genericEventReference = (genericEvent != null) ? new DomainReference<GenericEvent>(genericEvent) : null;        
    }
    
    public PunctualRoomsOccupationRequest getRoomsReserveRequest() {
	return (this.roomsReserveRequestReference != null) ? this.roomsReserveRequestReference.getObject() : null;
    }

    public void setRoomsReserveRequest(PunctualRoomsOccupationRequest request) {
	this.roomsReserveRequestReference = (request != null) ? new DomainReference<PunctualRoomsOccupationRequest>(request) : null;
    }
        
    public MultiLanguageString getCompleteDescription() {
        return completeDescription;
    }

    public void setCompleteDescription(MultiLanguageString completeDescription) {
        this.completeDescription = completeDescription;
    }

    public MultiLanguageString getSmallDescription() {
        return smallDescription;
    }

    public void setSmallDescription(MultiLanguageString smallDescription) {
        this.smallDescription = smallDescription;
    }

    public Boolean getGanttDiagramAvailable() {
        return ganttDiagramAvailable;
    }

    public void setGanttDiagramAvailable(Boolean ganttDiagramAvailable) {
        this.ganttDiagramAvailable = ganttDiagramAvailable;
    }

    public Integer getRoomsReserveRequestIdentification() {
        return roomsReserveRequestIdentification;
    }

    public void setRoomsReserveRequestIdentification(Integer roomsReserveIdentification) {
        this.roomsReserveRequestIdentification = roomsReserveIdentification;
    }

    public Boolean getMarkSaturday() {
        return markSaturday;
    }

    public void setMarkSaturday(Boolean markSaturday) {
        this.markSaturday = markSaturday;
    }

    public Boolean getMarkSunday() {
        return markSunday;
    }

    public void setMarkSunday(Boolean markSunday) {
        this.markSunday = markSunday;
    }    
}
