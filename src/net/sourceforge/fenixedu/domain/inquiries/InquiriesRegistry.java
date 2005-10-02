/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import org.apache.commons.lang.NullArgumentException;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;


/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesRegistry extends InquiriesRegistry_Base {
	
	public InquiriesRegistry() {
		super();
	}
	
	public InquiriesRegistry(IExecutionCourse executionCourse, IExecutionPeriod executionPeriod, IStudent student) {
		if((executionCourse == null) || (executionPeriod == null) || (student == null)) {
			throw new NullArgumentException("The executionCourse, executionPeriod and student should not be null!");
		}
		this.setExecutionCourse(executionCourse);
		this.setExecutionPeriod(executionPeriod);
		this.setStudent(student);
		
	}
    
}
