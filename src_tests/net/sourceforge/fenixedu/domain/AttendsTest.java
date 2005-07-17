package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AttendsTest extends DomainTestBase {

	private IAttends attendToDelete;
	private IAttends attendNotToDelete1;
	private IAttends attendNotToDelete2;
	private IAttends attendNotToDelete3;
	private IAttends attendNotToDelete4;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		
		attendToDelete = new Attends();
		attendToDelete.setIdInternal(1);
		attendNotToDelete1 = new Attends();
		attendNotToDelete1.setIdInternal(2);
		attendNotToDelete2 = new Attends();
		attendNotToDelete2.setIdInternal(3);
		attendNotToDelete3 = new Attends();
		attendNotToDelete3.setIdInternal(4);
		attendNotToDelete4 = new Attends();
		attendNotToDelete4.setIdInternal(5);
		
		
		IStudent student = new Student();
		student.setIdInternal(1);
		IEnrolment enrolment = new Enrolment();
		enrolment.setIdInternal(1);
		IExecutionCourse executionCourse = new ExecutionCourse();
		executionCourse.setIdInternal(1);
		
		
		
		IStudentGroupAttend sga1 = new StudentGroupAttend();
		sga1.setIdInternal(1);
		IStudentGroupAttend sga2 = new StudentGroupAttend();
		sga2.setIdInternal(2);
		
		IAttendInAttendsSet aias1 = new AttendInAttendsSet();
		aias1.setIdInternal(1);
		IAttendInAttendsSet aias2 = new AttendInAttendsSet();
		aias1.setIdInternal(2);
		
		IMark mark1 = new Mark();
		mark1.setIdInternal(1);
		IMark mark2 = new Mark();
		mark2.setIdInternal(2);
		
		
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

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDelete() {

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

		
		
		assertFalse(attendToDelete.hasAluno());
		assertFalse(attendToDelete.hasEnrolment());
		assertFalse(attendToDelete.hasDisciplinaExecucao());

		assertTrue(attendNotToDelete1.hasAluno());
		assertTrue(attendNotToDelete1.hasEnrolment());
		assertTrue(attendNotToDelete1.hasDisciplinaExecucao());
		assertTrue(attendNotToDelete1.hasAnyStudentGroupAttends());
		assertTrue(attendNotToDelete1.hasAnyAttendInAttendsSet());
		assertTrue(attendNotToDelete1.hasMark());
		
		assertTrue(attendNotToDelete2.hasAluno());
		assertTrue(attendNotToDelete2.hasEnrolment());
		assertTrue(attendNotToDelete2.hasDisciplinaExecucao());
		assertTrue(attendNotToDelete2.hasAnyStudentGroupAttends());
		
		assertTrue(attendNotToDelete3.hasAluno());
		assertTrue(attendNotToDelete3.hasEnrolment());
		assertTrue(attendNotToDelete3.hasDisciplinaExecucao());
		assertTrue(attendNotToDelete3.hasAnyAttendInAttendsSet());
		
		assertTrue(attendNotToDelete4.hasAluno());
		assertTrue(attendNotToDelete4.hasEnrolment());
		assertTrue(attendNotToDelete4.hasDisciplinaExecucao());
		assertTrue(attendNotToDelete4.hasMark());
		
	}

}

