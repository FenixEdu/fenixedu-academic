package net.sourceforge.fenixedu.dataTransferObject.sop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.utl.fenix.utils.Pair;

public class RoomsPunctualSchedulingBean implements Serializable {

    private MultiLanguageString smallDescription;
    
    private MultiLanguageString completeDescription;
    
    private YearMonthDay begin;
    
    private YearMonthDay end;
    
    private Partial beginTime;

    private Partial endTime;
    
    private PeriodType periodType;
    
    private FrequencyType frequency;
    
    private List<DomainReference<OldRoom>> roomsReferences;
    
    private DomainReference<OldRoom> selectedRoomReference;
    
    private DomainReference<GenericEvent> genericEventReference;
    
    private Boolean ganttDiagramAvailable;
    
    private transient Locale locale = LanguageUtils.getLocale(); 
    
    public RoomsPunctualSchedulingBean() {  
	setGanttDiagramAvailable(Boolean.TRUE);	
    }       
           
    public RoomsPunctualSchedulingBean(GenericEvent genericEvent) {	
	setRooms(genericEvent.getAssociatedRooms());
	setBegin(genericEvent.getBeginDate());
	setEnd(genericEvent.getEndDate());
	setBeginTime(new Partial(genericEvent.getBeginTime()));
	setEndTime(new Partial(genericEvent.getEndTime()));	
	setFrequency(genericEvent.getFrequency());
	setSmallDescription(genericEvent.getTitle());
	setCompleteDescription(genericEvent.getDescription());
	setGenericEvent(genericEvent);
	setGanttDiagramAvailable(Boolean.TRUE);
    }
  
    public void editDailyType(YearMonthDay begin, Partial beginTime, Partial endTime) {
	setBegin(begin);
	setEnd(begin);
	setBeginTime(beginTime);
	setEndTime(endTime);	
    }
    
    public enum PeriodType {
	DAILY_TYPE, WITH_FREQUENCY, CONTINUOUS;

	public String getName() {
	    return name();
	}
    }
    
    public Pair<Integer, Integer> getTotalAvailableRoomSpace(){
	Integer availableSpaceForExam = Integer.valueOf(0), availableNormalSpace = Integer.valueOf(0);
	for (OldRoom room : getRooms()) {
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

    public List<OldRoom> getRooms() {
	List<OldRoom> result = new ArrayList<OldRoom>();	
        if(roomsReferences == null) {
            return result;
        }
	for (DomainReference<OldRoom> domainReference : roomsReferences) {
	    result.add(domainReference.getObject());
	}
	return result;
    }
    
    public void setRooms(List<OldRoom> rooms) {
	if(roomsReferences == null) {
	    roomsReferences = new ArrayList<DomainReference<OldRoom>>();
	}
	for (OldRoom room : rooms) {
	    roomsReferences.add(new DomainReference<OldRoom>(room));
	}
    }
    
    public void addNewRoom(OldRoom oldRoom) {
	if(oldRoom != null) {
            if(roomsReferences == null) {
        	roomsReferences = new ArrayList<DomainReference<OldRoom>>();     
            }
            for (DomainReference<OldRoom> domainReference : roomsReferences) {
		if(domainReference.getObject().equals(oldRoom)) {
		    return;
		}
	    }
            roomsReferences.add(new DomainReference<OldRoom>(oldRoom));
	}
    }
    
    public void removeRoom(OldRoom oldRoom) {
	if(oldRoom != null) {
            if(roomsReferences == null) {
        	roomsReferences = new ArrayList<DomainReference<OldRoom>>();     
            }           
            roomsReferences.remove(new DomainReference<OldRoom>(oldRoom));
	}
    }
       
    public OldRoom getSelectedRoom() {
        return (this.selectedRoomReference != null) ? this.selectedRoomReference.getObject() : null;
    }

    public void setSelectedRoom(OldRoom selectedRoom) {
        this.selectedRoomReference = (selectedRoom != null) ? new DomainReference<OldRoom>(selectedRoom) : null;        
    }

    public GenericEvent getGenericEvent() {
        return (this.genericEventReference != null) ? this.genericEventReference.getObject() : null;
    }

    public void setGenericEvent(GenericEvent genericEvent) {
        this.genericEventReference = (genericEvent != null) ? new DomainReference<GenericEvent>(genericEvent) : null;        
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
}
