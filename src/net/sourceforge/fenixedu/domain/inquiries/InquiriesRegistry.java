/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;

import org.apache.commons.lang.NullArgumentException;


/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesRegistry extends InquiriesRegistry_Base {
	
	public InquiriesRegistry() {
		super();
	}
	
	public InquiriesRegistry(ExecutionCourse executionCourse, ExecutionPeriod executionPeriod, Student student) {
		if((executionCourse == null) || (executionPeriod == null) || (student == null)) {
			throw new NullArgumentException("The executionCourse, executionPeriod and student should not be null!");
		}
		this.setExecutionCourse(executionCourse);
		this.setExecutionPeriod(executionPeriod);
		this.setStudent(student);
		
	}
    
}
