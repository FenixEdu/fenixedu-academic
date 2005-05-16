/*
 * Created on Apr 28, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author Ricardo Rodrigues
 *
 */

public interface INonAffiliatedTeacher extends IDomainObject {
    
    public IInstitution getInstitution();
    
    public void setInstitution(IInstitution institution);
    
    public Integer getKeyInstitution();
    
    public void setKeyInstitution(Integer keyInstitution);
    
    public String getName();
    
    public void setName(String name) ;
    
    public List getExecutionCourses();

    public void setExecutionCourses(List executionCourses);

}
