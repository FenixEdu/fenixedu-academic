package ServidorAplicacao.Servico.sop;

/**
 * Servico LerSalas
 *
 * @author tfc130
 * @version
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


public class ReadExecutionDegreesByExecutionYear implements IServico {

  private static ReadExecutionDegreesByExecutionYear service = new ReadExecutionDegreesByExecutionYear();
  /**
   * The singleton access method of this class.
   **/
  public static ReadExecutionDegreesByExecutionYear getService() {
    return service;
  }

  /**
   * The actor of this class.
   **/
  private ReadExecutionDegreesByExecutionYear() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadExecutionDegreesByExecutionYear";
  }

  public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException{
                        
    ArrayList infoExecutionDegreeList = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
      
      IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
	  
      List executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear);
      
      Iterator iterator = executionDegrees.iterator();
      infoExecutionDegreeList = new ArrayList();
      
      while(iterator.hasNext()) {
      	ICursoExecucao executionDegree = (ICursoExecucao)iterator.next();
        infoExecutionDegreeList.add(Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree));
      }
      
    } catch (ExcepcaoPersistencia ex) {
      throw new FenixServiceException(ex);
    }
    return infoExecutionDegreeList;
  }

}