package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
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

	public void delete(ICurricularCourseScope scope) throws ExcepcaoPersistencia {
		try {
					Criteria crit = new Criteria();
					crit.addEqualTo("curricularCourseScopeKey", scope.getIdInternal());
					List result = queryList(Enrolment.class, crit);
					
					Iterator iter = result.iterator();
					EnrolmentOJB enrolmentOJB = new EnrolmentOJB(); 
					while(iter.hasNext()){
						enrolmentOJB.delete((Enrolment) iter.next());
					}
			
			super.delete(scope);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
			ICurricularCourse curricularCourse,
			ICurricularSemester curricularSemester,
			IBranch branch
			)
			throws ExcepcaoPersistencia {
 
				Criteria criteria = new Criteria();
				criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
				criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
				criteria.addEqualTo("branchKey", branch.getIdInternal());
				return (ICurricularCourseScope)queryObject(CurricularCourseScope.class, criteria);

		}

	
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
		Criteria crit = new Criteria();
		crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
		List result = queryList(CurricularCourseScope.class, crit);
		return result;
	}
}