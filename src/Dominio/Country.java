/*
 * Country.java
 * 
 * Created on 28 of December 2002, 10:04
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package Dominio;

public class Country extends DomainObject implements ICountry
{
    private String name;
    private String nationality;
    private String code;

    public Country()
    {
    }

    public Country(String name, String nationality, String code)
    {
        this.code = code;
        this.nationality = nationality;
        this.name = name;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof Country)
        {
            Country d = (Country) obj;
            resultado =
                (getName().equals(d.getName())
                    && getNationality().equals(d.getNationality())
                    && getCode().equals(d.getCode()));
        }
        return resultado;
    }

    public String toString()
    {
        String result = "[COUNTRY";
        result += ", idInternal =" + getIdInternal();
        result += ", Name =" + name;
        result += ", Nationality =" + nationality;
        result += ", Code =" + code;
        result += "]";
        return result;
    }

    /**
	 * Returns the code.
	 * 
	 * @return String
	 */
    public String getCode()
    {
        return code;
    }

    /**
	 * Returns the name.
	 * 
	 * @return String
	 */
    public String getName()
    {
        return name;
    }

    /**
	 * Returns the nationality.
	 * 
	 * @return String
	 */
    public String getNationality()
    {
        return nationality;
    }

    /**
	 * Sets the code.
	 * 
	 * @param code
	 *            The code to set
	 */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to set
	 */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
	 * Sets the nationality.
	 * 
	 * @param nationality
	 *            The nationality to set
	 */
    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }

}