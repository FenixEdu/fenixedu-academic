/*
 * Created on 4/Ago/2003, 19:04:21
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import java.util.List;

import DataBeans.InfoObject;
import Dominio.Seminaries.ICaseStudy;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 4/Ago/2003, 19:04:21
 * 
 */
public class InfoCaseStudy extends InfoObject
{
     
      private String themeName;
      private List seminaryCandidacies;
      private String code;
      private String description;
      private String name;
	/**
	 * @return
	 */
	public String getCode()
	{
		return code;
	}

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
	 * @return
	 */
	public List getSeminaryCandidacies()
	{
		return seminaryCandidacies;
	}

	/**
	 * @return
	 */
	public String getThemeName()
	{
		return themeName;
	}

	/**
	 * @param string
	 */
	public void setCode(String string)
	{
		code= string;
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
	 * @param list
	 */
	public void setSeminaryCandidacies(List list)
	{
		seminaryCandidacies= list;
	}

	/**
	 * @param string
	 */
	public void setThemeName(String string)
	{
		themeName= string;
	}
    
    public String toString()
    {
        String result = "[InfoCaseStudy:";
        result+="Name=" + this.getName() + ";";
        result+="IdInternal=" + this.getIdInternal() + ";";
        result+="Code=" + this.getCode() + ";";
        result+="Description=" + this.getDescription() + "]";        
        return result;
    }
    
    public void copyFromDomain(ICaseStudy caseStudy) {
        super.copyFromDomain(caseStudy);
        if (caseStudy != null) {
        	setCode(caseStudy.getCode());
        	setDescription(caseStudy.getDescription());
        	setName(caseStudy.getName());
        	if(caseStudy.getSeminaryTheme() != null) {
        	    setThemeName(caseStudy.getSeminaryTheme().getName());
        	}
        }
    }

    public static InfoCaseStudy newInfoFromDomain(ICaseStudy caseStudy) {
    	InfoCaseStudy infoCaseStudy = null;
        if (caseStudy != null) {
        	infoCaseStudy = new InfoCaseStudy();
        	infoCaseStudy.copyFromDomain(caseStudy);
        }
        return infoCaseStudy;
    }
}
