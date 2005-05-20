package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;

import org.apache.ojb.broker.query.Criteria;

public class CountryOJB extends PersistentObjectOJB implements IPersistentCountry {

    public ICountry readCountryByName(String name) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        return (ICountry) queryObject(Country.class, crit);
    }

    public ICountry readCountryByCode(String code) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("code", code);
        return (ICountry) queryObject(Country.class, crit);
    }

    public ICountry readCountryByNationality(String nationality) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("nationality", nationality);
        return (ICountry) queryObject(Country.class, crit);
    }

    public List readAllCountrys() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(Country.class, crit, "nationality", true);
    }

}