/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansService implements IServico {

  private static ReadDegreeCurricularPlansService service = new ReadDegreeCurricularPlansService();

  /**
   * The singleton access method of this class.
   */
  public static ReadDegreeCurricularPlansService getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDegreeCurricularPlansService() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDegreeCurricularPlansService";
  }

  /**
   * Executes the service. Returns the current collection of infoDegreeCurricularPlans.
   */
  public List run() throws FenixServiceException {
	ISuportePersistente sp;
	List allDegreeCurricularPlans = null;
	try {
			sp = SuportePersistenteOJB.getInstance();
			allDegreeCurricularPlans = sp.getIPersistentDegreeCurricularPlan().readAll();
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allDegreeCurricularPlans == null || allDegreeCurricularPlans.isEmpty()) 
		return allDegreeCurricularPlans;

	// build the result of this service
	Iterator iterator = allDegreeCurricularPlans.iterator();
	List result = new ArrayList(allDegreeCurricularPlans.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan((IDegreeCurricularPlan) iterator.next()));

	return result;
  }
}