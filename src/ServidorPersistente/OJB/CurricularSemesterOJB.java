package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularSemester;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularSemesterOJB extends ObjectFenixOJB implements IPersistentCurricularSemester {

	public CurricularSemesterOJB() {
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularSemester.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(ICurricularSemester curricularSemesterToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularSemester curricularSemesterFromDB = null;

		// If there is nothing to write, simply return.
		if (curricularSemesterToWrite == null) {
			return;
		}

		// Read CurricularSemester from database.
		curricularSemesterFromDB = this.readCurricularSemesterBySemesterAndCurricularYear(curricularSemesterToWrite.getSemester(), curricularSemesterToWrite.getCurricularYear());

		// If CurricularSemester is not in database, then write it.
		if (curricularSemesterFromDB == null) {
			super.lockWrite(curricularSemesterToWrite);
		// else If the CurricularSemester is mapped to the database, then write any existing changes.
		} else if ((curricularSemesterToWrite instanceof CurricularSemester) && ((CurricularSemester) curricularSemesterFromDB).getInternalID().equals(((CurricularSemester) curricularSemesterToWrite).getInternalID())) {
			super.lockWrite(curricularSemesterToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularSemester curricularSemester) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularSemester);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester, ICurricularYear curricularYear) throws ExcepcaoPersistencia {

		try {
			ICurricularSemester curricularSemester = null;
			String oqlQuery = "select all from " + CurricularSemester.class.getName();
			oqlQuery += " where semester = $1";
			oqlQuery += " and curricularYear.year = $2";
			query.create(oqlQuery);
			query.bind(semester);
			query.bind(curricularYear.getYear());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if( (result != null) && (result.size() != 0) ) {
				curricularSemester = (ICurricularSemester) result.get(0);
			}
			return curricularSemester;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularSemester.class.getName();
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
					list.add((ICurricularSemester) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}