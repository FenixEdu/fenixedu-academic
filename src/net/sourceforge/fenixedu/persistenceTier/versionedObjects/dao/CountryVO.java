package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class CountryVO extends VersionedObjectsBase implements IPersistentCountry {

    public ICountry readCountryByName(String name) throws ExcepcaoPersistencia {
        List<ICountry> countries = (List<ICountry>) readAll(Country.class);
        
        for (ICountry country : countries) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        
        return null;
    }

    public ICountry readCountryByCode(String code) throws ExcepcaoPersistencia {
        List<ICountry> countries = (List<ICountry>) readAll(Country.class);
        
        for (ICountry country : countries) {
            if (country.getCode().equals(code)) {
                return country;
            }
        }
        
        return null;
    }

    public ICountry readCountryByNationality(String nationality) throws ExcepcaoPersistencia {
        List<ICountry> countries = (List<ICountry>) readAll(Country.class);
        
        for (ICountry country : countries) {
            if (country.getNationality().equals(nationality)) {
                return country;
            }
        }
        
        return null;
    }

    public List readAllCountrys() throws ExcepcaoPersistencia {
        List<ICountry> countries = (List<ICountry>) readAll(Country.class);
        
        ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("nationality"), true);
        Collections.sort(countries, comparatorChain);

        return countries;
    }

}