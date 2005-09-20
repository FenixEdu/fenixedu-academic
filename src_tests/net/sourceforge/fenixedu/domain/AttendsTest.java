package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AttendsTest extends DomainTestBase {

	private IAttends attendToDelete;
	private IAttends attendNotToDelete1;
	private IAttends attendNotToDelete2;
	private IAttends attendNotToDelete3;
	private IAttends attendNotToDelete4;
	
	private void setUpForDeleteCase() {
		attendToDelete = new Attends();
		attendNotToDelete1 = new Attends();
		attendNotToDelete2 = new Attends();
		attendNotToDelete3 = new Attends();
		attendNotToDelete4 = new Attends();
		
		IStudent student = new Student();
		IEnrolment enrolment = new Enrolment();
		IExecutionCourse executionCourse = new ExecutionCourse();		
		
		IMark mark1 = new Mark();
		IMark mark2 = new Mark();
        
        IGrouping grouping = new Grouping();
        
        IStudentGroup studentGroup1 = new StudentGroup();
        studentGroup1.setGrouping(grouping);
        IStudentGroup studentGroup2 = new StudentGroup();
        studentGroup2.setGrouping(grouping);
        IStudentGroup studentGroup3 = new StudentGroup();
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

		assertFalse("Failed to dereference Student", attendToDelete.hasAluno());
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

	private void assertAttendsNotDereferenced(IAttends attends) {
		assertTrue("Should not dereference Student", attends.hasAluno());
		assertTrue("Should not dereference Enrolment", attends.hasEnrolment());
		assertTrue("Should not dereference ExecutionCourse", attends.hasDisciplinaExecucao());
	}
}

