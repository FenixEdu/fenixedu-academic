/*
 * Created on 1/Ago/2003, 21:13:13
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import DataBeans.InfoObject;
import Dominio.Seminaries.ITheme;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 1/Ago/2003, 21:13:13
 * 
 */
public class InfoTheme extends InfoObject
{
   
    private String description;
    private String name;
    private String shortName;
	/**
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string)
	{
		description= string;
	}

	

	/**
	 * @param string
	 */
	public void setName(String string)
	{
		name= string;
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
    
    public String toString()
    {
        String retorno;
        retorno= "[InfoTheme:";
        retorno += "ID=" + this.getIdInternal();
        retorno += "Name=" + this.getName();
        retorno += ",Description=" + this.getDescription();
        retorno += ",Short Name=" + this.getShortName() + "]";
        return retorno;
    }

	public void copyFromDomain(ITheme theme) {
		super.copyFromDomain(theme);
		if(theme!=null) {
			setDescription(theme.getDescription());
			setName(theme.getName());
			setShortName(theme.getShortName());
		}
	}
	
	public static InfoTheme newInfoFromDomain(ITheme theme) {
		InfoTheme infoTheme = null;
		
		if(theme != null) {
			infoTheme = new InfoTheme();
			infoTheme.copyFromDomain(theme);
		}
		return infoTheme;
	}

}
