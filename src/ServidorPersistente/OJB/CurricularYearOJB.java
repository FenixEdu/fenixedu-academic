package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularYear;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularYearOJB extends ObjectFenixOJB implements IPersistentCurricularYear {

	public CurricularYearOJB() {
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularYear.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(ICurricularYear curricularYearToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularYear curricularYearFromDB = null;

		// If there is nothing to write, simply return.
		if (curricularYearToWrite == null) {
			return;
		}

		// Read CurricularYear from database.
		curricularYearFromDB = this.readCurricularYearByYear(curricularYearToWrite.getYear());

		// If CurricularYear is not in database, then write it.
		if (curricularYearFromDB == null) {
			super.lockWrite(curricularYearToWrite);
		// else If the CurricularYear is mapped to the database, then write any existing changes.
		} else if ((curricularYearToWrite instanceof CurricularYear) && ((CurricularYear) curricularYearFromDB).getInternalID().equals(((CurricularYear) curricularYearToWrite).getInternalID())) {
			super.lockWrite(curricularYearToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularYear curricularYear) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularYear);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia {

		try {
			ICurricularYear curricularYear = null;
			String oqlQuery = "select all from " + CurricularYear.class.getName();
			oqlQuery += " where year = $1";
			query.create(oqlQuery);
			query.bind(year);

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				curricularYear = (ICurricularYear) result.get(0);
			}
			return curricularYear;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularYear.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularYear) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}