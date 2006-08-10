/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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
	
	public InquiriesRegistry(ExecutionCourse executionCourse, ExecutionPeriod executionPeriod, Registration student) {
		this();
		if((executionCourse == null) || (executionPeriod == null) || (student == null)) {
			throw new NullArgumentException("The executionCourse, executionPeriod and student should not be null!");
		}
		this.setExecutionCourse(executionCourse);
		this.setExecutionPeriod(executionPeriod);
		this.setStudent(student);
		
	}
    
}
