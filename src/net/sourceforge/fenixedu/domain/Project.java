/*
 * Created on Nov 7, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

public class Project extends Project_Base {

    private Project() {
        this.setOjbConcreteClass(Project.class.getName());
    }

    public Project(String name, Date begin, Date end, String description, IExecutionCourse executionCourse) {        
        this();
        if (name == null || begin == null || end == null || executionCourse == null) {
            throw new NullPointerException();
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
        setName(name);
        setBegin(begin);
        setEnd(end);
        setDescription((description != null) ? description : "");
    }
}
