/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 10:17:47 AM
 *  
 */
public interface IModality extends IDomainObject {
    /**
     * @return
     */
    public abstract String getDescription();

    /**
     * @return
     */
    public abstract String getName();

    /**
     * @param string
     */
    public abstract void setDescription(String description);

    /**
     * @param string
     */
    public abstract void setName(String name);
}