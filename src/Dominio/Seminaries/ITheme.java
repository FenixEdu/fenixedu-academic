/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import java.util.List;

import Dominio.IDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 10:14:53 AM
 *  
 */
public interface ITheme extends IDomainObject {
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

    public abstract List getCaseStudies();

    public abstract void setCaseStudies(List list);

    public abstract String getShortName();

    public abstract void setShortName(String string);

}