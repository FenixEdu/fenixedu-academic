/*
 * LerLicenciaturaExecucaoDeLicenciaturaESemestre.java
 *
 * Created on 24 de Novembro de 2002, 15:30
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerLicenciaturaExecucaoDeLicenciaturaESemestre.
 *
 * @author tfc130
 **/
import DataBeans.DegreeKey;
import DataBeans.InfoExecutionDegree;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;

public class LerLicenciaturaExecucaoDeLicenciatura implements IServico {

  private static LerLicenciaturaExecucaoDeLicenciatura _servico = new LerLicenciaturaExecucaoDeLicenciatura();
  /**
   * The singleton access method of this class.
   **/
  public static LerLicenciaturaExecucaoDeLicenciatura getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerLicenciaturaExecucaoDeLicenciatura() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerLicenciaturaExecucaoDeLicenciatura";
  }

  public Object run(DegreeKey keyLicenciatura) {

    ICursoExecucao licenciaturaExecucao = null;
    InfoExecutionDegree infoLicenciaturaExecucao = null;
//
//    try {
//      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//      licenciaturaExecucao = sp.getICursoExecucaoPersistente().readBySigla(keyLicenciatura.getSigla());
//	  if (licenciaturaExecucao != null) {
//	  	InfoDegree infoLicenciatura = new InfoDegree(licenciaturaExecucao.getCurso().getSigla(),
//	  	                                                         licenciaturaExecucao.getCurso().getNome());
//	  	infoLicenciaturaExecucao = new InfoExecutionDegree(licenciaturaExecucao.getAnoLectivo(),
//	                                                            infoLicenciatura);
//      }
//
//    } catch (ExcepcaoPersistencia ex) {
//      ex.printStackTrace();
//    }
    return infoLicenciaturaExecucao;
  }

}