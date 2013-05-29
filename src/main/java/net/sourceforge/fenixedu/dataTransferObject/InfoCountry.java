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

    public Country getCountry() {
        return country;
    }

    @Override
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

    public String getNameAndNationality() {
        return getCountry().getName() + " - " + getCountry().getNationality();
    }

    @Override
    public String getExternalId() {
        return getCountry().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
