/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 * @author Francisco Paulo 27/Out/04 frnp@mega.ist.utl.pt (edit)
 * 
 * ReadCoordinatedDegrees class, implements the service that given a teacher returns 
 * a list containing the degree curricular plans for that teacher.
 */

package ServidorAplicacao.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlanWithDegree;
import Dominio.IDegreeCurricularPlan;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadCoordinatedDegrees implements IService {

	public ReadCoordinatedDegrees() {
	}

	public List run(UserView userView) throws ExcepcaoInexistente,
			FenixServiceException {

		ISuportePersistente sp = null;

		List plans = null;

		try {
			sp = SuportePersistenteOJB.getInstance();

			// Read the Teacher

			ITeacher teacher = sp.getIPersistentTeacher()
					.readTeacherByUsername(userView.getUtilizador());
			if (teacher == null) {
				throw new ExcepcaoInexistente("No Teachers Found !!");
			}
			plans = sp.getIPersistentCoordinator()
					.readCurricularPlansByTeacher(teacher);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException(
					"Persistence layer error", ex);
			throw newEx;
		}
		if (plans == null) {
			throw new ExcepcaoInexistente("No Degrees Found !!");
		}
		Iterator iterator = plans.iterator();
		List result = new ArrayList();
		while (iterator.hasNext()) {
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator
					.next();
			result.add(InfoDegreeCurricularPlanWithDegree
					.newInfoFromDomain(degreeCurricularPlan));
		}

		return result;
	}
}