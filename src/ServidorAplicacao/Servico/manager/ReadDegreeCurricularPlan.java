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
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlan implements IServico {

  private static ReadDegreeCurricularPlan service = new ReadDegreeCurricularPlan();

  /**
   * The singleton access method of this class.
   */
  public static ReadDegreeCurricularPlan getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDegreeCurricularPlan() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDegreeCurricularPlan";
  }

  /**
   * Executes the service. Returns the current InfoDegreeCurricularPlan.
   */
  public InfoDegreeCurricularPlan run(Integer idInternal) throws FenixServiceException {
	IDegreeCurricularPlan degreeCurricularPlan;
	try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class, idInternal);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}
     
	if (degreeCurricularPlan == null) {
		throw new NonExistingServiceException();
	}

	InfoDegreeCurricularPlan infoDegreeCurricularPlan = Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan); 
	return infoDegreeCurricularPlan;
  }
}