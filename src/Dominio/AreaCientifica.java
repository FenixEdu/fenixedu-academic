/*
 * AreaCientifica.java
 *
 * Created on 17 de Dezembro de 2003, 18:08
 */

package Dominio;

/**
 * 
 * @author  Nuno Correia
 * @author  Ricardo Rodrigues
 */

public class AreaCientifica extends DomainObject implements IAreaCientifica {

    private String name;

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
        if (obj instanceof IAreaCientifica)
        {
            IAreaCientifica areaCientifica = (IAreaCientifica) obj;

            resultado = areaCientifica.getName().equals(getName());            
        }
		return false;
    }
}