/*
 * Created on Apr 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Ricardo Rodrigues
 *
 */

public class Institution extends DomainObject_Base implements IInstitution{

    protected String name;

    public String getName() {
        return name;
    }
    

    public void setName(String name) {
        this.name = name;
    }
    

}
