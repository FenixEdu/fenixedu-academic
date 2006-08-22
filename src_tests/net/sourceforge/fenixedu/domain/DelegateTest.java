package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DelegateTest extends DomainTestBase {

	Delegate delegateToDelete = null;

	private void setUpDelete() {
		delegateToDelete = new Delegate();
		delegateToDelete.setIdInternal(1);
		
		ExecutionYear executionYear = new ExecutionYear();
		executionYear.addDelegate(delegateToDelete);
		
		Registration registration  = new Registration();
		registration.addDelegate(delegateToDelete);
		
		Degree degree = new Degree();
		degree.addDelegate(delegateToDelete);
	}
		
	public void testDelete () {
		
		setUpDelete();
		
		delegateToDelete.delete();
		
		assertCorrectDeletion(delegateToDelete);
	}
	
	protected static void assertCorrectDeletion(Delegate delegate) {
		assertFalse("Deleted Delegate should not have Degree", delegate.hasDegree());
		assertFalse("Deleted Delegate should not have Registration", delegate.hasStudent());
		assertFalse("Deleted Delegate should not have ExecutionYear", delegate.hasExecutionYear());
	}
}
