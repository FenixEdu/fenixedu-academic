/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionCourse;
import Dominio.ISummary;
import Dominio.Summary;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSummary;
import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernades
 *
 * 21/Jul/2003
 * fenix-head
 * ServidorPersistente.OJB
 * 
 */
public class SummaryOJB extends ObjectFenixOJB implements IPersistentSummary {

	/**
	 * 
	 */
	public SummaryOJB() {
	}

	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyExecutionCourse",
			executionCourse.getIdInternal());
		return queryList(Summary.class, criteria);

	}

	public List readByExecutionCourseAndType(
		IExecutionCourse executionCourse,
		TipoAula summaryType)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyExecutionCourse",
			executionCourse.getIdInternal());
		criteria.addEqualTo("summaryType", summaryType.getTipo());
		return queryList(Summary.class, criteria);

	}

	public void delete (ISummary summary) throws ExcepcaoPersistencia{
		super.delete(summary);
	}
}
