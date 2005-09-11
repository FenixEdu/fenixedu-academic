/*
 * Created on 08/Mar/2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
import net.sourceforge.fenixedu.util.ProposalState;

/**
 * @author joaosa & rmalo
 * 
 */
public class Grouping extends Grouping_Base {

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

    public String toString() {
        String result = "[GROUP";
        result += ", maximumCapacity=" + getMaximumCapacity();
        result += ", minimumCapacity=" + getMinimumCapacity();
        result += ", idealCapacity=" + getIdealCapacity();
        result += ", enrolmentPolicy=" + getEnrolmentPolicy();
        result += ", groupMaximumNumber=" + getGroupMaximumNumber();
        result += ", name=" + getName();
        result += ", shiftType=" + getShiftType();
        result += ", projectDescription=" + getProjectDescription();
        result += "]";
        return result;
    }

    public List<IExecutionCourse> getExecutionCourses() {
        final List result = new ArrayList();
        for (final IExportGrouping exportGrouping : this.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                result.add(exportGrouping.getExecutionCourse());
            }
        }
        return result;
    }

    public List getStudentGroupsWithoutShift() {
        final List<IStudentGroup> result = new ArrayList();
        for (final IStudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getShift() == null) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public List getStudentGroupsWithShift() {
        final List result = new ArrayList();
        for (final IStudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getShift() != null) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public List getStudentGroupsWithShift(IShift shift) {
        final List result = new ArrayList();
        for (final IStudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getShift() == shift) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public Integer getNumberOfStudentsNotInGrouping() {
        int numberOfStudents = 0;
        for (final IExportGrouping exportGrouping : this.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                for (final IAttends attend : exportGrouping.getExecutionCourse().getAttends()) {
                    if (!this.getAttends().contains(attend)) {
                        numberOfStudents++;
                    }
                }
            }
        }
        return Integer.valueOf(numberOfStudents);
    }

    public void checkShiftCapacity(IShift shift) {
        List shiftStudentGroups = this.getStudentGroupsWithShift(shift);
        Integer groupMaximumNumber = this.getGroupMaximumNumber();
        if (shiftStudentGroups != null && groupMaximumNumber != null
                && shiftStudentGroups.size() == groupMaximumNumber)
            throw new DomainException(this.getClass().getName(), 
                    "error.shift.with.max.number.of.studentGroups");
    }
    
    public Integer getNumberOfStudentsInGrouping() {
        return this.getAttends().size();
    }

    public IAttends getStudentAttend(IStudent student) {
        for (final IAttends attend : this.getAttends()) {
            if (attend.getAluno() == student) {
                return attend;
            }
        }
        return null;
    }

    public IAttends getStudentAttend(String studentUsername) {
        for (final IAttends attend : this.getAttends()) {
            if (attend.getAluno().getPerson().getUsername().equals(studentUsername)) {
                return attend;
            }
        }
        return null;
    }

    public IStudentGroup readStudentGroupBy(Integer studentGroupNumber) {
        for (final IStudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getGroupNumber().equals(studentGroupNumber)) {
                return studentGroup;
            }
        }
        return null;
    }

    public List<IStudentGroup> readAllStudentGroupsBy(IShift shift) {
        List<IStudentGroup> result = new ArrayList();
        for (final IStudentGroup studentGroup : this.getStudentGroups()) {
            if (studentGroup.getShift() == shift) {
                result.add(studentGroup);
            }
        }
        return result;
    }

    public static IGrouping create(String goupingName, Date enrolmentBeginDay, Date enrolmentEndDay,
            EnrolmentGroupPolicyType enrolmentGroupPolicyType, Integer groupMaximumNumber,
            Integer idealCapacity, Integer maximumCapacity, Integer minimumCapacity,
            String projectDescription, ShiftType shiftType, IExecutionCourse executionCourse) {

        if (goupingName == null || enrolmentBeginDay == null || enrolmentEndDay == null
                || enrolmentGroupPolicyType == null) {
            throw new NullPointerException();
        }

        checkIfGroupingAlreadyExistInExecutionCourse(goupingName, executionCourse);

        IGrouping grouping = new Grouping();
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

        IExportGrouping exportGrouping = new ExportGrouping(grouping, executionCourse);
        exportGrouping.setProposalState(new ProposalState(ProposalState.CRIADOR));

        addGroupingToAttends(grouping, executionCourse.getAttends());

        return grouping;
    }

    private static void addGroupingToAttends(final IGrouping grouping, final List<IAttends> attends) {
        for (final IAttends attend : attends) {
            attend.addGroupings(grouping);
        }
    }

    private static void checkIfGroupingAlreadyExistInExecutionCourse(final String goupingName,
            final IExecutionCourse executionCourse) {
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
            for (final IExecutionCourse executionCourse : this.getExecutionCourses()) {
                if (executionCourse.getGroupingByName(groupingName) != null) {
                    throw new DomainException("error.exception.existing.groupProperties");
                }
            }
        }
    }

    private void unEnrollStudentGroups(List<IStudentGroup> studentGroups) {
        for (final IStudentGroup studentGroup : studentGroups) {
            studentGroup.setShift(null);
        }
    }

    public void createStudentGroup(IShift shift, Integer groupNumber, List<IStudent> students) {

        if (groupNumber == null || students == null)
            throw new NullPointerException();

        if (readStudentGroupBy(groupNumber) != null) {
            throw new DomainException(this.getClass().getName(), "error.invalidGroupNumber");
        }

        Integer minCapacity = this.getMinimumCapacity();
        Integer maxCapacity = this.getMaximumCapacity();
        Integer groupMaximumNumber = this.getGroupMaximumNumber();

        checkCapacities(shift, students, minCapacity, maxCapacity, groupMaximumNumber);

        checkForStudentsInStudentGroupsAndGrouping(students);

        IStudentGroup newStudentGroup = null;
        if (shift != null)
            newStudentGroup = new StudentGroup(groupNumber, this, shift);
        else
            newStudentGroup = new StudentGroup(groupNumber, this);

        for (IStudent student : students) {
            IAttends attend = getStudentAttend(student);
            newStudentGroup.addAttends(attend);
        }
    }

    private void checkCapacities(IShift shift, List<IStudent> students, Integer minCapacity, Integer maxCapacity, Integer groupMaximumNumber) {
        if (minCapacity != null && maxCapacity != null) {
            if (students.size() < minCapacity || students.size() > maxCapacity)
                throw new DomainException(this.getClass().getName(), "error.invalidNumberOfStudents");
        }

        if (groupMaximumNumber != null) {
            if (shift == null) {
                List studentGroupsWithoutShift = this.getStudentGroupsWithoutShift();
                if (studentGroupsWithoutShift != null
                        && studentGroupsWithoutShift.size() == groupMaximumNumber)
                    throw new DomainException(this.getClass().getName(),
                            "error.invalidNumberOfStudentGroupsWithoutGroup");
            } else {
                List studentGroupsWithShift = this.getStudentGroupsWithShift(shift);
                if (studentGroupsWithShift != null
                        && studentGroupsWithShift.size() == groupMaximumNumber)
                    throw new DomainException(this.getClass().getName(),
                            "error.invalidNumberOfStudentGroups");
            }
        }
    }

    private void checkForStudentsInStudentGroupsAndGrouping(List<IStudent> students) {
        for (IStudent student : students) {
            IAttends attend = getStudentAttend(student);
            for (final IStudentGroup studentGroup : this.getStudentGroups()) {
                if (studentGroup.getAttends().contains(attend) || !this.getAttends().contains(attend)) {
                    throw new DomainException(this.getClass().getName(), "");
                }
            }
        }
    }

    public void delete() {

        if (!this.getStudentGroups().isEmpty()) {
            throw new DomainException(this.getClass().getName(), "");
        }

        List<IAttends> attends = this.getAttends();
        List<IAttends> attendsAux = new ArrayList();
        attendsAux.addAll(attends);
        for (IAttends attend : attendsAux) {
            attend.removeGroupings(this);
        }

        List exportGroupings = this.getExportGroupings();
        List<IExportGrouping> exportGroupingsAux = new ArrayList();
        exportGroupingsAux.addAll(exportGroupings);
        for (IExportGrouping exportGrouping : exportGroupingsAux) {
            IExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
            executionCourse.removeExportGroupings(exportGrouping);
            exportGrouping.delete();
        }
    }
}
