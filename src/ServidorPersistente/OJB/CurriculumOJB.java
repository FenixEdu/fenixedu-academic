/*
 * CurriculumOJB.java
 *
 * Created on 6 de Janeiro de 2003, 20:44
 */
package ServidorPersistente.OJB;
/**
 *
 * @author jmota
 */
import java.util.List;

import org.odmg.QueryException;

import Dominio.Curriculum;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurriculum;
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
			oqlQuery += " where executionCourse.nome = $1";
			oqlQuery += " and executionCourse.executionPeriod.name = $2";
			oqlQuery
				+= " and executionCourse.executionPeriod.executionYear.year = $3";
			query.create(oqlQuery);
			query.bind(executionCourse.getNome());
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
	public void lockWrite(ICurriculum curriculum) throws ExcepcaoPersistencia {
		super.lockWrite(curriculum);
	}
	public void delete(ICurriculum curriculum) throws ExcepcaoPersistencia {
		super.delete(curriculum);
	}
	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Curriculum.class.getName();
		super.deleteAll(oqlQuery);
	}
}
