/*
 * Created on Dec 5, 2003
 *  
 */
package Dominio;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class WorkLocation extends DomainObject implements IWorkLocation
{
    private String name;

    /**
	 * @return Returns the name.
	 */
    public String getName()
    {
        return name;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name)
    {
        this.name = name;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;

        if (obj instanceof IWorkLocation)
        {
            IWorkLocation workLocation = (IWorkLocation) obj;
            result = this.name.equals(workLocation.getName());
        }
        return result;
    }

}
