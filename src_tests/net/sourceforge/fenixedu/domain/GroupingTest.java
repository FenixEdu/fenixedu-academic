/*
 * Created on Sep 6, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
import net.sourceforge.fenixedu.util.ProposalState;

public class GroupingTest extends DomainTestBase {

    private Grouping groupingToEdit;

    private Shift shift, shift2;

    Grouping grouping, grouping2;

    ExecutionCourse executionCourse, executionCourse1, executionCourse2;

    ExportGrouping exportGrouping, exportGrouping2;

    StudentGroup studentGroup;

    Registration student, student2, student3, student4, student5, student6, student7;

    Attends attend, attend2, attend3, attend4, attend5, attend6, attend7;

    protected void setUp() throws Exception {
        super.setUp();

        setupCreateStudentAndDeleteGrouping();
        setupTestEditGrouping();
        setupTestCreate();
    }

    public void testCreateStudentGroup() {
        try {
            grouping.createStudentGroup(null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Size UnExpected", 1, grouping.getStudentGroupsCount());
            assertEquals("StudentGroup UnExpected", studentGroup, grouping.getStudentGroups().get(0));
        }

        List<Registration> students = new ArrayList();
        students.add(student3);
        students.add(student4);

        try {
            grouping.createStudentGroup(null, 1, students);
            fail("Expected DomainExpected");
        } catch (DomainException e) {
            assertEquals("Size UnExpected", 1, grouping.getStudentGroupsCount());
            assertEquals("StudentGroup UnExpected", studentGroup, grouping.getStudentGroups().get(0));
        }

        students.clear();
        students.add(student2);
        students.add(student3);

        try {
            grouping.createStudentGroup(null, 2, students);
            fail("Expected DomainExpected");
        } catch (DomainException e) {
            assertEquals("Size UnExpected", 1, grouping.getStudentGroupsCount());
            assertEquals("StudentGroup UnExpected", studentGroup, grouping.getStudentGroups().get(0));
        }

        students.clear();
        students.add(student3);
        students.add(student5);

        try {
            grouping.createStudentGroup(null, 2, students);
            fail("Expected DomainExpected");
        } catch (DomainException e) {
            assertEquals("Size UnExpected", 1, grouping.getStudentGroupsCount());
            assertEquals("StudentGroup UnExpected", studentGroup, grouping.getStudentGroups().get(0));
        }

        students.clear();
        students.add(student3);

        try {
            grouping.createStudentGroup(null, 2, students);
            assertEquals("Size UnExpected", 2, grouping.getStudentGroupsCount());
        } catch (DomainException e) {
            fail("UnExpected DomainExpected");
        }

        students.clear();
        students.add(student3);
        students.add(student4);
        students.add(student6);
        students.add(student7);

        try {
            grouping.createStudentGroup(null, 3, students);
            fail("Expected DomainExpected");
        } catch (DomainException e) {
            assertEquals("Size UnExpected", 2, grouping.getStudentGroupsCount());
        }

        students.clear();
        students.add(student3);
        students.add(student4);

        try {
            grouping.createStudentGroup(null, 3, students);
            fail("Expected DomainExpected");
        } catch (DomainException e) {
            assertEquals("Size UnExpected", 2, grouping.getStudentGroupsCount());
        }

        assertEquals("Size UnExpected", 2, grouping.getStudentGroupsCount());
        assertEquals("Size UnExpected", 1, grouping.getStudentGroupsWithoutShift().size());
        assertEquals("Size UnExpected", 1, grouping.getStudentGroupsWithShift().size());

        int firstGroupNumber = grouping.getStudentGroups().get(0).getGroupNumber().intValue();
        int secondGroupNumber = grouping.getStudentGroups().get(1).getGroupNumber().intValue();
        assertTrue("GroupNumber UnExpected", firstGroupNumber == 1 || firstGroupNumber == 2);
        assertTrue("GroupNumber UnExpected", secondGroupNumber == 1 || secondGroupNumber == 2);
        assertTrue("GroupNumber UnExpected", firstGroupNumber != secondGroupNumber);

        assertEquals("Attend UnExpected", attend3, grouping.getStudentGroups().get(0).getAttends()
                .get(0));

        assertEquals("Attend UnExpected", attend, grouping.getStudentGroups().get(1).getAttends().get(0));
        assertEquals("Attend UnExpected", attend2, grouping.getStudentGroups().get(1).getAttends()
                .get(1));

        students.clear();
        students.add(student6);
        students.add(student7);

        try {
            grouping.createStudentGroup(null, 3, students);
            assertEquals("Size UnExpected", 3, grouping.getStudentGroupsCount());
            assertEquals("Size UnExpected", 2, grouping.getStudentGroupsWithoutShift().size());
            assertEquals("Size UnExpected", 1, grouping.getStudentGroupsWithShift().size());
        } catch (DomainException e) {
            fail("UnExpected DomainExpected");
        }

        students.clear();
        students.add(student6);
        students.add(student7);

        try {
            grouping.createStudentGroup(shift2, 3, students);
            fail("Expected DomainExpected");
        } catch (DomainException e) {
            assertEquals("Size UnExpected", 3, grouping.getStudentGroupsCount());
            assertEquals("Size UnExpected", 2, grouping.getStudentGroupsWithoutShift().size());
            assertEquals("Size UnExpected", 1, grouping.getStudentGroupsWithShift().size());
        }

        studentGroup.setShift(null);
        studentGroup.setGrouping(null);

        try {
            grouping.createStudentGroup(shift2, 3, students);
            fail("UnExpected DomainExpected");
        } catch (DomainException e) {
        }

        assertEquals("Size UnExpected", 2, grouping.getStudentGroupsCount());
        assertEquals("Size UnExpected", 2, grouping.getStudentGroupsWithoutShift().size());
        assertEquals("Size UnExpected", 0, grouping.getStudentGroupsWithShift().size());
    }

    public void testCreate() {
        try {
            Grouping.create(null, null, null, null, 1, 2, 3, 1, "projectDescription", null,
                    executionCourse);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different ExportingGroups Count!", executionCourse.getExportGroupingsCount(),
                    0);
            for (final Attends attend : executionCourse.getAttends()) {
                assertEquals("Invalid Attend Groupings Count!", attend.getGroupingsCount(), 0);
            }
        }

        try {
            Grouping.create(null, createDate(2005, 2, 1), null, null, 1, 2, 3, 1, "projectDescription",
                    ShiftType.PRATICA, executionCourse);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different ExportingGroups Count!", executionCourse.getExportGroupingsCount(),
                    0);
            for (final Attends attend : executionCourse.getAttends()) {
                assertEquals("Invalid Attend Groupings Count!", attend.getGroupingsCount(), 0);
            }
        }

        Date enrolmentBeginDayDate = createDate(2005, 10, 1);
        Date enrolmentEndDayDate = createDate(2005, 11, 1);
        Grouping newGrouping = Grouping.create("goupingName", enrolmentBeginDayDate,
                enrolmentEndDayDate, new EnrolmentGroupPolicyType(EnrolmentGroupPolicyType.ATOMIC), 2,
                2, 3, 1, "projectDescription", ShiftType.TEORICA, executionCourse);
        checkIfGroupingAttributesAreCorrect(newGrouping, "goupingName", new EnrolmentGroupPolicyType(
                EnrolmentGroupPolicyType.ATOMIC), 2, 2, 3, 1, "projectDescription", ShiftType.TEORICA);

        assertEquals("Different Enrolment Begin Date!", newGrouping.getEnrolmentBeginDayDate(),
                enrolmentBeginDayDate);
        assertEquals("Different Enrolment End Date!", newGrouping.getEnrolmentEndDayDate(),
                enrolmentEndDayDate);

        assertEquals("Different ExportingGroupings Count in Grouping!", newGrouping
                .getExportGroupingsCount(), 1);
        ExportGrouping exportGrouping = newGrouping.getExportGroupings().get(0);
        assertEquals("", exportGrouping.getProposalState().getState(), Integer
                .valueOf(ProposalState.CRIADOR));

        for (final Attends attend : executionCourse.getAttends()) {
            assertEquals("Different Groupings Count in Attends!", attend.getGroupingsCount(), 1);
            assertEquals("Invalid Grouping int Attends!", attend.getGroupings().get(0), newGrouping);
        }
    }

    public void testEdit() {

        try {
            groupingToEdit.edit(null, null, null, null, null, null, null, null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            checkIfGroupingAttributesAreCorrect(groupingToEdit, "name", new EnrolmentGroupPolicyType(
                    EnrolmentGroupPolicyType.ATOMIC), 5, 4, 8, 2, "projectDescription",
                    ShiftType.TEORICA);
        }
        try {
            groupingToEdit.edit("newName", null, null, null, null, null, null, null, "",
                    ShiftType.PRATICA);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            checkIfGroupingAttributesAreCorrect(groupingToEdit, "name", new EnrolmentGroupPolicyType(
                    EnrolmentGroupPolicyType.ATOMIC), 5, 4, 8, 2, "projectDescription",
                    ShiftType.TEORICA);
        }
        try {
            groupingToEdit.edit("otherInvalidName", createDate(2005, 10, 1), createDate(2005, 11, 1),
                    new EnrolmentGroupPolicyType(EnrolmentGroupPolicyType.INDIVIDUAL), null, null, null,
                    null, "", ShiftType.PRATICA);
            fail("Expected DomainException");
        } catch (DomainException e) {
            checkIfGroupingAttributesAreCorrect(groupingToEdit, "name", new EnrolmentGroupPolicyType(
                    EnrolmentGroupPolicyType.ATOMIC), 5, 4, 8, 2, "projectDescription",
                    ShiftType.TEORICA);
        }

        // Edit Grouping properties
        Date enrolmentBeginDayDate = createDate(2005, 10, 1);
        Date enrolmentEndDayDate = createDate(2005, 11, 1);
        groupingToEdit.edit("otherValidName", enrolmentBeginDayDate, enrolmentEndDayDate,
                new EnrolmentGroupPolicyType(EnrolmentGroupPolicyType.INDIVIDUAL), 4, 2, 6, 4,
                "otherProjectDescription", ShiftType.PRATICA);
        checkIfGroupingAttributesAreCorrect(groupingToEdit, "otherValidName",
                new EnrolmentGroupPolicyType(EnrolmentGroupPolicyType.INDIVIDUAL), 4, 2, 6, 4,
                "otherProjectDescription", ShiftType.PRATICA);
        assertEquals("Different Enrolment Begin Date!", groupingToEdit.getEnrolmentBeginDayDate(),
                enrolmentBeginDayDate);
        assertEquals("Different Enrolment End Date!", groupingToEdit.getEnrolmentEndDayDate(),
                enrolmentEndDayDate);

        // Edit Grouping properties using a null ShiftType
        // (this should unenroll student groups from shift)
        enrolmentBeginDayDate = createDate(2005, 12, 1);
        enrolmentEndDayDate = createDate(2005, 12, 20);
        groupingToEdit.edit("otherValidName", enrolmentBeginDayDate, enrolmentEndDayDate,
                new EnrolmentGroupPolicyType(EnrolmentGroupPolicyType.INDIVIDUAL), 5, 3, 7, 5,
                "otherProjectDescription", null);
        checkIfGroupingAttributesAreCorrect(groupingToEdit, "otherValidName",
                new EnrolmentGroupPolicyType(EnrolmentGroupPolicyType.INDIVIDUAL), 5, 3, 7, 5,
                "otherProjectDescription", null);
        assertEquals("Different Enrolment Begin Date!", groupingToEdit.getEnrolmentBeginDayDate(),
                enrolmentBeginDayDate);
        assertEquals("Different Enrolment End Date!", groupingToEdit.getEnrolmentEndDayDate(),
                enrolmentEndDayDate);
        assertEquals("Different StudentGroups Count!", groupingToEdit.getStudentGroupsCount(), 2);
        assertEquals("Different Shift StudentGroups Count!", shift.getAssociatedStudentGroupsCount(), 0);

        for (final StudentGroup studentGroup : groupingToEdit.getStudentGroups()) {
            assertNull("StudentGroup Shift should be null!", studentGroup.getShift());
        }
    }

    public void testDelete() {
        try {
            grouping.delete();
            fail("DomainException Expected");
        } catch (DomainException e) {
            assertEquals("StudentGroups UnExpected", 1, grouping.getStudentGroupsCount());
            assertEquals("Grouping UnExpected", grouping, exportGrouping.getGrouping());
            assertEquals("ExecutionCourse UnExpected", executionCourse1, exportGrouping
                    .getExecutionCourse());
        }

        try {
            grouping2.delete();
        } catch (DomainException e) {
            fail("DomainException UnExpected");
        }

        assertEquals("Attends UnExpected", 0, grouping2.getAttendsCount());

        assertNull("Grouping UnExpected", exportGrouping2.getGrouping());
        assertNull("ExecutionCourse UnExpected", exportGrouping2.getExecutionCourse());
    }

    private void checkIfGroupingAttributesAreCorrect(final Grouping grouping, final String goupingName,
            final EnrolmentGroupPolicyType enrolmentGroupPolicyType, final Integer groupMaximumNumber,
            final Integer idealCapacity, final Integer maximumCapacity, final Integer minimumCapacity,
            final String projectDescription, final ShiftType shiftType) {

        assertEquals("Different Grouping Name!", grouping.getName(), goupingName);
        assertEquals("Different Grouping EnrolmentPolicyType!", grouping.getEnrolmentPolicy(),
                enrolmentGroupPolicyType);
        assertEquals("Different Grouping GroupMaximumNumber!", grouping.getGroupMaximumNumber(),
                groupMaximumNumber);
        assertEquals("Different Grouping IdealCapacity!", grouping.getIdealCapacity(), idealCapacity);
        assertEquals("Different Grouping MaximumCapacity!", grouping.getMaximumCapacity(),
                maximumCapacity);
        assertEquals("Different Grouping MinimumCapacity!", grouping.getMinimumCapacity(),
                minimumCapacity);
        assertEquals("Different Grouping Project Description!", grouping.getProjectDescription(),
                projectDescription);
        assertEquals("Different Grouping ShiftType!", grouping.getShiftType(), shiftType);
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private void setupCreateStudentAndDeleteGrouping() {

//        shift2 = new Shift();

        grouping = new Grouping();
        grouping.setIdInternal(0);
        grouping.setMaximumCapacity(3);
        grouping.setMinimumCapacity(2);
        grouping.setGroupMaximumNumber(1);

        grouping2 = new Grouping();
        grouping2.setIdInternal(1);

        executionCourse1 = new ExecutionCourse();
        executionCourse1.setIdInternal(0);

        executionCourse2 = new ExecutionCourse();
        executionCourse2.setIdInternal(1);

        exportGrouping = new ExportGrouping();
        exportGrouping.setIdInternal(0);
        exportGrouping.setGrouping(grouping);
        exportGrouping.setExecutionCourse(executionCourse1);

        exportGrouping2 = new ExportGrouping();
        exportGrouping2.setIdInternal(1);
        exportGrouping2.setGrouping(grouping2);
        exportGrouping2.setExecutionCourse(executionCourse2);

        student = new Registration();
        student.setIdInternal(0);

        student2 = new Registration();
        student2.setIdInternal(1);

        student3 = new Registration();
        student3.setIdInternal(2);

        student4 = new Registration();
        student4.setIdInternal(3);

        student5 = new Registration();
        student5.setIdInternal(4);

        student6 = new Registration();
        student6.setIdInternal(5);

        student7 = new Registration();
        student7.setIdInternal(6);

        attend = new Attends();
        attend.setIdInternal(0);
        attend.setAluno(student);
        attend.addGroupings(grouping);

        attend2 = new Attends();
        attend2.setIdInternal(1);
        attend2.setAluno(student2);
        attend2.addGroupings(grouping);

        attend3 = new Attends();
        attend3.setIdInternal(2);
        attend3.setAluno(student3);
        attend3.addGroupings(grouping);

        attend4 = new Attends();
        attend4.setIdInternal(3);
        attend4.setAluno(student4);
        attend4.addGroupings(grouping);

        attend5 = new Attends();
        attend5.setIdInternal(4);
        attend5.setAluno(student5);
        attend5.addGroupings(grouping2);

        attend6 = new Attends();
        attend6.setIdInternal(4);
        attend6.setAluno(student6);
        attend6.addGroupings(grouping);

        attend7 = new Attends();
        attend7.setIdInternal(4);
        attend7.setAluno(student7);
        attend7.addGroupings(grouping);

        studentGroup = new StudentGroup();
        studentGroup.setIdInternal(0);
        studentGroup.setGrouping(grouping);
        studentGroup.setGroupNumber(1);
        studentGroup.setShift(shift2);
        studentGroup.addAttends(attend);
        studentGroup.addAttends(attend2);
    }

    private void setupTestEditGrouping() {
        groupingToEdit = new Grouping();
        groupingToEdit.setIdInternal(1);
        groupingToEdit.setName("name");
        groupingToEdit.setEnrolmentBeginDayDate(createDate(2005, 1, 1));
        groupingToEdit.setEnrolmentEndDayDate(createDate(2005, 2, 1));
        groupingToEdit.setEnrolmentPolicy(new EnrolmentGroupPolicyType(EnrolmentGroupPolicyType.ATOMIC));
        groupingToEdit.setGroupMaximumNumber(5);
        groupingToEdit.setIdealCapacity(4);
        groupingToEdit.setMaximumCapacity(8);
        groupingToEdit.setMinimumCapacity(2);
        groupingToEdit.setProjectDescription("projectDescription");
        groupingToEdit.setShiftType(ShiftType.TEORICA);

        ExecutionCourse executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(1);

        ExportGrouping exportGrouping = new ExportGrouping();
        exportGrouping.setIdInternal(1);
        exportGrouping.setExecutionCourse(executionCourse);
        exportGrouping.setGrouping(groupingToEdit);
        exportGrouping.setProposalState(new ProposalState(ProposalState.CRIADOR));

        Grouping otherGrouping = new Grouping();
        otherGrouping.setIdInternal(2);
        otherGrouping.setName("otherInvalidName");

        ExportGrouping otherExportGrouping = new ExportGrouping();
        otherExportGrouping.setIdInternal(2);
        otherExportGrouping.setExecutionCourse(executionCourse);
        otherExportGrouping.setGrouping(otherGrouping);
        otherExportGrouping.setProposalState(new ProposalState(ProposalState.ACEITE));

//        shift = new Shift();
        shift.setIdInternal(1);

        StudentGroup studentGroup1 = new StudentGroup();
        studentGroup1.setIdInternal(1);
        studentGroup1.setShift(shift);

        StudentGroup studentGroup2 = new StudentGroup();
        studentGroup2.setIdInternal(2);
        studentGroup2.setShift(shift);

        groupingToEdit.addStudentGroups(studentGroup1);
        groupingToEdit.addStudentGroups(studentGroup2);
    }

    private void setupTestCreate() {
        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(1);

        Attends attend1 = new Attends();
        attend1.setIdInternal(1);
        Attends attend2 = new Attends();
        attend2.setIdInternal(2);

        executionCourse.addAttends(attend1);
        executionCourse.addAttends(attend2);
    }
}
