package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterAllOptionalDegreesRule implements IEnrolmentRule {

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