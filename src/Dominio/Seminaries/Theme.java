/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import java.util.List;

import Dominio.DomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at Jul 23, 2003, 10:12:40 AM
 * 
 */
public class Theme extends DomainObject implements ITheme
{
	private Integer idInternal;
	private String description;
	private String name;
    private String shortName;
    
    private List caseStudies;
    
    private Integer seminaryIdInternal;
    
	public Theme()
	{
	}
	public Theme(String name, String description)
	{
		this.setName(name);
		this.setDescription(description);
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
	public String toString()
	{
		String retorno;
		retorno= "[Theme:";
		retorno += "ID=" + this.getIdInternal();
		retorno += "Name=" + this.getName();
		retorno += ",Description=" + this.getDescription();
        retorno += ",Short Name=" + this.getShortName() + "]";
		return retorno;
	}
	/**
	 * true if the names are equals
	 */
	public boolean equals(Object obj)
	{
		boolean equalsResult= false;
		if (obj instanceof ITheme)
		{
			ITheme theme= (ITheme) obj;
			if (theme.getName() == null)
				equalsResult= (this.getName() == null);
			else
				equalsResult = this.getName().equalsIgnoreCase(theme.getName());
		}
		return equalsResult;
	}
	/**
	 * @return
	 */
	public Integer getSeminaryIdInternal()
	{
		return seminaryIdInternal;
	}

	/**
	 * @param integer
	 */
	public void setSeminaryIdInternal(Integer integer)
	{
		seminaryIdInternal= integer;
	}

	/**
	 * @return
	 */
	public List getCaseStudies()
	{
		return caseStudies;
	}

	/**
	 * @param list
	 */
	public void setCaseStudies(List list)
	{
		caseStudies= list;
	}
	/**
	 * @return
	 */
	public String getShortName()
	{
		return shortName;
	}

	/**
	 * @param string
	 */
	public void setShortName(String string)
	{
		shortName= string;
	}

}
