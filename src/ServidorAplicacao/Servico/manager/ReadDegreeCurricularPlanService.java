/*
 * Created on 1/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlanService implements IServico {

  private static ReadDegreeCurricularPlanService service = new ReadDegreeCurricularPlanService();

  /**
   * The singleton access method of this class.
   */
  public static ReadDegreeCurricularPlanService getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDegreeCurricularPlanService() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDegreeCurricularPlanService";
  }

  /**
   * Executes the service. Returns the current InfoDegreeCurricularPlan.
   */
  public InfoDegreeCurricularPlan run(Integer idInternal) throws FenixServiceException {
	IDegreeCurricularPlan degreeCurricularPlan;
	try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOId(new DegreeCurricularPlan(idInternal), false);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}
     
	if (degreeCurricularPlan == null) {
		return null;
	}

	InfoDegreeCurricularPlan infoDegreeCurricularPlan = Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan); 
	return infoDegreeCurricularPlan;
  }
}