package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularCourseScope;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class CurricularCourseScopeOJB extends ObjectFenixOJB implements IPersistentCurricularCourseScope {

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularCourseScope.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(ICurricularCourseScope curricularCourseScopeToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularCourseScope curricularCourseScopeFromDB = null;

		// If there is nothing to write, simply return.
		if (curricularCourseScopeToWrite == null) {
			return;
		}

		// Read CurricularCourseScope from database.
		curricularCourseScopeFromDB =
			this.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
				curricularCourseScopeToWrite.getCurricularCourse(),
				curricularCourseScopeToWrite.getCurricularSemester(),
				curricularCourseScopeToWrite.getBranch());

		// If CurricularCourseScope is not in database, then write it.
		if (curricularCourseScopeFromDB == null) {
			super.lockWrite(curricularCourseScopeToWrite);
			// else If the CurricularCourseScope is mapped to the database, then write any existing changes.
		} else if (
			(curricularCourseScopeToWrite instanceof CurricularCourseScope)
				&& ((CurricularCourseScope) curricularCourseScopeFromDB).getIdInternal().equals(
					((CurricularCourseScope) curricularCourseScopeToWrite).getIdInternal())) {
			super.lockWrite(curricularCourseScopeToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularCourseScope enrolment) throws ExcepcaoPersistencia {
		try {
			super.delete(enrolment);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
		ICurricularCourse curricularCourse,
		ICurricularSemester curricularSemester,//ainda JA TA BD ,isso eh o que eu vou ver
		IBranch branch//este JA ta na BD
		)
		throws ExcepcaoPersistencia {
//TODO:FAZER ISTO COM CRITERIA

		try {
			ICurricularCourseScope curricularCourseScope = null;
			
			String oqlQuery = "select all from " + CurricularCourseScope.class.getName();
			oqlQuery += " where curricularCourseKey= $1";
			oqlQuery += " and curricularSemesterKey = $2";
			oqlQuery += " and branchKey = $2";
			query.create(oqlQuery);

			
			query.bind(curricularCourse.getIdInternal());
			query.bind(curricularSemester.getInternalID());
			query.bind(branch.getIdInternal());
			
			System.out.println("OJB"+curricularCourse.getIdInternal()+"OJB"+curricularSemester.getInternalID()+"OJB"+branch.getIdInternal());
			
			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
			
			if ((result != null) && (result.size() != 0)) {
				curricularCourseScope = (ICurricularCourseScope) result.get(0);
			}
			System.out.println("OJB11111111111111111curricularCourseScope"+curricularCourseScope);
			return curricularCourseScope;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
//	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
//			ICurricularCourse curricularCourse,
//			ICurricularSemester curricularSemester,//ainda nao ta ana BD ,isso eh o que eu vou ver
//			IBranch branch//este ainda nao ta na BD
//			)
//			throws ExcepcaoPersistencia {
//
//
//			try {
//				ICurricularCourseScope curricularCourseScope = null;
//			
//				String oqlQuery = "select all from " + Branch.class.getName();
//				oqlQuery += " where code= $1";
//				oqlQuery += " and keyDegreeCurricularPlan = $2";
//				query.create(oqlQuery);
//
//				query.bind(branch.getCode());
//				query.bind(curricularCourse.getDegreeCurricularPlan().getIdInternal());
//
//			
//				List branchResult = (List) query.execute();
//				try {
//					lockRead(branchResult);
//				} catch (ExcepcaoPersistencia ex) {
//					throw ex;
//				}
//			
//				//so leio pelo semester a chave do year na pratica nao e preciso
//
//				if ((branchResult != null) && (branchResult.size() != 0)) {
//					branch = (IBranch) branchResult.get(0);
//					String oqlQuery1 = "select all from " + CurricularSemester.class.getName();
//					oqlQuery1 += " where curricularSemester.semester= $1";
//					query.create(oqlQuery1);
//
//					query.bind(curricularSemester.getSemester());
//			
//					List semesterResult = (List) query.execute();
//					try {
//						lockRead(semesterResult);
//					} catch (ExcepcaoPersistencia ex) {
//						throw ex;
//					}
//
//					if ((semesterResult != null) && (semesterResult.size() != 0)) {
//						curricularSemester = (ICurricularSemester) semesterResult.get(0);
//						
//						String oqlQuery2 = "select all from " + CurricularCourseScope.class.getName();
//						oqlQuery2 += " where curricularCourseScope.curricularCourseKey= $1";
//						oqlQuery2 += " where curricularCourseScope.curricularSemesterKey= $2";
//						oqlQuery2 += " where curricularCourseScope.branchKey= $3";
//						query.create(oqlQuery2);
//		
//						query.bind(curricularCourseScope.getCurricularCourse().getIdInternal());
//						query.bind(curricularCourseScope.getCurricularSemester().getInternalID());
//						query.bind(curricularCourseScope.getBranch().getIdInternal());
//								
//						List result = (List) query.execute();
//						try {
//							lockRead(result);
//						} catch (ExcepcaoPersistencia ex) {
//							throw ex;
//						}
//			
//						if ((result != null) && (result.size() != 0)) {
//							curricularCourseScope = (ICurricularCourseScope) result.get(0);
//						}
//
//			
//					}
//				
//				}
//
//				return curricularCourseScope;
//
//			} catch (QueryException ex) {
//				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//			}
//		}
	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourseScope.class.getName();
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

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentCurricularCourseScope#readCurricularCourseScopeByCurricularCourse(Dominio.ICurricularCourse)
	 */
	public List readCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		try {
				String oqlQuery = "select all from " + CurricularCourseScope.class.getName();
				oqlQuery += " where curricularCourse.name = $1";
				oqlQuery += " and curricularCourse.code = $2";
				oqlQuery += " and curricularCourse.degreeCurricularPlan.name = $3";
				oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.nome = $4";
				oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.sigla = $5";
				oqlQuery += " and curricularSemester.semester = $6";

				query.create(oqlQuery);

				query.bind(curricularCourse.getName());
				query.bind(curricularCourse.getCode());
				query.bind(curricularCourse.getDegreeCurricularPlan().getName());
				query.bind(curricularCourse.getDegreeCurricularPlan().getDegree().getNome());
				query.bind(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
//				FIXME: semester is hard coded, it must be an argument 
				query.bind(new Integer(2));

				List result = (List) query.execute();
				try {
					lockRead(result);
				} catch (ExcepcaoPersistencia ex) {
					throw ex;
				}

				return result;

			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}

	}
}