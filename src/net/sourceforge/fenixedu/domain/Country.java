/*
 * Country.java
 * 
 * Created on 28 of December 2002, 10:04
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

public class Country extends Country_Base {

	public Country() {
		super();
    }

    public Country(final String name, final String nationality, final String code) {
		super();
		setCode(code);
		setNationality(nationality);
		setName(name);
    }

    public boolean equals(final Object obj) {
        if (obj instanceof ICountry) {
            final ICountry country = (ICountry) obj;
            return getIdInternal().equals(country.getIdInternal());
        }
        return false;
    }

    public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[COUNTRY idInternal= ");
		stringBuilder.append(getIdInternal());
		stringBuilder.append(", name= ");
		stringBuilder.append(getName());
		stringBuilder.append(", nationality= ");
		stringBuilder.append(getNationality());
		stringBuilder.append(", code= ");
		stringBuilder.append(getCode());
		stringBuilder.append("]");
		return stringBuilder.toString();
    }

}