/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionDegree implements IServico {

  private static ReadExecutionDegree service = new ReadExecutionDegree();

  /**
   * The singleton access method of this class.
   */
  public static ReadExecutionDegree getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadExecutionDegree() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadExecutionDegree";
  }

  /**
   * Executes the service. Returns the current InfoExecutionDegree.
   */
  public InfoExecutionDegree run(Integer idInternal) throws FenixServiceException {
	ICursoExecucao executionDegree;
	try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			CursoExecucao execDegree = new CursoExecucao();
			execDegree.setIdInternal(idInternal);
			executionDegree = (ICursoExecucao) sp.getICursoExecucaoPersistente().readByOId(execDegree, false);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}
     
	if (executionDegree == null) {
		throw new NonExistingServiceException();
	}

	InfoExecutionDegree infoExecutionDegree = Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree); 
	return infoExecutionDegree;
  }
}