package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.IDelegate;

public class DelegateTest extends DomainTestBase {

	IDelegate delegateToDelete = null;

	private void setUpDelete() {
		delegateToDelete = new Delegate();
		delegateToDelete.setIdInternal(1);
		
		IExecutionYear executionYear = new ExecutionYear();
		executionYear.addDelegate(delegateToDelete);
		
		IStudent student  = new Student();
		student.addDelegate(delegateToDelete);
		
		IDegree degree = new Degree();
		degree.addDelegate(delegateToDelete);
	}
		
	public void testDelete () {
		
		setUpDelete();
		
		delegateToDelete.delete();
		
		assertCorrectDeletion(delegateToDelete);
	}
	
	protected static void assertCorrectDeletion(IDelegate delegate) {
		assertFalse("Deleted Delegate should not have Degree", delegate.hasDegree());
		assertFalse("Deleted Delegate should not have Student", delegate.hasStudent());
		assertFalse("Deleted Delegate should not have ExecutionYear", delegate.hasExecutionYear());
	}
}
