package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

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

	/**
	 * @deprecated
	 */
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

	public ICurricularCourse readCurricularCourseByDegreeCurricularPlanAndNameAndCode(Integer degreeCurricularPlanId, String name, String code) throws ExcepcaoPersistencia {
		try {
			ICurricularCourse curricularCourse = null;
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where name = $1";
			oqlQuery += " and code = $2";
			oqlQuery += " and degreeCurricularPlanKey = $3";
			query.create(oqlQuery);
			query.bind(name);
			query.bind(code);
			query.bind(degreeCurricularPlanId);

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
		curricularCourseFromBD =
			this.readCurricularCourseByDegreeCurricularPlanAndNameAndCode(
				curricularCourseToWrite.getDegreeCurricularPlan().getIdInternal(),
				curricularCourseToWrite.getName(),
				curricularCourseToWrite.getCode());
		// If branch is not in database, then write it.
		if (curricularCourseFromBD == null) {
			super.lockWrite(curricularCourseToWrite);
			// else If the branch is mapped to the database, then write any existing changes.
		} else if (
			(curricularCourseToWrite instanceof CurricularCourse)
				&& ((CurricularCourse) curricularCourseFromBD).getIdInternal().equals(((CurricularCourse) curricularCourseToWrite).getIdInternal())) {
			super.lockWrite(curricularCourseToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		
		super.delete(curricularCourse);
		
	}

	public List readAll() throws ExcepcaoPersistencia {

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
					list.add(iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readCurricularCoursesByCurricularYear(Integer year) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.curricularYear.year = $1";
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
					list.add(iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	//	public ArrayList readCurricularCoursesByCurricularSemesterAndCurricularYear(Integer semester, Integer year) throws ExcepcaoPersistencia {
	//		try {
	//			ArrayList list = new ArrayList();
	//			String oqlQuery = "select all from " + CurricularCourse.class.getName();
	//			oqlQuery += " where associatedCurricularSemesters.semester = $1";
	//			oqlQuery += " and associatedCurricularSemesters.curricularYear.year = $2";
	//			query.create(oqlQuery);
	//			query.bind(semester);
	//			query.bind(year);
	//			List result = (List) query.execute();
	//
	//			try {
	//				lockRead(result);
	//			} catch (ExcepcaoPersistencia ex) {
	//				throw ex;
	//			}
	//
	//			if ((result != null) && (result.size() != 0)) {
	//				ListIterator iterator = result.listIterator();
	//				while (iterator.hasNext())
	//					list.add((ICurricularCourse) iterator.next());
	//			}
	//			return list;
	//
	//		} catch (QueryException ex) {
	//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
	//		}
	//	}

	public List readCurricularCoursesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where degreeCurricularPlan.name = $1";
			oqlQuery += " and degreeCurricularPlan.degree.nome = $2";
			oqlQuery += " and degreeCurricularPlan.degree.sigla = $3";
			query.create(oqlQuery);

			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getNome());
			query.bind(degreeCurricularPlan.getDegree().getSigla());

			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add(iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(IDegreeCurricularPlan degreeCurricularPlan, Boolean basic) throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
		criteria.addEqualTo("basic", basic);
		return queryList(CurricularCourse.class, criteria);
	}

	public List readAllCurricularCoursesByBranch(IBranch branch) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.branch.name = $1";
			oqlQuery += " and scopes.branch.code = $2";

			query.create(oqlQuery);

			query.bind(branch.getName());
			query.bind(branch.getCode());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			ICurricularCourse curricularCourse = null;

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext()) {
					curricularCourse = (ICurricularCourse) iterator.next();
					list.add(curricularCourse);
				}
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAllCurricularCoursesBySemester(Integer semester /*, IStudentCurricularPlan studentCurricularPlan*/
	) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.semester = $1";
			//			oqlQuery += " and curricularCourse.degreeCurricularPlan.name = $2";
			//			oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.name = $3";

			query.create(oqlQuery);

			query.bind(semester);
			//			query.bind(studentCurricularPlan.getDegreeCurricularPlan().getName());
			//			query.bind(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			ICurricularCourse curricularCourse = null;

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext()) {
					curricularCourse = (ICurricularCourse) iterator.next();
					list.add(curricularCourse);
				}
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentCurricularCourse#readCurricularCoursesBySemesterAndYear(java.lang.Integer, java.lang.Integer)
	 */
	public List readCurricularCoursesBySemesterAndYear(Integer semester, Integer year) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.semester = $1";
			oqlQuery += " and scopes.curricularSemester.curricularYear.year = $2";

			query.create(oqlQuery);

			query.bind(semester);
			query.bind(year);

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			ICurricularCourse curricularCourse = null;

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext()) {
					curricularCourse = (ICurricularCourse) iterator.next();
					list.add(curricularCourse);
				}
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentCurricularCourse#readCurricularCoursesBySemesterAndYearAnBranch(java.lang.Integer, java.lang.Integer, Dominio.IBranch)
	 */
	public List readCurricularCoursesBySemesterAndYearAndBranch(Integer semester, Integer year, IBranch branch) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.semester = $1";
			oqlQuery += " and scopes.curricularSemester.curricularYear.year = $2";
			oqlQuery += " and scopes.branch.name = $3";
			oqlQuery += " and scopes.branch.code = $4";

			query.create(oqlQuery);

			query.bind(semester);
			query.bind(year);
			query.bind(branch.getName());
			query.bind(branch.getCode());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			ICurricularCourse curricularCourse = null;

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext()) {
					curricularCourse = (ICurricularCourse) iterator.next();
					list.add(curricularCourse);
				}
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentCurricularCourse#readCurricularCoursesBySemesterAndYearAndBranchAndNoBranch(java.lang.Integer, java.lang.Integer, Dominio.IBranch)
	 */
	public List readCurricularCoursesBySemesterAndYearAndBranchAndNoBranch(Integer semester, Integer year, IBranch branch) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.semester = $1";
			oqlQuery += " and scopes.curricularSemester.curricularYear.year = $2";
			oqlQuery += " and ((scopes.branch.name = $3";
			oqlQuery += " and scopes.branch.code = $4)";
			oqlQuery += " or (scopes.branch.name = $5";
			oqlQuery += " and scopes.branch.name = $6))";

			query.create(oqlQuery);

			query.bind(semester);
			query.bind(year);
			query.bind(branch.getName());
			query.bind(branch.getCode());
			query.bind("");
			query.bind("");

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			ICurricularCourse curricularCourse = null;

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext()) {
					curricularCourse = (ICurricularCourse) iterator.next();
					list.add(curricularCourse);
				}
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readbyCourseCodeAndDegreeCurricularPlan(String curricularCourseCode, IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
		
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", curricularCourseCode);
		criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());

		return queryList(CurricularCourse.class, criteria);
	}

	public List readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(String courseCode, TipoCurso degreeType, DegreeCurricularPlanState degreeCurricularPlanState) throws ExcepcaoPersistencia {
		
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", courseCode);
		criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
		criteria.addEqualTo("degreeCurricularPlan.state", degreeCurricularPlanState);

		return queryList(CurricularCourse.class, criteria);
	}

	public List readbyCourseNameAndDegreeCurricularPlan(String curricularCourseName, IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
		
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", curricularCourseName);
		criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());

		return queryList(CurricularCourse.class, criteria);
	}
	
	public List readByScientificArea(IScientificArea scientificArea) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyScientificArea",scientificArea.getIdInternal());
		
		return queryList(CurricularCourse.class, criteria);
	}
}