package ServidorAplicacao.strategy.enrolment.rules.depercated;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to obtain ALL the degrees,
 * so that one of them can be chosen, with the purpose of obtaining ALL it's curricular courses
 * that can be enrolled as an optional course.
 */

public class EnrolmentFilterAllOptionalDegreesRule //implements IEnrolmentRule
{

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		List degreesList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoPersistente cursoPersistente = sp.getICursoPersistente();
			degreesList = cursoPersistente.readAll();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new IllegalStateException("Cannot read from data base");
		}
		enrolmentContext.setDegreesForOptionalCurricularCourses(degreesList);
		return enrolmentContext;
	}
}