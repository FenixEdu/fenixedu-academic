package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public class CurricularCourseOJB extends ObjectFenixOJB implements IPersistentCurricularCourse {

	public CurricularCourseOJB() {
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularCourse readCurricularCourseByNameAndCode(String name, String code) throws ExcepcaoPersistencia {
		try {
			ICurricularCourse curricularCourse = null;
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where name = $1";
			oqlQuery += " and code = $2";
			query.create(oqlQuery);
			query.bind(name);
			query.bind(code);

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				curricularCourse = (ICurricularCourse) result.get(0);
			}
			return curricularCourse;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ICurricularCourse curricularCourseToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularCourse curricularCourseFromBD = null;

		// If there is nothing to write, simply return.
		if (curricularCourseToWrite == null) {
			return;
		}

		// Read branch from database.
		curricularCourseFromBD = this.readCurricularCourseByNameAndCode(curricularCourseToWrite.getName(), curricularCourseToWrite.getCode());

		// If branch is not in database, then write it.
		if (curricularCourseFromBD == null) {
			super.lockWrite(curricularCourseToWrite);
			// else If the branch is mapped to the database, then write any existing changes.
		} else if (
			(curricularCourseToWrite instanceof CurricularCourse)
				&& ((CurricularCourse) curricularCourseFromBD).getInternalCode().equals(
					((CurricularCourse) curricularCourseToWrite).getInternalCode())) {
			super.lockWrite(curricularCourseToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularCourse);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public ArrayList readCurricularCoursesByCurricularYear(Integer year) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where associatedCurricularSemesters.curricularYear.year = $1";
			query.create(oqlQuery);
			query.bind(year);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public ArrayList readCurricularCoursesByCurricularSemester(Integer semester) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where associatedCurricularSemesters.semester = $1";
			query.create(oqlQuery);
			query.bind(semester);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public ArrayList readCurricularCoursesByCurricularSemesterAndCurricularYear(Integer semester, Integer year) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where associatedCurricularSemesters.semester = $1";
			oqlQuery += " and associatedCurricularSemesters.curricularYear.year = $2";
			query.create(oqlQuery);
			query.bind(semester);
			query.bind(year);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}