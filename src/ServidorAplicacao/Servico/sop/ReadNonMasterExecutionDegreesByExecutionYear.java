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
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;


public class ReadNonMasterExecutionDegreesByExecutionYear implements IServico {

  private static ReadNonMasterExecutionDegreesByExecutionYear service = new ReadNonMasterExecutionDegreesByExecutionYear();
  /**
   * The singleton access method of this class.
   **/
  public static ReadNonMasterExecutionDegreesByExecutionYear getService() {
    return service;
  }

  /**
   * The actor of this class.
   **/
  private ReadNonMasterExecutionDegreesByExecutionYear() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadNonMasterExecutionDegreesByExecutionYear";
  }

  public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException{
                        
    ArrayList infoExecutionDegreeList = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
      
      IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
	  
      List executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear, new TipoCurso(1));
      
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