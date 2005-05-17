/*
 * Created on Apr 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author Ricardo Rodrigues
 *
 */

public class NonAffiliatedTeacher extends DomainObject implements INonAffiliatedTeacher{

    protected String name;
    protected Integer keyInstitution;
    protected IInstitution institution;
    protected List executionCourses;
    
    public IInstitution getInstitution() {
        return institution;
    }
    
    public void setInstitution(IInstitution institution) {
        this.institution = institution;
    }
    
    public Integer getKeyInstitution() {
        return keyInstitution;
    }
    
    public void setKeyInstitution(Integer keyInstitution) {
        this.keyInstitution = keyInstitution;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public List getExecutionCourses() {
        return executionCourses;
    }
    

    public void setExecutionCourses(List executionCourses) {
        this.executionCourses = executionCourses;
    }
    
      
    
}
