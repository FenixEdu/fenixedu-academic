/*
 * Created on 1/Ago/2003, 21:14:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 1/Ago/2003, 21:14:43
 * 
 */
public class InfoModality
{
    private Integer idInternal;
    private String description;
    private String name;
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

}
