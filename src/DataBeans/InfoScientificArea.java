/*
 * Created on 18/Dez/2003
 *
 */
package DataBeans;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InfoScientificArea
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
        boolean resultado = false;
        if (obj instanceof InfoScientificArea)
        {
			InfoScientificArea scientificArea = (InfoScientificArea) obj;

            resultado = scientificArea.getName().equals(getName());
        }
        return false;
    }
}
