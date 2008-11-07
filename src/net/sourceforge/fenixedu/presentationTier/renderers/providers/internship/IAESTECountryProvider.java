package net.sourceforge.fenixedu.presentationTier.renderers.providers.internship;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class IAESTECountryProvider implements DataProvider {
    private static final List<String> IAESTE_COUNTRIES = Arrays.asList(new String[] { "ARG", "ARM", "AUS", "AUT", "BLR", "BEL",
	    "BIH", "BWA", "BRA", "BGR", "CAN", "CHN", "COL", "HRV", "CYP", "CZE", "DNK", "ECU", "EGY", "EST", "MKD", "FIN",
	    "FRA", "GMB", "GEO", "DEU", "GHA", "GRC", "HUN", "ISL", "IND", "IRN", "IRQ", "IRL", "ISR", "ITA", "JAM", "JPN",
	    "JOR", "KAZ", "KEN", "LVA", "LBN", "LTU", "LUX", "MLT", "MEX", "MNG", "NOR", "OMN", "PAK", "PAN", "PER", "POL",
	    "ROM", "RUS", "SLE", "SVK", "SVN", "ZAF", "ESP", "LKA", "SWE", "CHE", "SYR", "TJK", "TZA", "THA", "TUN", "TUR",
	    "UKR", "ARE", "GBR", "URY", "USA", "UZB", "VNM", "MAC", "CS", "KOR" });

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final Set<Country> countrySet = new TreeSet<Country>(Country.COMPARATOR_BY_NAME);
	for (Object object : RootDomainObject.readAllDomainObjects(Country.class)) {
	    Country country = (Country) object;
	    String code = country.getThreeLetterCode() != null ? country.getThreeLetterCode() : country.getCode();
	    if (IAESTE_COUNTRIES.contains(code)) {
		countrySet.add(country);
	    }
	}
	return countrySet;
    }
}
