package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Country extends Country_Base {
    
    public static Comparator<Country> COMPARATOR_BY_NAME = new Comparator<Country>() {
	public int compare(Country leftCountry, Country rightCountry) {
	    int comparationResult = leftCountry.getName().compareTo(
		    rightCountry.getName());
	    return (comparationResult == 0) ? leftCountry.getIdInternal().compareTo(
		    rightCountry.getIdInternal()) : comparationResult;
	}
    };

    public Country() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Country(final String name, final String nationality, final String code) {
	this();
	setCode(code);
	setNationality(nationality);
	setName(name);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static Country readCountryByNationality(final String nationality) {
	for (final Country country : RootDomainObject.getInstance().getCountrysSet()) {
	    if (country.getNationality().equals(nationality)) {
		return country;
	    }
	}
	return null;
    }

    // FIXME: This method is wrong and should not exist
    // exists only because PORTUGAL is repeated
    // country object should be split in 2 objects: Country and District
    // where Country has many Districts
    public static Set<Country> readDistinctCountries() {
	final Set<Country> result = new HashSet<Country>();
	for (final Country country : RootDomainObject.getInstance().getCountrysSet()) {
	    if (!country.getName().equalsIgnoreCase("PORTUGAL")) {
		result.add(country);
	    } else {
		if (country.getNationality().equals("PORTUGUESA NATURAL DO CONTINENTE")) {
		    result.add(country);
		}
	    }
	}
	
	return result;

    }
}