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

	public void deleteAllCurricularYears() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularYear.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void writeCurricularYear(ICurricularYear CurricularYearToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularYear CurricularYearFromDB = null;

		// If there is nothing to write, simply return.
		if (CurricularYearToWrite == null) {
			return;
		}

		// Read CurricularYear from database.
		CurricularYearFromDB = this.readCurricularYearByYear(CurricularYearToWrite.getYear());

		// If CurricularYear is not in database, then write it.
		if (CurricularYearFromDB == null) {
			super.lockWrite(CurricularYearToWrite);
		// else If the CurricularYear is mapped to the database, then write any existing changes.
		} else if ((CurricularYearToWrite instanceof CurricularYear) && ((CurricularYear) CurricularYearFromDB).getInternalID().equals(((CurricularYear) CurricularYearToWrite).getInternalID())) {
			super.lockWrite(CurricularYearToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void deleteCurricularYear(ICurricularYear CurricularYear) throws ExcepcaoPersistencia {
		try {
			super.delete(CurricularYear);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia {

		try {
			ICurricularYear CurricularYear = null;
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
				CurricularYear = (ICurricularYear) result.get(0);
			}
			return CurricularYear;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readAllCurricularYears() throws ExcepcaoPersistencia {

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