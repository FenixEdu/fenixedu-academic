/*
 * Created on 1/Mar/2004
 *  
 */
package Dominio;

/**
 * @author Tânia Pousão
 *  
 */
public class CostCenter extends DomainObject implements ICostCenter
{
	private String code;
	private String departament;
	private String section1;
	private String section2;

	public CostCenter()
	{
	}

	public CostCenter(String code, String departament, String section1, String section2)
	{
		setCode(code);
		setDepartament(departament);
		setSection1(section1);
		setSection2(section2);
	}

	/**
	 * @return Returns the departament.
	 */
	public String getDepartament()
	{
		return departament;
	}

	/**
	 * @param departament
	 *            The departament to set.
	 */
	public void setDepartament(String departament)
	{
		this.departament = departament;
	}

	/**
	 * @return Returns the section1.
	 */
	public String getSection1()
	{
		return section1;
	}

	/**
	 * @param section1
	 *            The section1 to set.
	 */
	public void setSection1(String section1)
	{
		this.section1 = section1;
	}

	/**
	 * @return Returns the section2.
	 */
	public String getSection2()
	{
		return section2;
	}

	/**
	 * @param section2
	 *            The section2 to set.
	 */
	public void setSection2(String section2)
	{
		this.section2 = section2;
	}

	/**
	 * @return Returns the sigla.
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param sigla
	 *            The sigla to set.
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

}
