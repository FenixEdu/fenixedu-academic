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
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CountryOJB extends ObjectFenixOJB implements IPersistentCountry {

	public CountryOJB() {
	}

	public void deleteAllCountrys() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Country.class.getName();
		super.deleteAll(oqlQuery);
	}

	public void writeCountry(ICountry countryToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ICountry countryFromDB = null;

		// If there is nothing to write, simply return.
		if (countryToWrite == null)
			return;

		// Read country from database.
		countryFromDB = this.readCountryByName(countryToWrite.getName());

		// If country is not in database, then write it.
		if (countryFromDB == null)
			super.lockWrite(countryToWrite);
		// else If the country is mapped to the database, then write any existing changes.
		else if (
			(countryToWrite instanceof Country)
				&& ((Country) countryFromDB).getIdInternal().equals(
					((Country) countryToWrite).getIdInternal())) {

			countryFromDB.setCode(countryToWrite.getCode());
			countryFromDB.setNationality(countryToWrite.getNationality());
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public ICountry readCountryByName(String name)
		throws ExcepcaoPersistencia {
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

	public ICountry readCountryByCode(String code)
		throws ExcepcaoPersistencia {
		try {
			ICountry country = null;
			String oqlQuery = "select all from " + Country.class.getName();
			oqlQuery += " where code = $1";
			query.create(oqlQuery);
			query.bind(code);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				country = (ICountry) result.get(0);
			return country;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ICountry readCountryByNationality(String nationality)
		throws ExcepcaoPersistencia {
		try {
			ICountry country = null;
			String oqlQuery = "select all from " + Country.class.getName();
			oqlQuery += " where nationality = $1";
			query.create(oqlQuery);
			query.bind(nationality);
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
			String oqlQuery =
				"select all from "
					+ Country.class.getName()
					+ " order by nationality asc";

			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					countryList.add(iterator.next());
			}
			return countryList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}