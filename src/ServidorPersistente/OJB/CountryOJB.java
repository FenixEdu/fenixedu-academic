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
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CountryOJB extends ObjectFenixOJB implements IPersistentCountry
{

    public CountryOJB()
    {
    }

    public void writeCountry(ICountry countryToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        ICountry countryFromDB = null;

        // If there is nothing to write, simply return.
        if (countryToWrite == null)
            return;

        // Read country from database.
        countryFromDB = this.readCountryByName(countryToWrite.getName());

        // If country is not in database, then write it.
        if (countryFromDB == null)
            super.lockWrite(countryToWrite);
        // else If the country is mapped to the database, then write any
        // existing changes.
        else if (
            (countryToWrite instanceof Country)
                && ((Country) countryFromDB).getIdInternal().equals(
                    ((Country) countryToWrite).getIdInternal()))
        {

            countryFromDB.setCode(countryToWrite.getCode());
            countryFromDB.setNationality(countryToWrite.getNationality());
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
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