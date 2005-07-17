package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.IDelegate;

public class DelegateTest extends DomainTestBase {

	IDelegate delegateToDelete = null;
	IExecutionYear executionYear = null;
	IStudent student = null;
	IDegree degree = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		delegateToDelete = new Delegate();
		delegateToDelete.setIdInternal(1);
		
		executionYear = new ExecutionYear();
		executionYear.setIdInternal(1);
		executionYear.addDelegate(delegateToDelete);
		
		student = new Student();
		student.setIdInternal(1);
		student.addDelegate(delegateToDelete);
		
		degree = new Degree();
		degree.setIdInternal(1);
		degree.addDelegate(delegateToDelete);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDelete () {
		delegateToDelete.delete();
		
		assertFalse(delegateToDelete.hasDegree());
		assertFalse(delegateToDelete.hasStudent());
		assertFalse(delegateToDelete.hasExecutionYear());
		
		assertFalse(degree.hasDelegate(delegateToDelete));
		assertFalse(student.hasDelegate(delegateToDelete));
		assertFalse(executionYear.hasDelegate(delegateToDelete));
	}
}
