/*
 * Created on 31/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlansService implements IServico {

	private static DeleteDegreeCurricularPlansService service = new DeleteDegreeCurricularPlansService();

	public static DeleteDegreeCurricularPlansService getService() {
		return service;
	}

	private DeleteDegreeCurricularPlansService() {
	}

	public final String getNome() {
		return "DeleteDegreeCurricularPlansService";
	}
	
// delete a set of degreeCurricularPlans
	public List run(List degreeCurricularPlansIds) throws FenixServiceException {

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();

			Iterator iter = degreeCurricularPlansIds.iterator();

			Boolean result = new Boolean(true);
			List undeletedDegreeCurricularPlansNames = new ArrayList();
			Integer degreeCurricularPlanId;
			IDegreeCurricularPlan degreeCurricularPlan;

			while (iter.hasNext()) {

				degreeCurricularPlanId = (Integer) iter.next();
				degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
				if (degreeCurricularPlan != null)
					result = persistentDegreeCurricularPlan.deleteDegreeCurricularPlan(degreeCurricularPlan);			
				if(result.equals(new Boolean(false)))				
					undeletedDegreeCurricularPlansNames.add((String) degreeCurricularPlan.getName());	
			}
			
			return undeletedDegreeCurricularPlansNames;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}
