package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.util.MessageResources;

public class WrittenEvaluationsByRoomBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources
            .getMessageResources("resources/ApplicationResourcesSOP");

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
        return (executionPeriodOID == null) ? executionPeriodOID = getAndHoldIntegerParameter("executionPeriodOID")
                : executionPeriodOID;
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

    private Collection<OldRoom> allRooms = null;
    private Collection<OldRoom> getAllRooms() throws FenixFilterException, FenixServiceException {
        if (allRooms == null) {
            allRooms = OldRoom.getOldRooms();
        }
        return allRooms;
    }

    private Collection<OldRoom> searchRooms() throws FenixFilterException, FenixServiceException {
        final String name = getName();
        final Integer building = (getBuilding() != null && getBuilding().length() > 0) ? Integer
                .valueOf(getBuilding()) : null;
        final Integer floor = (getFloor() != null && getFloor().length() > 0) ? Integer
                .valueOf(getFloor()) : null;
        final String type = getType();
        final Integer normalCapacity = (getNormalCapacity() != null && getNormalCapacity().length() > 0) ? Integer
                .valueOf(getNormalCapacity())
                : null;
        final Integer examCapacity = (getExamCapacity() != null && getExamCapacity().length() > 0) ? Integer
                .valueOf(getExamCapacity())
                : null;

        final Collection<OldRoom> rooms = getAllRooms();
        final Collection<OldRoom> selectedRooms = new ArrayList<OldRoom>();
        for (final OldRoom room : rooms) {
            boolean matchesCriteria = true;
            if (name != null && name.length() > 0 && !room.getNome().equalsIgnoreCase(name)) {
                matchesCriteria = false;
            } else if (building != null && !room.getBuilding().getIdInternal().equals(building)) {
                matchesCriteria = false;
            } else if (floor != null && !room.getPiso().equals(floor)) {
                matchesCriteria = false;
            } else if (type != null && type.length() > 0 && !room.getTipo().toString().equals(type)) {
                matchesCriteria = false;
            } else if (normalCapacity != null
                    && room.getCapacidadeNormal().intValue() < normalCapacity.intValue()) {
                matchesCriteria = false;
            } else if (examCapacity != null
                    && room.getCapacidadeExame().intValue() < examCapacity.intValue()) {
                matchesCriteria = false;
            }
            if (matchesCriteria) {
                selectedRooms.add(room);
            }
        }
        return selectedRooms;
    }

    public Collection<OldRoom> getRooms() throws FenixFilterException, FenixServiceException {
        return (getRequestParameter("submittedForm") != null) ? searchRooms() : null;
    }

    public Collection<OldRoom> getRoomsToDisplayMap() throws FenixFilterException, FenixServiceException {
        final Set<Integer> selectedRoomIDs = getSelectedRoomIDs();
        if (selectedRoomIDs != null) {
            return filterRooms(getAllRooms(), selectedRoomIDs);
        } else {
            final Collection<OldRoom> rooms = getRooms();
            return (rooms != null && rooms.size() == 1) ? getRooms() : null;
        }
    }

    private Collection<OldRoom> filterRooms(final Collection<OldRoom> allRooms,
            final Set<Integer> selectedRoomIDs) {
        final Collection<OldRoom> rooms = new ArrayList<OldRoom>(selectedRoomIDs.size());
        for (final OldRoom room : allRooms) {
            if (selectedRoomIDs.contains(room.getIdInternal())) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    public Collection<OldBuilding> getBuildings() throws FenixFilterException, FenixServiceException {
        return OldBuilding.getOldBuildings();
    }

    public Collection<SelectItem> getBuildingSelectItems() throws FenixFilterException,
            FenixServiceException {
        final Collection<OldBuilding> buildings = getBuildings();
        final Collection<SelectItem> buildingSelectItems = new ArrayList<SelectItem>();
        for (final OldBuilding building : buildings) {
            buildingSelectItems.add(new SelectItem(building.getIdInternal().toString(), building.getName()));
        }
        return buildingSelectItems;
    }

    public Collection<SelectItem> getRoomTypeSelectItems() throws FenixFilterException,
            FenixServiceException {
        final Collection<SelectItem> roomTypeSelectItems = new ArrayList<SelectItem>();
        roomTypeSelectItems.add(new SelectItem(Integer.valueOf(TipoSala.ANFITEATRO).toString(), messages
                .getMessage("room.tyoe.anfitiatro")));
        roomTypeSelectItems.add(new SelectItem(Integer.valueOf(TipoSala.PLANA).toString(), messages
                .getMessage("room.type.normal")));
        roomTypeSelectItems.add(new SelectItem(Integer.valueOf(TipoSala.LABORATORIO).toString(),
                messages.getMessage("room.typr.lab")));
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

    public Map<OldRoom, List<CalendarLink>> getWrittenEvaluationCalendarLinks()
            throws FenixFilterException, FenixServiceException {
        final Collection<OldRoom> rooms = getRoomsToDisplayMap();
        if (rooms != null) {
            final Map<OldRoom, List<CalendarLink>> calendarLinksMap = new HashMap<OldRoom, List<CalendarLink>>();
            // final Collection<List<CalendarLink>>
            // collectionOfCalendarLinkLists = new
            // ArrayList<List<CalendarLink>>();                  
            for (final OldRoom room : rooms) {
                final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
                for (final RoomOccupation roomOccupation : room.getRoomOccupations()) {
                    final WrittenEvaluation writtenEvaluation = roomOccupation.getWrittenEvaluation();
                    if (writtenEvaluation != null) {                     
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
                // collectionOfCalendarLinkLists.add(calendarLinks);
                calendarLinksMap.put(room, calendarLinks);
            }
            // return collectionOfCalendarLinkLists;
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

    public List<Entry<OldRoom, List<CalendarLink>>> getWrittenEvaluationCalendarLinksEntryList() throws FenixFilterException, FenixServiceException {
        final Map<OldRoom, List<CalendarLink>> calendarLinks = getWrittenEvaluationCalendarLinks();
        return (calendarLinks != null) ? new ArrayList<Entry<OldRoom, List<CalendarLink>>>(calendarLinks.entrySet()) : null;
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
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}