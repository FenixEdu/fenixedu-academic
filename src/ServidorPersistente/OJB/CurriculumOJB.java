/*
 * CurriculumOJB.java
 *
 * Created on 6 de Janeiro de 2003, 20:44
 */
package ServidorPersistente.OJB;
/**
 *
 * @author João Mota
 */
import java.util.List;

import org.odmg.QueryException;

import Dominio.Curriculum;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.exceptions.ExistingPersistentException;
/**
 *
 * @author  EP 15
 */
public class CurriculumOJB
	extends ObjectFenixOJB
	implements IPersistentCurriculum {
	
	public ICurriculum readCurriculumByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		try {
			ICurriculum curriculumResultado = null;
			String oqlQuery =
				"select curriculum from " + Curriculum.class.getName();
			oqlQuery += " where executionCourse.sigla = $1";
			oqlQuery += " and executionCourse.executionPeriod.name = $2";
			oqlQuery
				+= " and executionCourse.executionPeriod.executionYear.year = $3";
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
			if (!result.isEmpty()) {
				curriculumResultado = (ICurriculum) result.get(0);
			}
			return curriculumResultado;
		} catch (QueryException queryEx) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
		}
	}
	

	public void lockWrite(ICurriculum curriculum)
				throws ExcepcaoPersistencia, ExistingPersistentException {
					ICurriculum curriculumFromDB = null;

				// If there is nothing to write, simply return.
				if (curriculum == null)
					return;

				// Read curriculum from database.
				curriculumFromDB =
					this.readCurriculumByExecutionCourse(curriculum.getExecutionCourse());

				// If curriculum is not in database, then write it.
				if (curriculumFromDB == null)
					super.lockWrite(curriculum);
				
				// else If the curriculum is mapped to the database, then write any existing changes.
				else if (
					(curriculum instanceof Curriculum)
						&& ((Curriculum) curriculumFromDB).getInternalCode().equals(
							((Curriculum) curriculum).getInternalCode())) {
					super.lockWrite(curriculum);
					// else Throw an already existing exception
				} else
					throw new ExistingPersistentException();
				}

		public List readAll()throws ExcepcaoPersistencia {
	
				try {
					String oqlQuery = "select all from " + Curriculum.class.getName();
					query.create(oqlQuery);
					List result = (List) query.execute();
					lockRead(result);
					return result;
				} catch (QueryException ex) {
					throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
				}
			}


	public void delete(ICurriculum curriculum) throws ExcepcaoPersistencia {
		super.delete(curriculum);
	}
	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Curriculum.class.getName();
		super.deleteAll(oqlQuery);
	}
}
