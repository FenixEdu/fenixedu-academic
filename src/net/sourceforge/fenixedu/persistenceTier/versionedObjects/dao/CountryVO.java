package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class CountryVO extends VersionedObjectsBase implements IPersistentCountry {

    public Country readCountryByName(String name) throws ExcepcaoPersistencia {
        List<Country> countries = (List<Country>) readAll(Country.class);
        
        for (Country country : countries) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        
        return null;
    }

    public Country readCountryByCode(String code) throws ExcepcaoPersistencia {
        List<Country> countries = (List<Country>) readAll(Country.class);
        
        for (Country country : countries) {
            if (country.getCode().equals(code)) {
                return country;
            }
        }
        
        return null;
    }

    public Country readCountryByNationality(String nationality) throws ExcepcaoPersistencia {
        List<Country> countries = (List<Country>) readAll(Country.class);
        
        for (Country country : countries) {
            if (country.getNationality().equals(nationality)) {
                return country;
            }
        }
        
        return null;
    }

    public List readAllCountrys() throws ExcepcaoPersistencia {
        List<Country> countries = (List<Country>) readAll(Country.class);
        
        ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("nationality"), true);
        Collections.sort(countries, comparatorChain);

        return countries;
    }

}