package net.sourceforge.fenixedu.domain;

public class Country extends Country_Base {

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

    
    //  -------------------------------------------------------------
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
}