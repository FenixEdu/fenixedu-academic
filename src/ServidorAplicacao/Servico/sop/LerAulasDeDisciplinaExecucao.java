/*
 * LerAulasDeDisciplinaExecucao.java
 *
 * Created on 27 de Outubro de 2002, 23:09
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucao.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeDisciplinaExecucao implements IServico {

  private static LerAulasDeDisciplinaExecucao _servico = new LerAulasDeDisciplinaExecucao();
  /**
   * The singleton access method of this class.
   **/
  public static LerAulasDeDisciplinaExecucao getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerAulasDeDisciplinaExecucao() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerAulasDeDisciplinaExecucao";
  }

  public Object run(InfoExecutionCourse infoDisciplinaExecucao) {

    ArrayList infoAulas = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      IDisciplinaExecucao iDE= sp.getIDisciplinaExecucaoPersistente().readBySiglaAndAnoLectivAndSiglaLicenciatura(infoDisciplinaExecucao.getSigla(),
                                                                                                                  infoDisciplinaExecucao.getInfoLicenciaturaExecucao().getAnoLectivo(),
                                                                                                                  infoDisciplinaExecucao.getInfoLicenciaturaExecucao().getInfoLicenciatura().getSigla());
      List aulas = sp.getIAulaPersistente().readByDisciplinaExecucao(iDE);

      Iterator iterator = aulas.iterator();
      infoAulas = new ArrayList();
      while(iterator.hasNext()) {
      	IAula elem = (IAula)iterator.next();
      	InfoRoom infoSala = new InfoRoom(elem.getSala().getNome(), elem.getSala().getEdificio(),
      	                                 elem.getSala().getPiso(), elem.getSala().getTipo(),
      	                                 elem.getSala().getCapacidadeNormal(),
      	                                 elem.getSala().getCapacidadeExame());
        infoAulas.add(new InfoLesson(elem.getDiaSemana(), elem.getInicio(), elem.getFim(),
                                   elem.getTipo(), infoSala, infoDisciplinaExecucao));
        }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    
    return infoAulas;
  }

}