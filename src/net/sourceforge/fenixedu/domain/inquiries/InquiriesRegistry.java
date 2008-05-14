/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.NullArgumentException;


/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesRegistry extends InquiriesRegistry_Base {
	
	public InquiriesRegistry() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	public InquiriesRegistry(ExecutionCourse executionCourse, ExecutionSemester executionSemester, Registration registration) {
		this();
		if((executionCourse == null) || (executionSemester == null) || (registration == null)) {
			throw new NullArgumentException("The executionCourse, executionPeriod and student should not be null!");
		}
		this.setExecutionCourse(executionCourse);
		this.setExecutionPeriod(executionSemester);
		this.setStudent(registration);
		
	}
    
}
