/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import Dominio.DomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at Jul 23, 2003, 10:15:55 AM
 * 
 */
public class Modality extends DomainObject implements IModality
{
    private Integer idInternal;
    private String description;
    private String name;
    
	public Modality()
	{
	}
	public Modality(String name, String description)
	{
		this.setDescription(description);
		this.setName(name);
	}

	/**
	 * @return
	 */
	public String getDescription()
	{
		return this.description;
	}
	/**
	 * @return
	 */
	public Integer getIdInternal()
	{
		return this.idInternal;
	}
	/**
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	/**
	 * @param string
	 */
	public void setDescription(String description)
	{
		this.description= description;
	}
	/**
	 * @param integer
	 */
	public void setIdInternal(Integer idInternal)
	{
		this.idInternal= idInternal;
	}
	/**
	 * @param string
	 */
	public void setName(String name)
	{
		this.name= name;
	}
    /**
     * 
     */
	public String toString()
	{
		String retorno;
		retorno= "[Modality:";
		retorno += "ID=" + this.getIdInternal();
		retorno += "Name=" + this.getName();
		retorno += ",Description=" + this.getDescription() + "]";
		return retorno;
	}
    /**
     * true if the names are equals
     */
	public boolean equals(Object obj)
	{
		boolean equalsResult= false;
		if (obj instanceof IModality)
		{
			IModality modality= (IModality) obj;
			if (modality.getName() == null)
				equalsResult= (this.getName() == null);
			else
				equalsResult= this.getName().equalsIgnoreCase(modality.getName());
		}
		return equalsResult;
	}
}
