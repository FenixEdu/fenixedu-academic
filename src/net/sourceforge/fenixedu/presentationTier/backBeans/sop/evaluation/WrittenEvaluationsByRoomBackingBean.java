package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.util.MessageResources;

public class WrittenEvaluationsByRoomBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");

    private String name;

    private String building;

    private String floor;

    private String type;

    private String normalCapacity;

    private String examCapacity;

    private Integer executionPeriodOID;

    private String startDate;

    private String endDate;

    public String getBuilding() {
	return building;
    }

    public void setBuilding(String building) {
	this.building = building;
    }

    public String getExamCapacity() {
	return examCapacity;
    }

    public void setExamCapacity(String examCapacity) {
	this.examCapacity = examCapacity;
    }

    public String getFloor() {
	return floor;
    }

    public void setFloor(String floor) {
	this.floor = floor;
    }

    public String getName() {
	return name;       
    }

    public void setName(String name) {
	this.name = name;        
    }

    public String getNormalCapacity() {
	return normalCapacity;
    }

    public void setNormalCapacity(String normalCapacity) {
	this.normalCapacity = normalCapacity;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public Integer getExecutionPeriodOID() {
	return (executionPeriodOID == null) ? executionPeriodOID = getAndHoldIntegerParameter("executionPeriodOID") : executionPeriodOID;
    }   

    private boolean submittedForm = false;
          
    public boolean getSubmittedForm() {
        return submittedForm;
    }

    public WrittenEvaluationsByRoomBackingBean() {
	if(getRequestParameter("submittedForm") != null) {
	    this.submittedForm = true;
	}		
	getExecutionCourseID();
    }
    
    private Collection<AllocatableSpace> allRooms = null;
    private Collection<AllocatableSpace> getAllRooms() throws FenixFilterException, FenixServiceException {
	if (allRooms == null) {
	    allRooms = AllocatableSpace.getAllActiveAllocatableSpacesForEducation();
	}
	return allRooms;
    }

    private Set<Integer> selectedRoomIDs = null;
    public Set<Integer> getSelectedRoomIDs() {
        if (selectedRoomIDs == null) {
            final String[] selectedRoomIDStrings = getRequest().getParameterValues("selectedRoomIDs");
            if (selectedRoomIDStrings != null) {
                selectedRoomIDs = new HashSet<Integer>(selectedRoomIDStrings.length);
                for (final String roomIDString : selectedRoomIDStrings) {
                    selectedRoomIDs.add(Integer.valueOf(roomIDString));
                }
            }
        }
        return selectedRoomIDs;
    }
    
    private Collection<AllocatableSpace> searchRooms() throws FenixFilterException, FenixServiceException {

	final String name = getName();
	final Integer building = (getBuilding() != null && getBuilding().length() > 0) ? Integer.valueOf(getBuilding()) : null;
	final Integer floor = (getFloor() != null && getFloor().length() > 0) ? Integer.valueOf(getFloor()) : null;
	final String type = getType();
	final Integer normalCapacity = (getNormalCapacity() != null && getNormalCapacity().length() > 0) ? Integer.valueOf(getNormalCapacity()) : null;
	final Integer examCapacity = (getExamCapacity() != null && getExamCapacity().length() > 0) ? Integer.valueOf(getExamCapacity()) : null;

	final Collection<AllocatableSpace> rooms = getAllRooms();
	final Collection<AllocatableSpace> selectedRooms = new ArrayList<AllocatableSpace>();

	for (final AllocatableSpace room : rooms) {
	    boolean matchesCriteria = true;

	    if (name != null && name.length() > 0 && !room.getNome().equalsIgnoreCase(name)) {
		matchesCriteria = false;
	    } else if (building != null && !room.getBuilding().getIdInternal().equals(building)) {
		matchesCriteria = false;
	    } else if (floor != null && !room.getPiso().equals(floor)) {
		matchesCriteria = false;
	    } else if (type != null && type.length() > 0 && (room.getTipo() == null || !room.getTipo().getIdInternal().toString().equals(type))) {
		matchesCriteria = false;
	    } else if (normalCapacity != null && room.getCapacidadeNormal().intValue() < normalCapacity.intValue()) {
		matchesCriteria = false;
	    } else if (examCapacity != null && room.getCapacidadeExame().intValue() < examCapacity.intValue()) {
		matchesCriteria = false;
	    }

	    if (matchesCriteria && room.containsIdentification()) {
		selectedRooms.add(room);
	    }                      
	}
	return selectedRooms;
    }

    public Collection<AllocatableSpace> getRooms() throws FenixFilterException, FenixServiceException {
	return getSubmittedForm() ? searchRooms() : null;
    }

    public Collection<Building> getBuildings() throws FenixFilterException, FenixServiceException {
	return Space.getAllActiveBuildings();
    }

    public Collection<AllocatableSpace> getRoomsToDisplayMap() throws FenixFilterException, FenixServiceException {
        final Set<Integer> selectedRoomIDs = getSelectedRoomIDs();
        if (selectedRoomIDs != null) {
            return filterRooms(getAllRooms(), selectedRoomIDs);
        } else {
            final Collection<AllocatableSpace> rooms = getRooms();
            return (rooms != null && rooms.size() == 1) ? getRooms() : null;
        }
    }
    
    private Collection<AllocatableSpace> filterRooms(final Collection<AllocatableSpace> allRooms, final Set<Integer> selectedRoomIDs) {
        final Collection<AllocatableSpace> rooms = new ArrayList<AllocatableSpace>(selectedRoomIDs.size());
        for (final AllocatableSpace room : allRooms) {
            if (selectedRoomIDs.contains(room.getIdInternal())) {
                rooms.add(room);
            }
        }
        return rooms;
    }
    
    public Collection<SelectItem> getBuildingSelectItems() throws FenixFilterException, FenixServiceException {
	final List<Building> buildings = (List<Building>) getBuildings();
	final List<SelectItem> buildingSelectItems = new ArrayList<SelectItem>();
	for (final Building building : buildings) {
	    buildingSelectItems.add(new SelectItem(building.getIdInternal().toString(), building.getName()));
	}
	Collections.sort(buildingSelectItems, new BeanComparator("label"));
	return buildingSelectItems;
    }

    public Collection<SelectItem> getRoomTypeSelectItems() throws FenixFilterException, FenixServiceException {
	List<RoomClassification> roomClassificationsForEducation = rootDomainObject.getRoomClassification();
	final List<SelectItem> roomTypeSelectItems = new ArrayList<SelectItem>();  	
	for (RoomClassification classification : RoomClassification.sortByRoomClassificationAndCode(roomClassificationsForEducation)) {
	    if(classification.hasParentRoomClassification()) {
		roomTypeSelectItems.add(new SelectItem(String.valueOf(classification.getIdInternal()), 
			classification.getPresentationCode() + " - " + classification.getName().getContent(LanguageUtils.getLanguage())));
	    }
	}        
	return roomTypeSelectItems;
    }

    public ExecutionPeriod getExecutionPeriod() throws FenixFilterException, FenixServiceException {
	final Integer executionPeriodID = getExecutionPeriodOID();
	return (executionPeriodID != null) ? rootDomainObject.readExecutionPeriodByOID(executionPeriodID) : null;
    }

    public Date getCalendarBegin() throws FenixFilterException, FenixServiceException, ParseException {
	if (getStartDate() != null && getStartDate().length() > 0) {
	    return DateFormatUtil.parse("dd/MM/yyyy", getStartDate());
	}
	final ExecutionPeriod executionPeriod = getExecutionPeriod();
	return executionPeriod.getBeginDate();
    }

    public Date getCalendarEnd() throws FenixFilterException, FenixServiceException, ParseException {
	if (getEndDate() != null && getEndDate().length() > 0) {
	    return DateFormatUtil.parse("dd/MM/yyyy", getEndDate());
	}
	final ExecutionPeriod executionPeriod = getExecutionPeriod();
	return executionPeriod.getEndDate();
    }

    public Map<AllocatableSpace, List<CalendarLink>> getWrittenEvaluationCalendarLinks() throws FenixFilterException, FenixServiceException {
	final Collection<AllocatableSpace> rooms = getRoomsToDisplayMap();	
	if (rooms != null) {
	    final Map<AllocatableSpace, List<CalendarLink>> calendarLinksMap = new HashMap<AllocatableSpace, List<CalendarLink>>();                
	    for (final AllocatableSpace room : rooms) {
		final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
		for (final ResourceAllocation roomOccupation : room.getResourceAllocations()) {
		    if(roomOccupation.isWrittenEvaluationSpaceOccupation()) {
			final WrittenEvaluation writtenEvaluation = ((WrittenEvaluationSpaceOccupation)roomOccupation).getWrittenEvaluation();                        
			if(verifyWrittenEvaluationExecutionPeriod(writtenEvaluation, getExecutionPeriod())) {
			    final ExecutionCourse executionCourse = writtenEvaluation.getAssociatedExecutionCourses().get(0);
			    final CalendarLink calendarLink = new CalendarLink();
			    calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
			    calendarLink.setObjectLinkLabel(constructEvaluationCalendarPresentarionString(writtenEvaluation, executionCourse));
			    calendarLink.setLinkParameters(constructLinkParameters(executionCourse, writtenEvaluation));
			    calendarLinks.add(calendarLink);
			}                        
		    }
		} 
		calendarLinksMap.put(room, calendarLinks);
	    }            
	    return calendarLinksMap;
	} else {
	    return null;
	}
    }

    private boolean verifyWrittenEvaluationExecutionPeriod(WrittenEvaluation writtenEvaluation, ExecutionPeriod executionPeriod) {
	for (ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCourses()) {
	    if(executionCourse.getExecutionPeriod() == executionPeriod) {
		return true;
	    }
	}        
	return false;
    }

    public List<Entry<AllocatableSpace, List<CalendarLink>>> getWrittenEvaluationCalendarLinksEntryList() throws FenixFilterException, FenixServiceException {
	final Map<AllocatableSpace, List<CalendarLink>> calendarLinks = getWrittenEvaluationCalendarLinks();
	return (calendarLinks != null) ? new ArrayList<Entry<AllocatableSpace, List<CalendarLink>>>(calendarLinks.entrySet()) : null;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse,
	    final WrittenEvaluation writtenEvaluation) {
	final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
	final ExecutionDegree executionDegree = findExecutionDegree(executionCourse);
	final Integer year = findCurricularYear(executionCourse);        
	CurricularYear curricularYear = CurricularYear.readByYear(year);

	final Map<String, String> linkParameters = new HashMap<String, String>();
	linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
	linkParameters.put("evaluationID", writtenEvaluation.getIdInternal().toString());
	linkParameters.put("executionPeriodID", executionPeriod.getIdInternal().toString());
	linkParameters.put("executionDegreeID", executionDegree.getIdInternal().toString());
	if (curricularYear != null) {
	    linkParameters.put("curricularYearID", curricularYear.getIdInternal().toString());    
	} 
	linkParameters.put("evaluationTypeClassname", writtenEvaluation.getClass().getName());
	linkParameters.put("executionPeriodOID", this.getExecutionPeriodOID().toString());
	return linkParameters;
    }

    private ExecutionDegree findExecutionDegree(final ExecutionCourse executionCourse) {
	final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
	final ExecutionYear executionYear = executionPeriod.getExecutionYear();

	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
	    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
		if (executionDegree.getExecutionYear() == executionYear) {
		    return executionDegree;
		}
	    }
	}
	return null;
    }

    private Integer findCurricularYear(final ExecutionCourse executionCourse) {
	final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();

	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {    		
	    for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
		if(degreeModuleScope.isActiveForExecutionPeriod(executionPeriod)) {
		    return degreeModuleScope.getCurricularYear();
		}
	    }    	
	}
	return null;
    }

    private String constructEvaluationCalendarPresentarionString(
	    final WrittenEvaluation writtenEvaluation, final ExecutionCourse executionCourse) {
	final StringBuilder stringBuilder = new StringBuilder();
	if (writtenEvaluation instanceof WrittenTest) {
	    stringBuilder.append(messages.getMessage("label.evaluation.shortname.test"));
	} else if (writtenEvaluation instanceof Exam) {
	    stringBuilder.append(messages.getMessage("label.evaluation.shortname.exam"));
	}
	stringBuilder.append(" ");
	stringBuilder.append(executionCourse.getSigla());
	stringBuilder.append(" (");
	stringBuilder.append(DateFormatUtil.format("HH:mm", writtenEvaluation.getBeginning().getTime()));
	stringBuilder.append(")");
	return stringBuilder.toString();
    }

    public String getEndDate() {
	return (endDate == null) ? endDate = getAndHoldStringParameter("endDate") : endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate = endDate;
    }

    public String getStartDate() {
	return (startDate == null) ? startDate = getAndHoldStringParameter("startDate") : startDate;
    }

    public void setStartDate(String startDate) {
	this.startDate = startDate;
    }

}