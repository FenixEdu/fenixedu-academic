package ServidorAplicacao.Servico.commons.degree;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * modified by:
 * @author  <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author  <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 */
public class ReadNumerusClausus implements IService {

	public Integer run(Integer degreeCurricularPlanID)
			throws NonExistingServiceException {

		IDegreeCurricularPlan degreeCurricularPlan = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = sp
					.getIPersistentDegreeCurricularPlan();
			degreeCurricularPlan = (IDegreeCurricularPlan) degreeCurricularPlanDAO
					.readByOID(DegreeCurricularPlan.class,
							degreeCurricularPlanID);

		} catch (ExcepcaoPersistencia ex) {
			throw new RuntimeException(ex);
		}

		if (degreeCurricularPlan == null) {
			throw new NonExistingServiceException();
		}

		return degreeCurricularPlan.getNumerusClausus();
	}

}