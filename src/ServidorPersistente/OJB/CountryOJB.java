/*
 * CountryOJB.java
 * 
 * Created on 25 de Agosto de 2002, 1:02
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package ServidorPersistente.OJB;

import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Country;
import Dominio.ICountry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCountry;

public class CountryOJB extends ObjectFenixOJB implements IPersistentCountry
{

    public CountryOJB()
    {
    }

   

    public ICountry readCountryByName(String name) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        return (ICountry) queryObject(Country.class, crit);

    }

    public ICountry readCountryByCode(String code) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("code", code);
        return (ICountry) queryObject(Country.class, crit);

    }

    public ICountry readCountryByNationality(String nationality) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("nationality", nationality);
        return (ICountry) queryObject(Country.class, crit);

    }

    public void deleteCountryByName(String name) throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        List result = queryList(Country.class, crit);
        ListIterator iterator = result.listIterator();
        while (iterator.hasNext())
        {

            super.delete(iterator.next());
        }

    }

    public void deleteCountry(ICountry country) throws ExcepcaoPersistencia
    {
        super.delete(country);
    }

    public List readAllCountrys() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addOrderBy("nationality", true);
        return queryList(Country.class, crit);
    }
}