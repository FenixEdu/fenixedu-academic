package ServidorAplicacao.Servico.sop;

/**
 * Serviço LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular
 *
 * @author tfc130
 * @version
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular implements IServico {

  private static LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular _servico = new LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular();
  /**
   * The singleton access method of this class.
   **/
  public static LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular";
  }

  public Object run(CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE) {
                        
    List listDCDE = null;
    List listInfoDE = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//      listDCDE = sp.getIDisciplinaCurricularDisciplinaExecucaoPersistente().readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(aCSiLE.getAnoCurricular(),
//                                                                                                                                 aCSiLE.getInfoLicenciaturaExecucao().getAnoLectivo(),
//                                                                                                                                 aCSiLE.getSemestre(),
//                                                                                                                                 aCSiLE.getInfoLicenciaturaExecucao().getInfoLicenciatura().getSigla());

	  listDCDE = sp.getIDisciplinaExecucaoPersistente().readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(aCSiLE.getAnoCurricular(),
                                                                                                                                 aCSiLE.getInfoLicenciaturaExecucao().getAnoLectivo(),
                                                                                                                                 aCSiLE.getSemestre(),
                                                                                                                                 aCSiLE.getInfoLicenciaturaExecucao().getInfoLicenciatura().getSigla());

      Iterator iterator = listDCDE.iterator();
      listInfoDE = new ArrayList();
      while(iterator.hasNext()) {
      	IDisciplinaExecucao elem = (IDisciplinaExecucao)iterator.next();
      	InfoDegree infoLicenciatura = new InfoDegree(elem.getLicenciaturaExecucao().getCurso().getSigla(),
      	                                                         elem.getLicenciaturaExecucao().getCurso().getNome());
      	InfoExecutionDegree infoLicenciaturaExecucao = new InfoExecutionDegree(elem.getLicenciaturaExecucao().getAnoLectivo(),
      	                                                                                 infoLicenciatura);
        listInfoDE.add(new InfoExecutionCourse(elem.getNome(), elem.getSigla(),elem.getPrograma(),
        										infoLicenciaturaExecucao, elem.getTheoreticalHours(),
        										elem.getPraticalHours(),elem.getTheoPratHours(),
        										elem.getLabHours()));
        }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return listInfoDE;
  }
  
}