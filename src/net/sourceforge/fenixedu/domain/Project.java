/*
 * Created on Nov 7, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EvaluationType;

public class Project extends Project_Base {

    private Project() {    	
        this.setOjbConcreteClass(Project.class.getName());
    }

    public Project(String name, Date begin, Date end, String description, IExecutionCourse executionCourse) {        
        this();
        if (name == null || begin == null || end == null || executionCourse == null) {
            throw new NullPointerException();
        }
        if (begin.after(end)) {
            throw new DomainException("error.evaluation.begin.sooner.end");
        }
        this.setName(name);
        this.setBegin(begin);
        this.setEnd(end);
        this.setDescription(description != null ? description : "");
        this.addAssociatedExecutionCourses(executionCourse);
    }
    
    public void edit(String name, Date begin, Date end, String description) {
        if (name == null || begin == null || end == null) {
            throw new NullPointerException();
        }
        if (begin.after(end)) {
            throw new DomainException("error.evaluation.begin.sooner.end");
        }
        setName(name);
        setBegin(begin);
        setEnd(end);
        setDescription((description != null) ? description : "");
    }
    
	public EvaluationType getEvaluationType() {
		return EvaluationType.PROJECT_TYPE;
	}
}
