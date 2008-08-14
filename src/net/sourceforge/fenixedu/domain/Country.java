package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Country extends Country_Base {

    static final public String PORTUGAL = "PORTUGAL";
    static final public String NATIONALITY_PORTUGUESE = "PORTUGUESA";
    static final public String DEFAULT_COUNTRY_NATIONALITY = NATIONALITY_PORTUGUESE;

    public static Comparator<Country> COMPARATOR_BY_NAME = new Comparator<Country>() {
	public int compare(Country leftCountry, Country rightCountry) {
	    int comparationResult = leftCountry.getName().compareTo(rightCountry.getName());
	    return (comparationResult == 0) ? leftCountry.getIdInternal().compareTo(rightCountry.getIdInternal())
		    : comparationResult;
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

    /**
     * If the person country is undefined it is set to default. In a not
     * distance future this will not be needed since the coutry can never be
     * null.
     */
    public static Country readDefault() {
	for (final Country country : RootDomainObject.getInstance().getCountrysSet()) {
	    if (country.isDefaultCountry()) {
		return country;
	    }
	}

	return null;
    }

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
	    if (!country.getName().equalsIgnoreCase(PORTUGAL)) {
		result.add(country);
	    } else {
		if (country.getNationality().equalsIgnoreCase(NATIONALITY_PORTUGUESE)) {
		    result.add(country);
		}
	    }
	}

	return result;

    }

    public String getFilteredNationality() {
	final String nationality = getNationality();
	final String nationalitySpecialCase = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale())
		.getString("label.person.portugueseNationality").toUpperCase();
	return nationality.trim().contains(nationalitySpecialCase) ? nationalitySpecialCase : nationality;
    }

    public boolean isDefaultCountry() {
	return getDefaultCountry();
    }

}
