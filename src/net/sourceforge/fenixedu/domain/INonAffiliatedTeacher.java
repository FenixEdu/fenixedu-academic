/*
 * Created on Apr 28, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

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

}
