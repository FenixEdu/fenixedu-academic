/*
 * Created on 18/Dez/2003
 *
 */
package DataBeans;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InfoScientificArea extends InfoObject
{
    private String name;


	public InfoScientificArea(){}
	
	public InfoScientificArea(String name)
	{
		setName(name);
	}
	
    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return " [name] " + name;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof InfoScientificArea)
        {
			InfoScientificArea scientificArea = (InfoScientificArea) obj;

            result = scientificArea.getName().equals(getName());
        }
        return result;
    }
}
