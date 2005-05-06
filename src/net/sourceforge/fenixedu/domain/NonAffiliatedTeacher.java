/*
 * Created on Apr 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Ricardo Rodrigues
 *
 */

public class NonAffiliatedTeacher extends DomainObject_Base implements INonAffiliatedTeacher{

    protected String name;
    protected Integer keyInstitution;
    protected IInstitution institution;
    
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
      
    
}
