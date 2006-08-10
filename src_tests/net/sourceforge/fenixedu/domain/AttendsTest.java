package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class AttendsTest extends DomainTestBase {

	private Attends attendToDelete;
	private Attends attendNotToDelete1;
	private Attends attendNotToDelete2;
	private Attends attendNotToDelete3;
	private Attends attendNotToDelete4;
	
	private void setUpForDeleteCase() {
		attendToDelete = new Attends();
		attendNotToDelete1 = new Attends();
		attendNotToDelete2 = new Attends();
		attendNotToDelete3 = new Attends();
		attendNotToDelete4 = new Attends();
		
		Registration student = new Registration();
		Enrolment enrolment = new Enrolment();
		ExecutionCourse executionCourse = new ExecutionCourse();		
		
		Mark mark1 = new Mark();
		Mark mark2 = new Mark();
        
        Grouping grouping = new Grouping();
        
        StudentGroup studentGroup1 = new StudentGroup();
        studentGroup1.setGrouping(grouping);
        StudentGroup studentGroup2 = new StudentGroup();
        studentGroup2.setGrouping(grouping);
        StudentGroup studentGroup3 = new StudentGroup();
        studentGroup3.setGrouping(grouping);

		attendToDelete.setAluno(student);
		attendToDelete.setEnrolment(enrolment);
		attendToDelete.setDisciplinaExecucao(executionCourse);
		
		attendNotToDelete1.setAluno(student);
		attendNotToDelete1.setEnrolment(enrolment);
		attendNotToDelete1.setDisciplinaExecucao(executionCourse);
		attendNotToDelete1.addStudentGroups(studentGroup1);
        attendNotToDelete1.addGroupings(grouping);
		attendNotToDelete1.addAssociatedMarks(mark1);
		
		attendNotToDelete2.setAluno(student);
		attendNotToDelete2.setEnrolment(enrolment);
		attendNotToDelete2.setDisciplinaExecucao(executionCourse);
        attendNotToDelete2.addStudentGroups(studentGroup2);
        attendNotToDelete2.addGroupings(grouping);
		
		attendNotToDelete3.setAluno(student);
		attendNotToDelete3.setEnrolment(enrolment);
		attendNotToDelete3.setDisciplinaExecucao(executionCourse);
        attendNotToDelete3.addStudentGroups(studentGroup3);
        attendNotToDelete3.addGroupings(grouping);
		
		attendNotToDelete4.setAluno(student);
		attendNotToDelete4.setEnrolment(enrolment);
		attendNotToDelete4.setDisciplinaExecucao(executionCourse);
		attendNotToDelete4.addAssociatedMarks(mark2);
	}

	public void testDelete() {

		setUpForDeleteCase();
		
		try {
			attendToDelete.delete();
            fail("Should not have been deleted.");
		}
		catch (DomainException e) {
		}		

        try {
            attendToDelete.setEnrolment(null);
            attendToDelete.delete();
        }
        catch (DomainException e) {
            fail("Should have been deleted.");
        }       

		try {
			attendNotToDelete1.delete();
			fail("Should not have been deleted.");
		}
		catch (DomainException e) {}
		
		try {
			attendNotToDelete2.delete();
			fail("Should not have been deleted.");
		}
		catch (DomainException e) {}
		
		try {
			attendNotToDelete3.delete();
			fail("Should not have been deleted.");
		}
		catch (DomainException e) {}
		
		try {
			attendNotToDelete4.delete();
			fail("Should not have been deleted.");
		}
		catch (DomainException e) {}

		assertFalse("Failed to dereference Registration", attendToDelete.hasAluno());
		assertFalse("Failed to dereference Enrolment", attendToDelete.hasEnrolment());
		assertFalse("Failed to dereference ExecutionCourse", attendToDelete.hasDisciplinaExecucao());

		assertAttendsNotDereferenced(attendNotToDelete1);
		assertTrue("Should not dereference StudentGroups", attendNotToDelete1.hasAnyStudentGroups());
		assertTrue("Should not dereference Groupings", attendNotToDelete1.hasAnyGroupings());
		assertTrue("Should not dereference Mark", attendNotToDelete1.hasAnyAssociatedMarks());
		
		assertAttendsNotDereferenced(attendNotToDelete2);
		assertTrue("Should not dereference StudentGroups", attendNotToDelete2.hasAnyStudentGroups());
		
		assertAttendsNotDereferenced(attendNotToDelete3);
		assertTrue("Should not dereference Groupings", attendNotToDelete3.hasAnyGroupings());
		
		assertAttendsNotDereferenced(attendNotToDelete4);
		assertTrue("Should not dereference Mark", attendNotToDelete4.hasAnyAssociatedMarks());
	}

	private void assertAttendsNotDereferenced(Attends attends) {
		assertTrue("Should not dereference Registration", attends.hasAluno());
		assertTrue("Should not dereference Enrolment", attends.hasEnrolment());
		assertTrue("Should not dereference ExecutionCourse", attends.hasDisciplinaExecucao());
	}
}

