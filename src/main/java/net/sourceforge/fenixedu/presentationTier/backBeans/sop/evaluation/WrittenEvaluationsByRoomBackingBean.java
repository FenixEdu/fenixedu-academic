package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;

import org.apache.struts.util.MessageResources;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class WrittenEvaluationsByRoomBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources
            .getMessageResources("resources/ResourceAllocationManagerResources");

    private String name;

    private String building;

    private String floor;

    private String type;

    private String normalCapacity;

    private String examCapacity;

    private String academicInterval;

    private String startDate;

    private String endDate;

    private Boolean includeEntireYear;

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

    public String getAcademicInterval() {
        return (academicInterval == null) ? academicInterval = getAndHoldStringParameter("academicInterval") : academicInterval;
    }

    protected AcademicInterval getAcademicIntervalObject() {
        return (getAcademicInterval() == null) ? null : AcademicInterval
                .getAcademicIntervalFromResumedString(getAcademicInterval());
    }

    private boolean submittedForm = false;

    public boolean getSubmittedForm() {
        return submittedForm;
    }

    public WrittenEvaluationsByRoomBackingBean() {
        if (getRequestParameter("submittedForm") != null) {
            this.submittedForm = true;
        }
        getExecutionCourseID();
    }

    private Collection<AllocatableSpace> allRooms = null;

    private Collection<AllocatableSpace> getAllRooms() throws FenixServiceException {
        if (allRooms == null) {
            allRooms = AllocatableSpace.getAllActiveAllocatableSpacesForEducation();
        }
        return allRooms;
    }

    private Set<String> selectedRoomIDs = null;

    public Set<String> getSelectedRoomIDs() {
        if (selectedRoomIDs == null) {
            final String[] selectedRoomIDStrings = getRequest().getParameterValues("selectedRoomIDs");
            if (selectedRoomIDStrings != null) {
                selectedRoomIDs = new HashSet<String>(selectedRoomIDStrings.length);
                for (final String roomIDString : selectedRoomIDStrings) {
                    selectedRoomIDs.add(roomIDString);
                }

            } else if (getRequest().getParameter("selectedRoomID") != null) {
                String roomID = getRequest().getParameter("selectedRoomID");
                selectedRoomIDs = new HashSet<String>(1);
                selectedRoomIDs.add(roomID);
            }
        }
        return selectedRoomIDs;
    }

    private Collection<AllocatableSpace> searchRooms() throws FenixServiceException {

        final String name = getName();
        final String building = (getBuilding() != null && getBuilding().length() > 0) ? getBuilding() : null;
        final Integer floor = (getFloor() != null && getFloor().length() > 0) ? Integer.valueOf(getFloor()) : null;
        final String type = getType();
        final Integer normalCapacity =
                (getNormalCapacity() != null && getNormalCapacity().length() > 0) ? Integer.valueOf(getNormalCapacity()) : null;
        final Integer examCapacity =
                (getExamCapacity() != null && getExamCapacity().length() > 0) ? Integer.valueOf(getExamCapacity()) : null;

        final Collection<AllocatableSpace> rooms = getAllRooms();
        final Collection<AllocatableSpace> selectedRooms = new ArrayList<AllocatableSpace>();

        for (final AllocatableSpace room : rooms) {
            boolean matchesCriteria = true;

            if (name != null && name.length() > 0 && !room.getNome().equalsIgnoreCase(name)) {
                matchesCriteria = false;
            } else if (building != null && !room.getBuilding().getExternalId().equals(building)) {
                matchesCriteria = false;
            } else if (floor != null && !room.getPiso().equals(floor)) {
                matchesCriteria = false;
            } else if (type != null && type.length() > 0
                    && (room.getTipo() == null || !room.getTipo().getExternalId().toString().equals(type))) {
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

    public Collection<AllocatableSpace> getRooms() throws FenixServiceException {
        return getSubmittedForm() ? searchRooms() : null;
    }

    public Collection<Building> getBuildings() throws FenixServiceException {
        return Space.getAllActiveBuildings();
    }

    public Collection<AllocatableSpace> getRoomsToDisplayMap() throws FenixServiceException {
        final Set<String> selectedRoomIDs = getSelectedRoomIDs();
        if (selectedRoomIDs != null) {
            return filterRooms(getAllRooms(), selectedRoomIDs);
        } else {
            final Collection<AllocatableSpace> rooms = getRooms();
            return (rooms != null && rooms.size() == 1) ? getRooms() : null;
        }
    }

    private Collection<AllocatableSpace> filterRooms(final Collection<AllocatableSpace> allRooms,
            final Set<String> selectedRoomIDs) {
        final Collection<AllocatableSpace> rooms = new ArrayList<AllocatableSpace>(selectedRoomIDs.size());
        for (final AllocatableSpace room : allRooms) {
            if (selectedRoomIDs.contains(room.getExternalId())) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    private static final Comparator<SelectItem> SELECT_ITEM_LABEL_COMPARATOR = new Comparator<SelectItem>() {

        @Override
        public int compare(SelectItem o1, SelectItem o2) {
            return o1.getLabel().compareTo(o2.getLabel());
        }

    };

    public Collection<SelectItem> getBuildingSelectItems() throws FenixServiceException {
        final List<Building> buildings = (List<Building>) getBuildings();
        final List<SelectItem> buildingSelectItems = new ArrayList<SelectItem>();
        for (final Building building : buildings) {
            buildingSelectItems.add(new SelectItem(building.getExternalId().toString(), building.getName()));
        }
        Collections.sort(buildingSelectItems, SELECT_ITEM_LABEL_COMPARATOR);
        return buildingSelectItems;
    }

    public Collection<SelectItem> getRoomTypeSelectItems() throws FenixServiceException {
        Collection<RoomClassification> roomClassificationsForEducation = rootDomainObject.getRoomClassification();
        final List<SelectItem> roomTypeSelectItems = new ArrayList<SelectItem>();
        for (RoomClassification classification : RoomClassification
                .sortByRoomClassificationAndCode(roomClassificationsForEducation)) {
            if (classification.hasParentRoomClassification()) {
                roomTypeSelectItems.add(new SelectItem(String.valueOf(classification.getExternalId()), classification
                        .getPresentationCode() + " - " + classification.getName().getContent(Language.getLanguage())));
            }
        }
        return roomTypeSelectItems;
    }

    @Deprecated
    public ExecutionSemester getExecutionPeriod() throws FenixServiceException {
        return (ExecutionSemester) (getAcademicIntervalObject() != null ? ExecutionSemester
                .getExecutionInterval(getAcademicIntervalObject()) : null);
    }

    public Date getCalendarBegin() throws FenixServiceException, ParseException {
        if (getStartDate() != null && getStartDate().length() > 0) {
            return DateFormatUtil.parse("dd/MM/yyyy", getStartDate());
        }
        return getAcademicIntervalObject().getStart().toDate();
    }

    public Date getCalendarEnd() throws FenixServiceException, ParseException {
        if (getEndDate() != null && getEndDate().length() > 0) {
            return DateFormatUtil.parse("dd/MM/yyyy", getEndDate());
        }
        return getAcademicIntervalObject().getEnd().toDate();
    }

    public Map<AllocatableSpace, List<CalendarLink>> getWrittenEvaluationCalendarLinks() throws FenixServiceException {
        final Collection<AllocatableSpace> rooms = getRoomsToDisplayMap();
        if (rooms != null) {
            AcademicInterval interval = getAcademicIntervalObject();
            final AcademicInterval otherAcademicInterval;
            final Boolean includeEntireYear = getIncludeEntireYear();
            if (includeEntireYear != null && includeEntireYear.booleanValue()) {
                otherAcademicInterval = interval.getPreviousAcademicInterval();
            } else {
                otherAcademicInterval = null;
            }

            final Map<AllocatableSpace, List<CalendarLink>> calendarLinksMap =
                    new HashMap<AllocatableSpace, List<CalendarLink>>();
            for (final AllocatableSpace room : rooms) {
                final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
                for (final ResourceAllocation roomOccupation : room.getResourceAllocationsSet()) {
                    if (roomOccupation.isWrittenEvaluationSpaceOccupation()) {
                        Collection<WrittenEvaluation> writtenEvaluations =
                                ((WrittenEvaluationSpaceOccupation) roomOccupation).getWrittenEvaluationsSet();
                        for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {
                            if (verifyWrittenEvaluationExecutionPeriod(writtenEvaluation, interval, otherAcademicInterval)) {
                                final ExecutionCourse executionCourse =
                                        writtenEvaluation.getAssociatedExecutionCoursesSet().iterator().next();
                                final CalendarLink calendarLink =
                                        new CalendarLink(executionCourse, writtenEvaluation, Language.getLocale());
                                calendarLink.setLinkParameters(constructLinkParameters(executionCourse, writtenEvaluation));
                                calendarLinks.add(calendarLink);
                            }
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

    protected boolean verifyWrittenEvaluationExecutionPeriod(WrittenEvaluation writtenEvaluation, AcademicInterval interval,
            AcademicInterval otherAcademicInterval) {
        for (ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getAcademicInterval().equals(interval)
                    || (otherAcademicInterval != null && executionCourse.getAcademicInterval().equals(otherAcademicInterval))) {
                return true;
            }
        }
        return false;
    }

    public List<Entry<AllocatableSpace, List<CalendarLink>>> getWrittenEvaluationCalendarLinksEntryList()
            throws FenixServiceException {
        final Map<AllocatableSpace, List<CalendarLink>> calendarLinks = getWrittenEvaluationCalendarLinks();
        return (calendarLinks != null) ? new ArrayList<Entry<AllocatableSpace, List<CalendarLink>>>(calendarLinks.entrySet()) : null;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse,
            final WrittenEvaluation writtenEvaluation) {
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        final ExecutionDegree executionDegree = findExecutionDegree(executionCourse);
        final Integer year = findCurricularYear(executionCourse);
        CurricularYear curricularYear = CurricularYear.readByYear(year);

        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("executionCourseID", executionCourse.getExternalId().toString());
        linkParameters.put("evaluationID", writtenEvaluation.getExternalId().toString());
        linkParameters.put("executionPeriodID", executionSemester.getExternalId().toString());
        linkParameters.put("executionDegreeID", executionDegree.getExternalId().toString());
        if (curricularYear != null) {
            linkParameters.put("curricularYearID", curricularYear.getExternalId().toString());
        }
        linkParameters.put("evaluationTypeClassname", writtenEvaluation.getClass().getName());
        linkParameters.put("academicInterval", getAcademicInterval());
        return linkParameters;
    }

    private ExecutionDegree findExecutionDegree(final ExecutionCourse executionCourse) {
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        final ExecutionYear executionYear = executionSemester.getExecutionYear();

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getExecutionYear() == executionYear) {
                    return executionDegree;
                }
            }
        }
        return null;
    }

    private Integer findCurricularYear(final ExecutionCourse executionCourse) {
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                if (degreeModuleScope.isActiveForExecutionPeriod(executionSemester)) {
                    return degreeModuleScope.getCurricularYear();
                }
            }
        }
        return null;
    }

    protected String constructEvaluationCalendarPresentarionString(final WrittenEvaluation writtenEvaluation,
            final ExecutionCourse executionCourse) {
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

    public Boolean getIncludeEntireYear() {
        return (includeEntireYear == null) ? includeEntireYear = getAndHoldBooleanParameter("includeEntireYear") : includeEntireYear;
    }

    public void setIncludeEntireYear(Boolean includeEntireYear) {
        this.includeEntireYear = includeEntireYear;
    }

}