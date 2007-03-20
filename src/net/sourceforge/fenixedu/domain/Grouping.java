/*
 * Created on 08/Mar/2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
import net.sourceforge.fenixedu.util.ProposalState;

/**
 * @author joaosa & rmalo
 * 
 */
public class Grouping extends Grouping_Base {

    public Grouping() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
        for (final StudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getShift() == null) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public List<StudentGroup> getStudentGroupsWithShift() {
        final List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (final StudentGroup studentGroup : this.getStudentGroups()) {
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
        Integer groupMaximumNumber = this.getGroupMaximumNumber();
        if (shiftStudentGroups != null && groupMaximumNumber != null
                && shiftStudentGroups.size() == groupMaximumNumber)
            throw new DomainException(this.getClass().getName(),
                    "error.shift.with.max.number.of.studentGroups");
    }

    public Integer getNumberOfStudentsInGrouping() {
        return this.getAttends().size();
    }

    public Attends getStudentAttend(final Registration registration) {
        for (final Attends attend : this.getAttends()) {
            if (attend.getRegistration() == registration) {
                return attend;
            }
        }
        return null;
    }

    public Attends getStudentAttend(String studentUsername) {
        for (final Attends attend : this.getAttends()) {
            if (attend.getRegistration().getPerson().hasUsername(studentUsername)) {
                return attend;
            }
        }
        return null;
    }

    public StudentGroup readStudentGroupBy(Integer studentGroupNumber) {
        for (final StudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getGroupNumber().equals(studentGroupNumber)) {
                return studentGroup;
            }
        }
        return null;
    }

    public List<StudentGroup> readAllStudentGroupsBy(Shift shift) {
        final List<StudentGroup> result = new ArrayList<StudentGroup>();
        for (final StudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getShift() == shift) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public static Grouping create(String goupingName, Date enrolmentBeginDay, Date enrolmentEndDay,
            EnrolmentGroupPolicyType enrolmentGroupPolicyType, Integer groupMaximumNumber,
            Integer idealCapacity, Integer maximumCapacity, Integer minimumCapacity,
            String projectDescription, ShiftType shiftType, ExecutionCourse executionCourse) {

        if (goupingName == null || enrolmentBeginDay == null || enrolmentEndDay == null
                || enrolmentGroupPolicyType == null) {
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

        ExportGrouping exportGrouping = new ExportGrouping(grouping, executionCourse);
        exportGrouping.setProposalState(new ProposalState(ProposalState.CRIADOR));

        addGroupingToAttends(grouping, executionCourse.getAttends());

        return grouping;
    }

    private static void addGroupingToAttends(final Grouping grouping, final List<Attends> attends) {
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
            EnrolmentGroupPolicyType enrolmentGroupPolicyType, Integer groupMaximumNumber,
            Integer idealCapacity, Integer maximumCapacity, Integer minimumCapacity,
            String projectDescription, ShiftType shiftType) {

        if (goupingName == null || enrolmentBeginDay == null || enrolmentEndDay == null
                || enrolmentGroupPolicyType == null) {
            throw new NullPointerException();
        }

        checkIfGroupingAlreadyExists(goupingName);

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

        if (shiftType == null) {
            unEnrollStudentGroups(this.getStudentGroups());
        }
    }

    private void checkIfGroupingAlreadyExists(String groupingName) {
        if (!this.getName().equals(groupingName)) {
            for (final ExecutionCourse executionCourse : this.getExecutionCourses()) {
                if (executionCourse.getGroupingByName(groupingName) != null) {
                    throw new DomainException(this.getClass().getName(),
                            "error.exception.existing.groupProperties");
                }
            }
        }
    }

    private void unEnrollStudentGroups(List<StudentGroup> studentGroups) {
        for (final StudentGroup studentGroup : studentGroups) {
            studentGroup.setShift(null);
        }
    }

    public void createStudentGroup(Shift shift, Integer groupNumber, List<Registration> students) {

        if (groupNumber == null || students == null)
            throw new NullPointerException();

        if (readStudentGroupBy(groupNumber) != null) {
            throw new DomainException(this.getClass().getName(), "error.invalidGroupNumber");
        }

        checkForStudentsInStudentGroupsAndGrouping(students);

        StudentGroup newStudentGroup = null;
        if (shift != null)
            newStudentGroup = new StudentGroup(groupNumber, this, shift);
        else
            newStudentGroup = new StudentGroup(groupNumber, this);

        for (Registration registration : students) {
            Attends attend = getStudentAttend(registration);
            newStudentGroup.addAttends(attend);
        }
    }

    private void checkForStudentsInStudentGroupsAndGrouping(List<Registration> students) {
        for (Registration registration : students) {
            Attends attend = getStudentAttend(registration);
            for (final StudentGroup studentGroup : this.getStudentGroups()) {
                if (studentGroup.getAttends().contains(attend))
                    throw new DomainException(this.getClass().getName(),
                            "errors.existing.studentEnrolment");
                else if (!this.getAttends().contains(attend))
                    throw new DomainException(this.getClass().getName(),
                            "errors.notExisting.studentInGrouping");
            }
        }
    }

    public void delete() {

        if (!this.getStudentGroups().isEmpty()) {
            throw new DomainException(this.getClass().getName(), "");
        }

        List<Attends> attends = this.getAttends();
        List<Attends> attendsAux = new ArrayList<Attends>();
        attendsAux.addAll(attends);
        for (Attends attend : attendsAux) {
            attend.removeGroupings(this);
        }

        List<ExportGrouping> exportGroupings = this.getExportGroupings();
        List<ExportGrouping> exportGroupingsAux = new ArrayList<ExportGrouping>();
        exportGroupingsAux.addAll(exportGroupings);
        for (ExportGrouping exportGrouping : exportGroupingsAux) {
            ExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
            executionCourse.removeExportGroupings(exportGrouping);
            exportGrouping.delete();
        }

        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public int findMaxGroupNumber() {
        int max = 0;
        for (final StudentGroup studentGroup : getStudentGroups()) {
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
        for (StudentGroup studentGroup : getStudentGroups()) {
            if (studentGroup.getAttends().contains(attends)) {
                return studentGroup;
            }
        }

        return null;
    }

    public Map<Shift, SortedSet<StudentGroup>> getStudentGroupsIndexedByShift() {
        final Map<Shift, SortedSet<StudentGroup>> map = new TreeMap<Shift, SortedSet<StudentGroup>>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
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
}
