/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.IDisciplinaExecucao;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ResponsibleForOJB
	extends ObjectFenixOJB
	implements IPersistentResponsibleFor {

	/**
	 * 
	 */
	public ResponsibleForOJB() {
		super();
	}
	public IResponsibleFor readByTeacherAndExecutionCourse(
			ITeacher teacher,
			IDisciplinaExecucao executionCourse)
			throws ExcepcaoPersistencia {
			try {
				IResponsibleFor responsibleFor = null;
				String oqlQuery =
					"select responsibleFor from "
						+  ResponsibleFor.class.getName()
						+ " where teacher.teacherNumber = $1"
						+ " and executionCourse.sigla = $2"
						+ " and executionCourse.executionPeriod.name = $3"
						+ " and executionCourse.executionPeriod.executionYear.year = $4";

				query.create(oqlQuery);
				query.bind(teacher.getTeacherNumber());
				query.bind(executionCourse.getSigla());
				query.bind(executionCourse.getExecutionPeriod().getName());
				query.bind(
					executionCourse
						.getExecutionPeriod()
						.getExecutionYear()
						.getYear());

				List result = (List) query.execute();
				lockRead(result);
				if (result.size() != 0)
					responsibleFor = (IResponsibleFor) result.get(0);
				return responsibleFor;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}

		public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
			try {

				String oqlQuery =
					"select responsibleFor from "
						+ ResponsibleFor.class.getName()
						+ " where teacher.teacherNumber = $1";

				query.create(oqlQuery);
				query.bind(teacher.getTeacherNumber());

				List result = (List) query.execute();
				lockRead(result);

				return result;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}

		public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
			throws ExcepcaoPersistencia {
			try {
				
				String oqlQuery =
					"select responsibleFor from "
						+ ResponsibleFor.class.getName()
						+ " where executionCourse.sigla = $1"
						+ " and executionCourse.executionPeriod.name = $2"
						+ " and executionCourse.executionPeriod.executionYear.year = $3";

				query.create(oqlQuery);
				query.bind(executionCourse.getSigla());
				query.bind(executionCourse.getExecutionPeriod().getName());
				query.bind(
					executionCourse
						.getExecutionPeriod()
						.getExecutionYear()
						.getYear());

				List result = (List) query.execute();
				lockRead(result);

				return result;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
		}

		public void delete(IResponsibleFor responsibleFor)
			throws ExcepcaoPersistencia {
			super.delete(responsibleFor);
		}

		public void deleteByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
			List result = readByTeacher(teacher);
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
				super.delete(responsibleFor);
			}

		}

		public void deleteByExecutionCourse(IDisciplinaExecucao executionCourse)
			throws ExcepcaoPersistencia {
			List result = readByExecutionCourse(executionCourse);
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
				super.delete(responsibleFor);
			}

		}

		public void deleteAll() throws ExcepcaoPersistencia {
			String oqlQuery = "select all from " + ResponsibleFor.class.getName();
			super.deleteAll(oqlQuery);
		}

		public void lockWrite(IResponsibleFor responsibleFor)
				throws ExcepcaoPersistencia, ExistingPersistentException {
					IResponsibleFor responsibleForFromDB = null;

				// If there is nothing to write, simply return.
				if (responsibleFor == null)
					return;

				// Read responsibleFor from database.
				responsibleForFromDB =
					this.readByTeacherAndExecutionCourse(
					responsibleFor.getTeacher(),
					responsibleFor.getExecutionCourse());

				// If responsibleFor is not in database, then write it.
				if (responsibleForFromDB == null)
					super.lockWrite(responsibleFor);
				
				// else If the responsibleFor is mapped to the database, then write any existing changes.
				else if (
					(responsibleFor instanceof ResponsibleFor)
						&& ((Professorship) responsibleForFromDB).getIdInternal().equals(
							((Professorship) responsibleFor).getIdInternal())) {
					super.lockWrite(responsibleFor);
					// else Throw an already existing exception
				} else
					throw new ExistingPersistentException();
				}

}
