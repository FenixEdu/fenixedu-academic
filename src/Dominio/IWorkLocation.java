/*
 * Created on Dec 5, 2003
 *
 */
package Dominio;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) 
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public interface IWorkLocation extends IDomainObject
{
    /**
     * @return Returns the name.
     */
    public abstract String getName();
    /**
     * @param name The name to set.
     */
    public abstract void setName(String name);
}