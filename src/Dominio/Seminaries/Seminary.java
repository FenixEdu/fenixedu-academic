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
 * Created at Jul 23, 2003, 9:49:19 AM
 * 
 */
public class Seminary extends DomainObject implements ISeminary
{
	private Integer idInternal;
	private String name;
	private String description;
	private List equivalencies;
    private Integer allowedCandidaciesPerStudent;
    
	public Seminary()
	{
	}
	public Seminary(String name, String description, List equivalencies)
	{
		this.setDescription(description);
		this.setName(name);
		this.setEquivalencies(equivalencies);
	}
    
    public Seminary(String name, String description)
    {
        this.setDescription(description);
        this.setName(name);
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
	public List getEquivalencies()
	{
		return equivalencies;
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
	public void setEquivalencies(List list)
	{
        equivalencies= list;
	}
	/**
	 * @return
	 */
	public Integer getIdInternal()
	{
		return idInternal;
	}
	/**
	 * @param integer
	 */
	public void setIdInternal(Integer integer)
	{
		idInternal= integer;
	}
	public String toString()
	{
		String retorno;
		retorno= "[Seminary:";
		retorno += "ID=" + this.getIdInternal();
		retorno += "Name=" + this.getName();
		retorno += ",Description=" + this.getDescription();
        retorno += ",Allowed Candidacies Per Student=" + this.getAllowedCandidaciesPerStudent();
		retorno += ",Modalities=" + this.getEquivalencies() +"]";
		return retorno;
	}
	/**
	 * true if the names are equals
	 */
	public boolean equals(Object obj)
	{
		boolean equalsResult= false;
		if (obj instanceof ISeminary)
		{
			ISeminary seminary= (ISeminary) obj;
			if (seminary.getName() == null)
				equalsResult= (this.getName() == null);
			else
				equalsResult= this.getName().equalsIgnoreCase(seminary.getName());
		}
		return equalsResult;
	}
	/**
	 * @return
	 */
	public Integer getAllowedCandidaciesPerStudent()
	{
		return allowedCandidaciesPerStudent;
	}

	/**
	 * @param integer
	 */
	public void setAllowedCandidaciesPerStudent(Integer integer)
	{
		allowedCandidaciesPerStudent= integer;
	}
}
