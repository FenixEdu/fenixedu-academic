/*
 * InfoCountry.java
 * 
 * Created on 13 de Dezembro de 2002, 16:28
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;

/**
 * @author tfc130
 */
public class InfoCountry extends InfoObject {

    private final DomainReference<Country> countryDomainReference;

    public InfoCountry(final Country country) {
	   countryDomainReference = new DomainReference<Country>(country);
    }

    public Country getCountry() {
        return countryDomainReference == null ? null : countryDomainReference.getObject();
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoCountry && getCountry() == ((InfoCountry) obj).getCountry();
    }

    public String getCode() {
        return getCountry().getCode();
    }

    public String getName() {
        return getCountry().getName();
    }

    public String getNationality() {
        return getCountry().getNationality();
    }

    public static InfoCountry newInfoFromDomain(final Country country) {
	return country == null ? null : new InfoCountry(country);
    }

    @Override
    public Integer getIdInternal() {
	return getCountry().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

}
