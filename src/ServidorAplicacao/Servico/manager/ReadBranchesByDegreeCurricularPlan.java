/*
 * Created on 22/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
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

public class ReadBranchesByDegreeCurricularPlan implements IServico {

  private static ReadBranchesByDegreeCurricularPlan service = new ReadBranchesByDegreeCurricularPlan();

  /**
   * The singleton access method of this class.
   */
  public static ReadBranchesByDegreeCurricularPlan getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadBranchesByDegreeCurricularPlan() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadBranchesByDegreeCurricularPlan";
  }

  /**
   * Executes the service. Returns the current collection of infoBranches.
   */
  public List run(Integer idDegreeCurricularPlan) throws FenixServiceException {
	ISuportePersistente sp;
	List allBranches = null;
	try {
			sp = SuportePersistenteOJB.getInstance();
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class, idDegreeCurricularPlan);
			if(degreeCurricularPlan == null)
				throw new NonExistingServiceException();
			allBranches = sp.getIPersistentBranch().readByDegreeCurricularPlan(degreeCurricularPlan);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if(allBranches == null || allBranches.isEmpty()) 
		return null;

	// build the result of this service
	Iterator iterator = allBranches.iterator();
	List result = new ArrayList(allBranches.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIBranch2InfoBranch((IBranch) iterator.next()));

	return result;
  }
}