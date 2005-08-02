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
		
		IStudentGroupAttend sga1 = new StudentGroupAttend();
		IStudentGroupAttend sga2 = new StudentGroupAttend();
		
		IAttendInAttendsSet aias1 = new AttendInAttendsSet();
		IAttendInAttendsSet aias2 = new AttendInAttendsSet();
		
		IMark mark1 = new Mark();
		IMark mark2 = new Mark();

		attendToDelete.setAluno(student);
		attendToDelete.setEnrolment(enrolment);
		attendToDelete.setDisciplinaExecucao(executionCourse);
		
		attendNotToDelete1.setAluno(student);
		attendNotToDelete1.setEnrolment(enrolment);
		attendNotToDelete1.setDisciplinaExecucao(executionCourse);
		attendNotToDelete1.addStudentGroupAttends(sga1);
		attendNotToDelete1.addAttendInAttendsSet(aias1);
		attendNotToDelete1.setMark(mark1);
		
		attendNotToDelete2.setAluno(student);
		attendNotToDelete2.setEnrolment(enrolment);
		attendNotToDelete2.setDisciplinaExecucao(executionCourse);
		attendNotToDelete2.addStudentGroupAttends(sga2);
		
		attendNotToDelete3.setAluno(student);
		attendNotToDelete3.setEnrolment(enrolment);
		attendNotToDelete3.setDisciplinaExecucao(executionCourse);
		attendNotToDelete3.addAttendInAttendsSet(aias2);
		
		attendNotToDelete4.setAluno(student);
		attendNotToDelete4.setEnrolment(enrolment);
		attendNotToDelete4.setDisciplinaExecucao(executionCourse);
		attendNotToDelete4.setMark(mark2);
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
		assertTrue("Should not dereference StudentGroupAttends", attendNotToDelete1.hasAnyStudentGroupAttends());
		assertTrue("Should not dereference AttendInAttendsSets", attendNotToDelete1.hasAnyAttendInAttendsSet());
		assertTrue("Should not dereference Mark", attendNotToDelete1.hasMark());
		
		assertAttendsNotDereferenced(attendNotToDelete2);
		assertTrue("Should not dereference StudentGroupAttends", attendNotToDelete2.hasAnyStudentGroupAttends());
		
		assertAttendsNotDereferenced(attendNotToDelete3);
		assertTrue("Should not dereference AttendInAttendsSets", attendNotToDelete3.hasAnyAttendInAttendsSet());
		
		assertAttendsNotDereferenced(attendNotToDelete4);
		assertTrue("Should not dereference Mark", attendNotToDelete4.hasMark());
	}

	private void assertAttendsNotDereferenced(IAttends attends) {
		assertTrue("Should not dereference Student", attends.hasAluno());
		assertTrue("Should not dereference Enrolment", attends.hasEnrolment());
		assertTrue("Should not dereference ExecutionCourse", attends.hasDisciplinaExecucao());
	}
}

