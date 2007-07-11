package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public abstract class AllocatableSpace extends AllocatableSpace_Base {
    
    public final static Comparator<AllocatableSpace> ROOM_COMPARATOR_BY_NAME = new ComparatorChain();        
    static {
	((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("identification", Collator.getInstance()));
	((ComparatorChain) ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("idInternal"));    
    }
    
    public abstract List<ResourceAllocation> getResourceAllocationsForCheck();
    
    public abstract String getIdentification();

    public abstract RoomClassification getRoomClassification();
    
    public abstract Integer getNormalCapacity();
    
    public abstract Integer getExamCapacity();     
    
    public abstract void setNormalCapacity(Integer capacidadeNormal);

    public abstract void setExamCapacity(Integer capacidadeExame);
    
    @Deprecated
    public abstract Integer getCapacidadeNormal();
    
    @Deprecated
    public abstract Integer getCapacidadeExame();
    
    @Deprecated
    public abstract RoomClassification getTipo();

    
    protected AllocatableSpace() {
        super();        
    }
           
    @Deprecated
    public String getNome() {
	return getIdentification();
    }
    
    @Deprecated
    public String getName() {
	return getIdentification();
    }
        
    @Deprecated
    public Building getBuilding() {
	return getSpaceBuilding();
    }
    
    @Deprecated
    public Integer getPiso() {
	Floor floor = getSpaceFloor();
	return floor != null ? floor.getSpaceInformation().getLevel() : null;
    }

    @Override
    public boolean isAllocatableSpace() {
        return true;
    }       
       
    public boolean containsIdentification() {	
	return !StringUtils.isEmpty(getIdentification());
    }   
           
    public static List<AllocatableSpace> readAllAllocatableSpacesByName(String name){
	List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();	
	String[] identificationWords = getIdentificationWords(name);
	for (Resource resource : RootDomainObject.getInstance().getResources()) {	    
	    if (resource.isAllocatableSpace() && ((Space)resource).verifyNameEquality(identificationWords)) {
		result.add((AllocatableSpace) resource);
	    }
	}
	return result;
    }                   
    
    public static void mergeAllocatableSpaces(AllocatableSpace fromRoom, AllocatableSpace destinationRoom) {
	
	if(fromRoom == null || destinationRoom == null || fromRoom.equals(destinationRoom)) {
	    throw new DomainException("error.room.invalid.rooms.for.merge");
	}
	
	Space destinationRoomParent = destinationRoom.getSuroundingSpace();
	while(destinationRoomParent != null) {
	    if(destinationRoomParent.equals(fromRoom)) {
		throw new DomainException("error.merge.rooms.from.room.is.parent.of.destination.room");
	    }
	    destinationRoomParent = destinationRoomParent.getSuroundingSpace();
	}
		
	destinationRoom.getResourceResponsibility().addAll(fromRoom.getResourceResponsibility());
	destinationRoom.getResourceAllocations().addAll(fromRoom.getResourceAllocations());
	destinationRoom.getContainedSpaces().addAll(fromRoom.getContainedSpaces());	
	destinationRoom.getAssociatedInquiriesRooms().addAll(fromRoom.getAssociatedInquiriesRooms());	
	destinationRoom.getAssociatedSummaries().addAll(fromRoom.getAssociatedSummaries());
	destinationRoom.getWrittenEvaluationEnrolments().addAll(fromRoom.getWrittenEvaluationEnrolments());
	destinationRoom.setNormalCapacity(fromRoom.getNormalCapacity());
	destinationRoom.setExamCapacity(fromRoom.getExamCapacity());
	
	fromRoom.delete();
    }
    
    public boolean isForEducation() {
	Group lessonGroup = getLessonOccupationsAccessGroup();
	Group writtenEvaluationGroup = getWrittenEvaluationOccupationsAccessGroup();
	return (lessonGroup != null && lessonGroup.getElementsCount() > 0) ||
		(writtenEvaluationGroup != null && writtenEvaluationGroup.getElementsCount() > 0);
    }
    
    public static AllocatableSpace findAllocatableSpaceForEducationByName(String name) {
        for (Resource space : RootDomainObject.getInstance().getResources()) {
            if (space.isAllocatableSpace() && ((AllocatableSpace)space).isForEducation() 
        	    && ((AllocatableSpace)space).getIdentification().equalsIgnoreCase(name)) {
        	return (AllocatableSpace) space;
            }
        }
        return null;
    }
    
    public static AllocatableSpace findActiveAllocatableSpaceForEducationByName(String name) {
        AllocatableSpace allocatableSpace = findAllocatableSpaceForEducationByName(name);
        if(allocatableSpace != null && allocatableSpace.isActive()) {
            return allocatableSpace;
        }
        return null;
    }

    public static List<AllocatableSpace> getAllActiveAllocatableSpacesExceptLaboratoriesForEducation() {	
        List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
        for (Resource space : RootDomainObject.getInstance().getResources()) {
            if (space.isAllocatableSpace() && ((AllocatableSpace)space).isActive() && ((AllocatableSpace)space).isForEducation()) {
        	RoomClassification roomClassification = (((AllocatableSpace)space)).getRoomClassification();
        	if(roomClassification == null || 
        		(!roomClassification.getPresentationCode().equals(RoomClassification.LABORATORY_FOR_EDUCATION_CODE) &&
        			!roomClassification.getPresentationCode().equals(RoomClassification.LABORATORY_FOR_RESEARCHER_CODE))) {
        	    result.add((AllocatableSpace) space);    
        	}				
            }		    		    
        }
        return result;
    }

    public static List<AllocatableSpace> getAllActiveAllocatableSpacesForEducation() {	
        List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
        for (Resource space : RootDomainObject.getInstance().getResources()) {
            if (space.isAllocatableSpace() && ((AllocatableSpace)space).isActive()
        	    && ((AllocatableSpace)space).isForEducation()) {
        	result.add((AllocatableSpace) space);
            }
        }
        return result;
    }   
    
    public static List<AllocatableSpace> findActiveAllocatableSpacesForEducationWithNormalCapacity(Integer normalCapacity) {
	List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
	if(normalCapacity != null) {
            for (Resource space : RootDomainObject.getInstance().getResources()) {
                if (space.isAllocatableSpace() && ((AllocatableSpace)space).isActive() && ((AllocatableSpace)space).isForEducation() 
                	&& ((AllocatableSpace)space).getNormalCapacity() != null 
                	&& ((AllocatableSpace)space).getNormalCapacity().equals(normalCapacity)) {
                    result.add((AllocatableSpace) space);
                }
            }
	}
	return result;
    }   
    
    public static List<AllocatableSpace> findActiveAllocatableSpacesForEducationByRoomType(RoomClassification roomType) {
	List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
	if(roomType != null) {
            for (Resource space : RootDomainObject.getInstance().getResources()) {
                if (space.isAllocatableSpace() && ((AllocatableSpace)space).isActive() && ((AllocatableSpace)space).isForEducation()  
                	&& ((AllocatableSpace)space).getRoomClassification() != null 
                	&& ((AllocatableSpace)space).getRoomClassification().equals(roomType)) {
                    result.add((AllocatableSpace) space);
                }
            }
	}
	return result;
    } 
    
    public boolean isFree(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTime, 
	    HourMinuteSecond endTime, DiaSemana dayOfWeek, FrequencyType frequency, Integer week,
	    Boolean dailyFrequencyMarkSaturday, Boolean dailyFrequencyMarkSunday) {
	
	for (ResourceAllocation spaceOccupation : getResourceAllocationsForCheck()) {
	    if(spaceOccupation.isEventSpaceOccupation()) {
		EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
		if (occupation.alreadyWasOccupiedIn(startDate, endDate, startTime, endTime, dayOfWeek,
			frequency, week, dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday)) {
		    return false;
		}
	    }
	}		
	return true;
    }
    
    public boolean isFree(EventSpaceOccupation occupationToNotCheck) {	
	for (ResourceAllocation spaceOccupation : getResourceAllocationsForCheck()) {
	    if(spaceOccupation.isEventSpaceOccupation()) {
		EventSpaceOccupation occupation = (EventSpaceOccupation) spaceOccupation;
		if (!occupation.equals(occupationToNotCheck) 
			&& occupation.alreadyWasOccupiedBy(occupationToNotCheck)) {
		    return false;
		}
	    }
	}	
	return true;
    }
                 
    public List<Lesson> getAssociatedLessons(final ExecutionPeriod executionPeriod) {
	final List<Lesson> lessons = new ArrayList<Lesson>();
	for (ResourceAllocation spaceOccupation : getResourceAllocations()) {
	    if(spaceOccupation.isLessonSpaceOccupation()) {
		LessonSpaceOccupation roomOccupation = (LessonSpaceOccupation) spaceOccupation;	    
		final Lesson lesson = roomOccupation.getLesson();
		if (lesson.getExecutionPeriod() == executionPeriod) {
		    lessons.add(lesson);
		}
	    }
	}
	return lessons;
    }       

    public static List<AllocatableSpace> findActiveAllocatableSpacesBySpecifiedArguments(String nome, String edificio,
	    Integer piso, RoomClassification tipo, Integer capacidadeNormal, Integer capacidadeExame)
	    throws ExcepcaoPersistencia {
	
	final List<AllocatableSpace> activeRoomsForEducation = AllocatableSpace.getAllActiveAllocatableSpacesForEducation();
	final List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
	
	for (AllocatableSpace room : activeRoomsForEducation) {
	    
	    if (nome != null && !room.getName().equalsIgnoreCase(nome)) {
		continue;
	    }	    
	    Building spaceBuilding = room.getSpaceBuilding();
	    if (edificio != null && (spaceBuilding == null || !spaceBuilding.getName().equalsIgnoreCase(edificio))) {
		continue;
	    }	    
	    Integer spaceFloor = room.getPiso();
	    if (piso != null && (spaceFloor == null || !spaceFloor.equals(piso))) {
		continue;
	    }
	    RoomClassification roomClassification = room.getRoomClassification();
	    if (tipo != null && (roomClassification == null || !roomClassification.equals(tipo))) {
		continue;
	    }
	    if (capacidadeNormal != null && (room.getNormalCapacity() == null || room.getNormalCapacity().intValue() < capacidadeNormal.intValue())) {
		continue;
	    }
	    if (capacidadeExame != null  && (room.getExamCapacity() == null || room.getExamCapacity().intValue() < capacidadeExame.intValue())) {
		continue;
	    }
	    
	    result.add(room);	   
	}
	return result;
    }   
}

