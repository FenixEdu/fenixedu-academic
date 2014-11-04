/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 08/Mar/2005
 *
 */
package org.fenixedu.academic.domain;

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

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EnrolmentGroupPolicyType;
import org.fenixedu.academic.util.ProposalState;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.FenixFramework;

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
        for (final ExportGrouping exportGrouping : this.getExportGroupingsSet()) {
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
        for (final ExportGrouping exportGrouping : this.getExportGroupingsSet()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                for (final Attends attend : exportGrouping.getExecutionCourse().getAttendsSet()) {
                    if (!this.getAttendsSet().contains(attend)) {
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
        return this.getAttendsSet().size();
    }

    public Attends getStudentAttend(final Registration registration) {
        for (final Attends attend : this.getAttendsSet()) {
            if (attend.getRegistration().getStudent() == registration.getStudent()) {
                return attend;
            }
        }
        return null;
    }

    public Attends getStudentAttend(String studentUsername) {
        for (final Attends attend : this.getAttendsSet()) {
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
            Boolean automaticEnrolment, Boolean differentiatedCapacity, ExecutionCourse executionCourse,
            Map<String, Integer> shiftCapacityMap) {

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

        addGroupingToAttends(grouping, executionCourse.getAttendsSet());
        GroupsAndShiftsManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.groupAndShifts.grouping.added", grouping.getName(), executionCourse.getNome(),
                executionCourse.getDegreePresentationString());

        if (differentiatedCapacity) {
            setDiferentiatedCapacityShiftsGroupingProperties(shiftType, shiftCapacityMap, grouping);
        }
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

    public void edit(String goupingName, Date enrolmentBeginDay, Date enrolmentEndDay,
            EnrolmentGroupPolicyType enrolmentGroupPolicyType, Integer groupMaximumNumber, Integer idealCapacity,
            Integer maximumCapacity, Integer minimumCapacity, String projectDescription, ShiftType shiftType,
            Boolean automaticEnrolment, Boolean differentiatedCapacity, Map<String, Integer> shiftCapacityMap) {

        if (goupingName == null || enrolmentBeginDay == null || enrolmentEndDay == null || enrolmentGroupPolicyType == null) {
            throw new NullPointerException();
        }

        checkIfGroupingAlreadyExists(goupingName);

        if (getDifferentiatedCapacity() && !differentiatedCapacity) {
            if (!super.getStudentGroupsSet().isEmpty()) {
                throw new DomainException(this.getClass().getName(), "error.groupProperties.edit.attendsSet.withGroups");
            }

            Collection<ShiftGroupingProperties> shiftGroupingProperties = this.getShiftGroupingPropertiesSet();
            for (ShiftGroupingProperties shiftGP : shiftGroupingProperties) {
                shiftGP.delete();
            }
        } else if (getDifferentiatedCapacity() && differentiatedCapacity && isShiftTypeDifferent(shiftType)) {
            if (!super.getStudentGroupsSet().isEmpty()) {
                throw new DomainException(this.getClass().getName(), "error.groupProperties.edit.attendsSet.withGroups");
            }

            Collection<ShiftGroupingProperties> shiftGroupingProperties = this.getShiftGroupingPropertiesSet();
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

        if (!differentiatedCapacity) {
            Collection<ShiftGroupingProperties> shiftGroupingProperties = this.getShiftGroupingPropertiesSet();
            for (ShiftGroupingProperties shiftGP : shiftGroupingProperties) {
                shiftGP.delete();
            }
        } else {
            setDiferentiatedCapacityShiftsGroupingProperties(shiftType, shiftCapacityMap, this);
        }

        if (shiftType == null) {
            unEnrollStudentGroups(this.getStudentGroupsSet());
        }

        List<ExecutionCourse> ecs = getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING, "log.executionCourse.groupAndShifts.grouping.edited",
                    getName(), ec.getNome(), ec.getDegreePresentationString());
        }

    }

    private static void setDiferentiatedCapacityShiftsGroupingProperties(ShiftType shiftType,
            Map<String, Integer> shiftCapacityMap, Grouping grouping) {
        shiftCapacityMap.forEach((shiftID, capacity) -> {
            Shift shift = ((Shift) FenixFramework.getDomainObject(shiftID));
            if (shift.getTypes().contains(shiftType)) {
                if (capacity != null && grouping.getStudentGroupsIndexedByShift().get(shift) != null
                        && capacity < grouping.getStudentGroupsIndexedByShift().get(shift).size()) {
                    throw new DomainException("error.groupProperties.edit.maxGroupCap.inferiorToExistingNumber");
                }
                if (shift.getShiftGroupingProperties() == null) {
                    shift.setShiftGroupingProperties(new ShiftGroupingProperties(shift, grouping, capacity));
                } else {
                    shift.getShiftGroupingProperties().setCapacity(capacity);
                }
            }
        });
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
            GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING, labelKey, groupNumber.toString(), getName(),
                    Integer.toString(students.size()), sbStudentNumbers.toString(), ec.getNome(),
                    ec.getDegreePresentationString());
        }
    }

    private void checkForStudentsInStudentGroupsAndGrouping(List<Registration> students) {
        for (Registration registration : students) {
            Attends attend = getStudentAttend(registration);
            for (final StudentGroup studentGroup : this.getStudentGroupsSet()) {
                if (studentGroup.getAttendsSet().contains(attend)) {
                    throw new DomainException(this.getClass().getName(), "errors.existing.studentEnrolment");
                } else if (!this.getAttendsSet().contains(attend)) {
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
            GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING, "log.executionCourse.groupAndShifts.grouping.removed",
                    getName(), ec.getNome(), ec.getDegreePresentationString());
        }

        Collection<Attends> attends = this.getAttendsSet();
        List<Attends> attendsAux = new ArrayList<Attends>();
        attendsAux.addAll(attends);
        for (Attends attend : attendsAux) {
            attend.removeGroupings(this);
        }

        Collection<ExportGrouping> exportGroupings = this.getExportGroupingsSet();
        List<ExportGrouping> exportGroupingsAux = new ArrayList<ExportGrouping>();
        exportGroupingsAux.addAll(exportGroupings);
        for (ExportGrouping exportGrouping : exportGroupingsAux) {
            ExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
            executionCourse.removeExportGroupings(exportGrouping);
            exportGrouping.delete();
        }

        for (ShiftGroupingProperties shiftGP : getShiftGroupingPropertiesSet()) {
            shiftGP.delete();
        }

        for (Project project : getProjectsSet()) {
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
            if (studentGroup.getAttendsSet().contains(attends)) {
                return studentGroup;
            }
        }

        return null;
    }

    public Map<Shift, SortedSet<StudentGroup>> getStudentGroupsIndexedByShift() {
        final Map<Shift, SortedSet<StudentGroup>> map =
                new TreeMap<Shift, SortedSet<StudentGroup>>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        for (final StudentGroup studentGroup : getStudentGroupsSet()) {
            if (studentGroup.getShift() != null) {
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
            for (Professorship professorship : ec.getProfessorshipsSet()) {
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

}
