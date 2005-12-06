package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

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
import net.sourceforge.fenixedu.domain.Building;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IBuilding;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.util.MessageResources;

public class WrittenEvaluationsByRoomBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources
            .getMessageResources("ServidorApresentacao/ApplicationResourcesSOP");

    private String name;

    private String building;

    private String floor;

    private String type;

    private String normalCapacity;

    private String examCapacity;

    private Integer executionPeriodOID;

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

    private Collection<IRoom> allRooms = null;
    private Collection<IRoom> getAllRooms() throws FenixFilterException, FenixServiceException {
        if (allRooms == null) {
            allRooms = (Collection<IRoom>) ServiceUtils.executeService(getUserView(), "ReadAllDomainObjects", new Object[] { Room.class });
        }
        return allRooms;
    }

    private Collection<IRoom> searchRooms() throws FenixFilterException, FenixServiceException {
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

        final Collection<IRoom> rooms = getAllRooms();
        final Collection<IRoom> selectedRooms = new ArrayList<IRoom>();
        for (final IRoom room : rooms) {
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

    public Collection<IRoom> getRooms() throws FenixFilterException, FenixServiceException {
        return (getRequestParameter("submittedForm") != null) ? searchRooms() : null;
    }

    public Collection<IRoom> getRoomsToDisplayMap() throws FenixFilterException, FenixServiceException {
        final Set<Integer> selectedRoomIDs = getSelectedRoomIDs();
        if (selectedRoomIDs != null) {
            return filterRooms(getAllRooms(), selectedRoomIDs);
        } else {
            final Collection<IRoom> rooms = getRooms();
            return (rooms != null && rooms.size() == 1) ? getRooms() : null;
        }
    }

    private Collection<IRoom> filterRooms(final Collection<IRoom> allRooms,
            final Set<Integer> selectedRoomIDs) {
        final Collection<IRoom> rooms = new ArrayList<IRoom>(selectedRoomIDs.size());
        for (final IRoom room : allRooms) {
            if (selectedRoomIDs.contains(room.getIdInternal())) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    public Collection<IBuilding> getBuildings() throws FenixFilterException, FenixServiceException {
        return (Collection<IBuilding>) readAllDomainObjects(Building.class);
    }

    public Collection<SelectItem> getBuildingSelectItems() throws FenixFilterException,
            FenixServiceException {
        final Collection<IBuilding> buildings = getBuildings();
        final Collection<SelectItem> buildingSelectItems = new ArrayList<SelectItem>();
        for (final IBuilding building : buildings) {
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

    public IExecutionPeriod getExecutionPeriod() throws FenixFilterException, FenixServiceException {
        final Integer executionPeriodID = getExecutionPeriodOID();
        return (executionPeriodID != null) ?
                (IExecutionPeriod) readDomainObject(ExecutionPeriod.class, executionPeriodID) : null;
    }

    public Date getCalendarBegin() throws FenixFilterException, FenixServiceException {
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        return executionPeriod.getBeginDate();
    }

    public Date getCalendarEnd() throws FenixFilterException, FenixServiceException {
        final IExecutionPeriod executionPeriod = getExecutionPeriod();
        return executionPeriod.getEndDate();
    }

    public Map<IRoom, List<CalendarLink>> getWrittenEvaluationCalendarLinks()
            throws FenixFilterException, FenixServiceException {
        final Collection<IRoom> rooms = getRoomsToDisplayMap();
        if (rooms != null) {
            final Map<IRoom, List<CalendarLink>> calendarLinksMap = new HashMap<IRoom, List<CalendarLink>>();
            // final Collection<List<CalendarLink>>
            // collectionOfCalendarLinkLists = new
            // ArrayList<List<CalendarLink>>();
            for (final IRoom room : rooms) {
                final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
                for (final IRoomOccupation roomOccupation : room.getRoomOccupations()) {
                    final IWrittenEvaluation writtenEvaluation = roomOccupation.getWrittenEvaluation();
                    if (writtenEvaluation != null) {
                        final IExecutionCourse executionCourse = writtenEvaluation
                                .getAssociatedExecutionCourses().get(0);

                        final CalendarLink calendarLink = new CalendarLink();
                        calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
                        calendarLink.setObjectLinkLabel(constructEvaluationCalendarPresentarionString(
                                writtenEvaluation, executionCourse));
                        calendarLink.setLinkParameters(constructLinkParameters(executionCourse,
                                writtenEvaluation));
                        calendarLinks.add(calendarLink);
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

    public List<Entry<IRoom, List<CalendarLink>>> getWrittenEvaluationCalendarLinksEntryList() throws FenixFilterException, FenixServiceException {
        final Map<IRoom, List<CalendarLink>> calendarLinks = getWrittenEvaluationCalendarLinks();
        return (calendarLinks != null) ? new ArrayList<Entry<IRoom, List<CalendarLink>>>(calendarLinks.entrySet()) : null;
    }

    private Map<String, String> constructLinkParameters(final IExecutionCourse executionCourse,
            final IWrittenEvaluation writtenEvaluation) {
    	final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
    	final IExecutionDegree executionDegree = findExecutionDegree(executionCourse);
    	final ICurricularSemester curricularSemester = findCurricularSemester(executionCourse);
    	final ICurricularYear curricularYear = curricularSemester.getCurricularYear();

        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
        linkParameters.put("evaluationID", writtenEvaluation.getIdInternal().toString());
        linkParameters.put("executionPeriodID", executionPeriod.getIdInternal().toString());
        linkParameters.put("executionDegreeID", executionDegree.getIdInternal().toString());
        linkParameters.put("curricularYearID", curricularYear.getIdInternal().toString());
        linkParameters.put("evaluationTypeClassname", writtenEvaluation.getClass().getName());
        linkParameters.put("executionPeriodOID", this.getExecutionPeriodOID().toString());
        return linkParameters;
    }

	private IExecutionDegree findExecutionDegree(final IExecutionCourse executionCourse) {
    	final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
    	final IExecutionYear executionYear = executionPeriod.getExecutionYear();

    	for (final ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
    		final IDegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
    		for (final IExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
    			if (executionDegree.getExecutionYear() == executionYear) {
    				return executionDegree;
    			}
    		}
    	}
    	return null;
	}

    private ICurricularSemester findCurricularSemester(final IExecutionCourse executionCourse) {
    	final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();

    	for (final ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
    		final List<ICurricularCourseScope> curricularCourseScopes = curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod);
    		if (!curricularCourseScopes.isEmpty()) {
    			final ICurricularCourseScope curricularCourseScope = curricularCourseScopes.get(0);
    			return curricularCourseScope.getCurricularSemester();
    		}
    	}
    	return null;
	}

	private String constructEvaluationCalendarPresentarionString(
            final IWrittenEvaluation writtenEvaluation, final IExecutionCourse executionCourse) {
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

}