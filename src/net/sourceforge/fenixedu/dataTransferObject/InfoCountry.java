/*
 * InfoCountry.java
 * 
 * Created on 13 de Dezembro de 2002, 16:28
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Country;

/**
 * @author tfc130
 */
public class InfoCountry extends InfoObject {

    private final Country country;

    public InfoCountry(final Country country) {
	this.country = country;
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoCountry && country == ((InfoCountry) obj).country;
    }

    public String getCode() {
        return country.getCode();
    }

    public String getName() {
        return country.getName();
    }

    public String getNationality() {
        return country.getNationality();
    }

    public static InfoCountry newInfoFromDomain(final Country country) {
	return country == null ? null : new InfoCountry(country);
    }

    @Override
    public Integer getIdInternal() {
	return country.getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

}
