/*
 * Created on 08/Mar/2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
import net.sourceforge.fenixedu.util.ProposalState;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author joaosa & rmalo
 * 
 */
public class Grouping extends Grouping_Base {

    public static Comparator<Grouping> COMPARATOR_BY_ENROLMENT_BEGIN_DATE = new Comparator<Grouping>() {

        @Override
        public int compare(Grouping g1, Grouping g2) {
            return g1.getEnrolmentBeginDayDateDateTime().compareTo(g2.getEnrolmentBeginDayDateDateTime());
        }

    };

    public Grouping() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Calendar getEnrolmentBeginDay() {
        if (this.getEnrolmentBeginDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrolmentBeginDayDate());
            return result;
        }
        return null;
    }

    public void setEnrolmentBeginDay(Calendar enrolmentBeginDay) {
        if (enrolmentBeginDay != null) {
            this.setEnrolmentBeginDayDate(enrolmentBeginDay.getTime());
        } else {
            this.setEnrolmentBeginDayDate(null);
        }
    }

    public Calendar getEnrolmentEndDay() {
        if (this.getEnrolmentEndDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrolmentEndDayDate());
            return result;
        }
        return null;
    }

    public void setEnrolmentEndDay(Calendar enrolmentEndDay) {
        if (enrolmentEndDay != null) {
            this.setEnrolmentEndDayDate(enrolmentEndDay.getTime());
        } else {
            this.setEnrolmentEndDayDate(null);
        }
    }

    public List<ExecutionCourse> getExecutionCourses() {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final ExportGrouping exportGrouping : this.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                result.add(exportGrouping.getExecutionCourse());
            }
        }
        return result;
    }

    public List<StudentGroup> getStudentGroupsWithoutShift() {
        final List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (final StudentGroup studentGroup : this.getStudentGroupsSet()) {
            if (studentGroup.getShift() == null) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public List<StudentGroup> getStudentGroupsWithShift() {
        final List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (final StudentGroup studentGroup : this.getStudentGroupsSet()) {
            if (studentGroup.getShift() != null) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public Integer getNumberOfStudentsNotInGrouping() {
        int numberOfStudents = 0;
        for (final ExportGrouping exportGrouping : this.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                for (final Attends attend : exportGrouping.getExecutionCourse().getAttends()) {
                    if (!this.getAttends().contains(attend)) {
                        numberOfStudents++;
                    }
                }
            }
        }
        return Integer.valueOf(numberOfStudents);
    }

    public void checkShiftCapacity(Shift shift) {
        List shiftStudentGroups = this.readAllStudentGroupsBy(shift);
        Integer groupMaximumNumber;
        if (this.getDifferentiatedCapacity()) {
            groupMaximumNumber = shift.getShiftGroupingProperties().getCapacity();
        } else {
            groupMaximumNumber = this.getGroupMaximumNumber();
        }
        if (shiftStudentGroups != null && groupMaximumNumber != null && shiftStudentGroups.size() == groupMaximumNumber) {
            throw new DomainException(this.getClass().getName(), "error.shift.with.max.number.of.studentGroups");
        }
    }

    public Integer getNumberOfStudentsInGrouping() {
        return this.getAttends().size();
    }

    public Attends getStudentAttend(final Registration registration) {
        for (final Attends attend : this.getAttends()) {
            if (attend.getRegistration().getStudent() == registration.getStudent()) {
                return attend;
            }
        }
        return null;
    }

    public Attends getStudentAttend(String studentUsername) {
        for (final Attends attend : this.getAttends()) {
            if (attend.getRegistration().getPerson().getUsername().equals(studentUsername)) {
                return attend;
            }
        }
        return null;
    }

    public StudentGroup readStudentGroupBy(Integer studentGroupNumber) {
        for (final StudentGroup studentGroup : this.getStudentGroupsSet()) {
            if (studentGroup.getGroupNumber().equals(studentGroupNumber)) {
                return studentGroup;
            }
        }
        return null;
    }

    public List<StudentGroup> readAllStudentGroupsBy(Shift shift) {
        final List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (final StudentGroup studentGroup : this.getStudentGroupsSet()) {
            if (studentGroup.getShift() == shift) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public static Grouping create(String goupingName, Date enrolmentBeginDay, Date enrolmentEndDay,
            EnrolmentGroupPolicyType enrolmentGroupPolicyType, Integer groupMaximumNumber, Integer idealCapacity,
            Integer maximumCapacity, Integer minimumCapacity, String projectDescription, ShiftType shiftType,
            Boolean automaticEnrolment, Boolean differentiatedCapacity, ExecutionCourse executionCourse) {

        if (goupingName == null || enrolmentBeginDay == null || enrolmentEndDay == null || enrolmentGroupPolicyType == null) {
            throw new NullPointerException();
        }

        checkIfGroupingAlreadyExistInExecutionCourse(goupingName, executionCourse);

        Grouping grouping = new Grouping();
        grouping.setName(goupingName);
        grouping.setEnrolmentBeginDayDate(enrolmentBeginDay);
        grouping.setEnrolmentEndDayDate(enrolmentEndDay);
        grouping.setEnrolmentPolicy(enrolmentGroupPolicyType);
        grouping.setGroupMaximumNumber(groupMaximumNumber);
        grouping.setIdealCapacity(idealCapacity);
        grouping.setMaximumCapacity(maximumCapacity);
        grouping.setMinimumCapacity(minimumCapacity);
        grouping.setProjectDescription(projectDescription);
        grouping.setShiftType(shiftType);
        grouping.setAutomaticEnrolment(automaticEnrolment);
        grouping.setDifferentiatedCapacity(differentiatedCapacity);

        ExportGrouping exportGrouping = new ExportGrouping(grouping, executionCourse);
        exportGrouping.setProposalState(new ProposalState(ProposalState.CRIADOR));

        addGroupingToAttends(grouping, executionCourse.getAttends());
        GroupsAndShiftsManagementLog.createLog(executionCourse, "resources.MessagingResources",
                "log.executionCourse.groupAndShifts.grouping.added", grouping.getName(), executionCourse.getNome(),
                executionCourse.getDegreePresentationString());
        return grouping;
    }

    private static void addGroupingToAttends(final Grouping grouping, final Collection<Attends> attends) {
        for (final Attends attend : attends) {
            attend.addGroupings(grouping);
        }
    }

    private static void checkIfGroupingAlreadyExistInExecutionCourse(final String goupingName,
            final ExecutionCourse executionCourse) {
        if (executionCourse.getGroupingByName(goupingName) != null) {
            throw new DomainException("error.exception.existing.groupProperties");
        }
    }

    public void createOrEditShiftGroupingProperties(List<InfoShift> infoShifts) {
        for (final InfoShift info : infoShifts) {
            Shift shift = info.getShift();
            if (shift.hasShiftGroupingProperties()) {
                shift.getShiftGroupingProperties().setCapacity(info.getGroupCapacity());
            } else {
                shift.setShiftGroupingProperties(new ShiftGroupingProperties(shift, this, info.getGroupCapacity()));
            }
        }
    }

    public void edit(String goupingName, Date enrolmentBeginDay, Date enrolmentEndDay,
            EnrolmentGroupPolicyType enrolmentGroupPolicyType, Integer groupMaximumNumber, Integer idealCapacity,
            Integer maximumCapacity, Integer minimumCapacity, String projectDescription, ShiftType shiftType,
            Boolean automaticEnrolment, Boolean differentiatedCapacity, List<InfoShift> infoShifts) {

        if (goupingName == null || enrolmentBeginDay == null || enrolmentEndDay == null || enrolmentGroupPolicyType == null) {
            throw new NullPointerException();
        }

        checkIfGroupingAlreadyExists(goupingName);

        if (getDifferentiatedCapacity() && !differentiatedCapacity) {
            if (!super.getStudentGroupsSet().isEmpty()) {
                throw new DomainException(this.getClass().getName(), "error.groupProperties.edit.attendsSet.withGroups");
            }

            Collection<ShiftGroupingProperties> shiftGroupingProperties = this.getShiftGroupingProperties();
            for (ShiftGroupingProperties shiftGP : shiftGroupingProperties) {
                shiftGP.delete();
            }
        } else if (getDifferentiatedCapacity() && differentiatedCapacity && isShiftTypeDifferent(shiftType)) {
            if (!super.getStudentGroupsSet().isEmpty()) {
                throw new DomainException(this.getClass().getName(), "error.groupProperties.edit.attendsSet.withGroups");
            }

            Collection<ShiftGroupingProperties> shiftGroupingProperties = this.getShiftGroupingProperties();
            for (ShiftGroupingProperties shiftGP : shiftGroupingProperties) {
                shiftGP.delete();
            }
        }

        Integer groupMaximumNumberFix;

        if (groupMaximumNumber == null) {
            groupMaximumNumberFix = Integer.MAX_VALUE;
        } else {
            groupMaximumNumberFix = groupMaximumNumber;
        }

        if (!differentiatedCapacity && groupMaximumNumberFix < getMaxStudentGroupsCount()) {
            throw new DomainException(this.getClass().getName(),
                    "error.groupProperties.edit.maxGroupCap.inferiorToExistingNumber");
        } else if (getDifferentiatedCapacity() && differentiatedCapacity && isShiftTypeEqual(shiftType)) {
            for (InfoShift infoshift : infoShifts) {
                if (getStudentGroupsIndexedByShift().containsKey(infoshift.getShift())) {
                    if (infoshift.getGroupCapacity() < getStudentGroupsIndexedByShift().get(infoshift.getShift()).size()) {
                        throw new DomainException(this.getClass().getName(),
                                "error.groupProperties.edit.maxGroupCap.inferiorToExistingNumber");
                    }
                }
            }
        }

        setName(goupingName);
        setEnrolmentBeginDayDate(enrolmentBeginDay);
        setEnrolmentEndDayDate(enrolmentEndDay);
        setEnrolmentPolicy(enrolmentGroupPolicyType);
        setGroupMaximumNumber(groupMaximumNumber);
        setIdealCapacity(idealCapacity);
        setMaximumCapacity(maximumCapacity);
        setMinimumCapacity(minimumCapacity);
        setProjectDescription(projectDescription);
        setShiftType(shiftType);
        setAutomaticEnrolment(automaticEnrolment);
        setDifferentiatedCapacity(differentiatedCapacity);

        if (differentiatedCapacity) {
            createOrEditShiftGroupingProperties(infoShifts);
        } else {
            Collection<ShiftGroupingProperties> shiftGroupingProperties = this.getShiftGroupingProperties();
            for (ShiftGroupingProperties shiftGP : shiftGroupingProperties) {
                shiftGP.delete();
            }
        }

        if (shiftType == null) {
            unEnrollStudentGroups(this.getStudentGroupsSet());
        }

        List<ExecutionCourse> ecs = getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.groupAndShifts.grouping.edited", getName(), ec.getNome(),
                    ec.getDegreePresentationString());
        }
    }

    private boolean isShiftTypeEqual(ShiftType shiftType) {
        return ((shiftType != null && getShiftType() != null && getShiftType().compareTo(shiftType) == 0) || (shiftType == null && getShiftType() == null));
    }

    private boolean isShiftTypeDifferent(ShiftType shiftType) {
        return (((shiftType == null && getShiftType() != null) || (shiftType != null && getShiftType() == null)) || getShiftType()
                .compareTo(shiftType) != 0);
    }

    private Integer getMaxStudentGroupsCount() {
        final Map<Object, Integer> shiftCountMap = new HashMap<Object, Integer>();
        for (final StudentGroup studentGroup : super.getStudentGroupsSet()) {
            if (!studentGroup.wasDeleted()) {
                final Shift shift = studentGroup.getShift();
                final int count = shiftCountMap.containsKey(shift) ? shiftCountMap.get(shift) + 1 : 1;
                shiftCountMap.put(shift, Integer.valueOf(count));
            }
        }
        int max = 0;
        for (final Integer i : shiftCountMap.values()) {
            if (i.intValue() > max) {
                max = i.intValue();
            }
        }
        return max;
    }

    private void checkIfGroupingAlreadyExists(String groupingName) {
        if (!this.getName().equals(groupingName)) {
            for (final ExecutionCourse executionCourse : this.getExecutionCourses()) {
                if (executionCourse.getGroupingByName(groupingName) != null) {
                    throw new DomainException(this.getClass().getName(), "error.exception.existing.groupProperties");
                }
            }
        }
    }

    private void unEnrollStudentGroups(Collection<StudentGroup> studentGroups) {
        for (final StudentGroup studentGroup : studentGroups) {
            studentGroup.setShift(null);
        }
    }

    public void createStudentGroup(Shift shift, Integer groupNumber, List<Registration> students) {

        if (groupNumber == null || students == null) {
            throw new NullPointerException();
        }

        if (readStudentGroupBy(groupNumber) != null) {
            throw new DomainException(this.getClass().getName(), "error.invalidGroupNumber");
        }

        checkForStudentsInStudentGroupsAndGrouping(students);

        StudentGroup newStudentGroup = null;
        if (shift != null) {
            newStudentGroup = new StudentGroup(groupNumber, this, shift);
        } else {
            newStudentGroup = new StudentGroup(groupNumber, this);
        }

        StringBuilder sbStudentNumbers = new StringBuilder("");
        sbStudentNumbers.setLength(0);
        for (Registration registration : students) {
            Attends attend = getStudentAttend(registration);
            newStudentGroup.addAttends(attend);
            if (sbStudentNumbers.length() != 0) {
                sbStudentNumbers.append(", " + registration.getNumber().toString());
            } else {
                sbStudentNumbers.append(registration.getNumber().toString());
            }
        }

        final String labelKey;
        if (sbStudentNumbers.length() == 0) {
            labelKey = "log.executionCourse.groupAndShifts.grouping.group.added.empty";
        } else {
            labelKey = "log.executionCourse.groupAndShifts.grouping.group.added";
        }

        List<ExecutionCourse> ecs = getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources", labelKey, groupNumber.toString(),
                    getName(), Integer.toString(students.size()), sbStudentNumbers.toString(), ec.getNome(),
                    ec.getDegreePresentationString());
        }
    }

    private void checkForStudentsInStudentGroupsAndGrouping(List<Registration> students) {
        for (Registration registration : students) {
            Attends attend = getStudentAttend(registration);
            for (final StudentGroup studentGroup : this.getStudentGroupsSet()) {
                if (studentGroup.getAttends().contains(attend)) {
                    throw new DomainException(this.getClass().getName(), "errors.existing.studentEnrolment");
                } else if (!this.getAttends().contains(attend)) {
                    throw new DomainException(this.getClass().getName(), "errors.notExisting.studentInGrouping");
                }
            }
        }
    }

    public void delete() {

        if (!super.getStudentGroupsSet().isEmpty()) {
            throw new DomainException(this.getClass().getName(), "");
        }

        if (getGroupingGroup() != null) {
            throw new DomainException("error.grouping.cannotDeleteGroupingUsedInAccessControl");
        }

        List<ExecutionCourse> ecs = getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.groupAndShifts.grouping.removed", getName(), ec.getNome(),
                    ec.getDegreePresentationString());
        }

        Collection<Attends> attends = this.getAttends();
        List<Attends> attendsAux = new ArrayList<Attends>();
        attendsAux.addAll(attends);
        for (Attends attend : attendsAux) {
            attend.removeGroupings(this);
        }

        Collection<ExportGrouping> exportGroupings = this.getExportGroupings();
        List<ExportGrouping> exportGroupingsAux = new ArrayList<ExportGrouping>();
        exportGroupingsAux.addAll(exportGroupings);
        for (ExportGrouping exportGrouping : exportGroupingsAux) {
            ExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
            executionCourse.removeExportGroupings(exportGrouping);
            exportGrouping.delete();
        }

        for (ShiftGroupingProperties shiftGP : getShiftGroupingProperties()) {
            shiftGP.delete();
        }

        for (Project project : getProjects()) {
            project.delete();
        }

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public int findMaxGroupNumber() {
        int max = 0;
        for (final StudentGroup studentGroup : super.getStudentGroupsSet()) {
            max = Math.max(max, studentGroup.getGroupNumber().intValue());
        }
        return max;
    }

    public ExportGrouping getExportGrouping(final ExecutionCourse executionCourse) {
        for (final ExportGrouping exportGrouping : this.getExportGroupingsSet()) {
            if (exportGrouping.getExecutionCourse() == executionCourse) {
                return exportGrouping;
            }
        }
        return null;
    }

    public boolean hasExportGrouping(final ExecutionCourse executionCourse) {
        return getExportGrouping(executionCourse) != null;
    }

    public StudentGroup getStudentGroupByAttends(Attends attends) {
        for (StudentGroup studentGroup : getStudentGroupsSet()) {
            if (studentGroup.getAttends().contains(attends)) {
                return studentGroup;
            }
        }

        return null;
    }

    public Map<Shift, SortedSet<StudentGroup>> getStudentGroupsIndexedByShift() {
        final Map<Shift, SortedSet<StudentGroup>> map =
                new TreeMap<Shift, SortedSet<StudentGroup>>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        for (final StudentGroup studentGroup : getStudentGroupsSet()) {
            if (studentGroup.hasShift()) {
                final Shift shift = studentGroup.getShift();
                final SortedSet<StudentGroup> studentGroups;
                if (map.containsKey(shift)) {
                    studentGroups = map.get(shift);
                } else {
                    studentGroups = new TreeSet<StudentGroup>(StudentGroup.COMPARATOR_BY_GROUP_NUMBER);
                    map.put(shift, studentGroups);
                }
                studentGroups.add(studentGroup);
            }
        }
        return map;
    }

    public SortedSet<StudentGroup> getStudentGroupsOrderedByGroupNumber() {
        final SortedSet<StudentGroup> studentGroups = new TreeSet<StudentGroup>(StudentGroup.COMPARATOR_BY_GROUP_NUMBER);
        studentGroups.addAll(getStudentGroupsSet());
        return studentGroups;
    }

    public boolean isPersonTeacher(Person person) {
        for (ExecutionCourse ec : getExecutionCourses()) {
            for (Professorship professorship : ec.getProfessorships()) {
                if (professorship.getPerson() == person) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<StudentGroup> getStudentGroupsSet() {
        Set<StudentGroup> result = new HashSet<StudentGroup>();
        for (StudentGroup sg : super.getStudentGroupsSet()) {
            if (!sg.wasDeleted()) {
                result.add(sg);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    public List<StudentGroup> getDeletedStudentGroups() {
        List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (StudentGroup sg : super.getStudentGroupsSet()) {
            if (!sg.getValid()) {
                result.add(sg);
            }
        }
        return result;
    }

    @Deprecated
    public java.util.Date getEnrolmentBeginDayDate() {
        org.joda.time.DateTime dt = getEnrolmentBeginDayDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setEnrolmentBeginDayDate(java.util.Date date) {
        if (date == null) {
            setEnrolmentBeginDayDateDateTime(null);
        } else {
            setEnrolmentBeginDayDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getEnrolmentEndDayDate() {
        org.joda.time.DateTime dt = getEnrolmentEndDayDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setEnrolmentEndDayDate(java.util.Date date) {
        if (date == null) {
            setEnrolmentEndDayDateDateTime(null);
        } else {
            setEnrolmentEndDayDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExportGrouping> getExportGroupings() {
        return getExportGroupingsSet();
    }

    @Deprecated
    public boolean hasAnyExportGroupings() {
        return !getExportGroupingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Attends> getAttends() {
        return getAttendsSet();
    }

    @Deprecated
    public boolean hasAnyAttends() {
        return !getAttendsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyStudentGroups() {
        return !getStudentGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Project> getProjects() {
        return getProjectsSet();
    }

    @Deprecated
    public boolean hasAnyProjects() {
        return !getProjectsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftGroupingProperties> getShiftGroupingProperties() {
        return getShiftGroupingPropertiesSet();
    }

    @Deprecated
    public boolean hasAnyShiftGroupingProperties() {
        return !getShiftGroupingPropertiesSet().isEmpty();
    }

    @Deprecated
    public boolean hasProjectDescription() {
        return getProjectDescription() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasEnrolmentPolicy() {
        return getEnrolmentPolicy() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasIdealCapacity() {
        return getIdealCapacity() != null;
    }

    @Deprecated
    public boolean hasShiftType() {
        return getShiftType() != null;
    }

    @Deprecated
    public boolean hasMaximumCapacity() {
        return getMaximumCapacity() != null;
    }

    @Deprecated
    public boolean hasDifferentiatedCapacity() {
        return getDifferentiatedCapacity() != null;
    }

    @Deprecated
    public boolean hasMinimumCapacity() {
        return getMinimumCapacity() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEndDayDateDateTime() {
        return getEnrolmentEndDayDateDateTime() != null;
    }

    @Deprecated
    public boolean hasAutomaticEnrolment() {
        return getAutomaticEnrolment() != null;
    }

    @Deprecated
    public boolean hasEnrolmentBeginDayDateDateTime() {
        return getEnrolmentBeginDayDateDateTime() != null;
    }

    @Deprecated
    public boolean hasGroupMaximumNumber() {
        return getGroupMaximumNumber() != null;
    }

}
