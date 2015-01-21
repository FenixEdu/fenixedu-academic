package org.fenixedu.academic.task;

import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.organizationalStructure.CountryUnit;
import org.fenixedu.academic.domain.organizationalStructure.PlanetUnit;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.joda.time.YearMonthDay;

import pt.ist.standards.geographic.Planet;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class LoadCountries extends CustomTask {

	@Override
	public void runTask() throws Exception {
		Locale PT = new Locale("pt");
	    Locale EN = new Locale("en");
		Country defaultCountry = null;
		
        for (pt.ist.standards.geographic.Country metaData : Planet.getEarth().getPlaces()) {
            String localizedNamePT = null;
            try {
                localizedNamePT = metaData.getLocalizedName(PT);
            } catch (MissingResourceException e) {
            }

            String localizedNameEN = null;
            try {
                localizedNameEN = metaData.getLocalizedName(EN);
            } catch (MissingResourceException e) {
            }

            if (localizedNameEN == null && localizedNamePT == null) {
                continue;
            }

            if (localizedNamePT == null) {
                localizedNamePT = localizedNameEN;
            }

            if (localizedNameEN == null) {
                localizedNameEN = localizedNamePT;
            }
            String nationalityPT = null;
            try {
                nationalityPT = metaData.getNationality(PT);
            } catch (MissingResourceException e) {
            }

            String nationalityEN = null;
            try {
                nationalityEN = metaData.getNationality(EN);
            } catch (MissingResourceException e) {
            }

            if (nationalityPT == null) {
                if (nationalityEN == null) {
                    nationalityPT = localizedNamePT;
                } else {
                    nationalityPT = nationalityEN;
                }
            }

            if (nationalityEN == null) {
                if (nationalityPT == null) {
                    nationalityEN = localizedNameEN;
                } else {
                    nationalityEN = nationalityPT;
                }
            }

            final MultiLanguageString countryName = new MultiLanguageString(MultiLanguageString.pt, localizedNamePT);
            countryName.append(new MultiLanguageString(MultiLanguageString.en, localizedNameEN));

            final String code = metaData.alpha2;
            final String threeLetterCode = metaData.alpha3;

            final Country country =
                    new Country(countryName,
                            new MultiLanguageString(MultiLanguageString.pt, nationalityPT).append(new MultiLanguageString(
                                    MultiLanguageString.en, nationalityEN)), code, threeLetterCode);
            if (StringUtils.equals(threeLetterCode, "PRT")) {
                defaultCountry = country;
            }
        }

        defaultCountry.setDefaultCountry(Boolean.TRUE);
        //return defaultCountry;
        
        final Bennu rootDomainObject = Bennu.getInstance();
        final PlanetUnit planetUnit =
                PlanetUnit.createNewPlanetUnit(new MultiLanguageString(Locale.getDefault(), "Earth"), null, null, "E",
                        new YearMonthDay(), null, null, null, null, false, null);
        rootDomainObject.setEarthUnit(planetUnit);
        for (final Country country : Country.readDistinctCountries()) {
            CountryUnit.createNewCountryUnit(new MultiLanguageString(Locale.getDefault(), country.getName()), null, null,
                    country.getCode(), new YearMonthDay(), null, planetUnit, null, null, false, null);
        }
	}
}
