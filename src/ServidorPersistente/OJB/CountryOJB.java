/*
 * CountryOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */
 
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.Country;
import Dominio.ICountry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCountry;

public class CountryOJB extends ObjectFenixOJB implements IPersistentCountry {
    
    public CountryOJB() {
    }
    
    public void deleteAllCountrys() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Country.class.getName();
        super.deleteAll(oqlQuery);
    }
    
    public void writeCountry(ICountry country) throws ExcepcaoPersistencia {
        super.lockWrite(country);
    }
    
    public ICountry readCountryByName(String name) throws ExcepcaoPersistencia {
        try {
            ICountry country = null;
            String oqlQuery = "select all from " + Country.class.getName();
            oqlQuery += " where name = $1";
            query.create(oqlQuery);
            query.bind(name);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0)
                country = (ICountry) result.get(0);
            return country;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void deleteCountryByName(String name) throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Country.class.getName();
            oqlQuery += " where name = $1";
            query.create(oqlQuery);
            query.bind(name);
            List result = (List) query.execute();
            ListIterator iterator = result.listIterator();
            while (iterator.hasNext())
                super.delete(iterator.next());
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void deleteCountry(ICountry country) throws ExcepcaoPersistencia {
        super.delete(country);
    }

    public ArrayList readAllCountrys() throws ExcepcaoPersistencia {
        try {
            ArrayList countryList = new ArrayList();
            String oqlQuery = "select all from " + Country.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            super.lockRead(result);
            if (result.size() != 0) {
                ListIterator iterator = result.listIterator();
                while(iterator.hasNext())
                    countryList.add((ICountry)iterator.next());
            }
            return countryList;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
}