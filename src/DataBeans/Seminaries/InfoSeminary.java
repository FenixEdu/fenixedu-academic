/*
 * Created on 31/Jul/2003, 16:59:55
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import java.util.List;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 31/Jul/2003, 16:59:55
 * 
 */
public class InfoSeminary
{
    private Integer idInternal;
    private String name;
    private String description;
    private List equivalencies;
    private Integer allowedCandidaciesPerStudent;
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
	public List getEquivalencies()
	{
		return equivalencies;
	}

	/**
	 * @return
	 */
	public Integer getIdInternal()
	{
		return idInternal;
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
	 * @param list
	 */
	public void setEquivalencies(List list)
	{
		equivalencies= list;
	}

	/**
	 * @param integer
	 */
	public void setIdInternal(Integer integer)
	{
		idInternal= integer;
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
