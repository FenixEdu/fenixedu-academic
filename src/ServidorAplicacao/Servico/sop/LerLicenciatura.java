/*
 * LerLicenciatura.java
 *
 * Created on 25 de Novembro de 2002, 1:21
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerLicenciatura.
 *
 * @author tfc130
 **/
import DataBeans.InfoDegree;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerLicenciatura implements IServico {

  private static LerLicenciatura _servico = new LerLicenciatura();
  /**
   * The singleton access method of this class.
   **/
  public static LerLicenciatura getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerLicenciatura() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerLicenciatura";
  }

  public Object run(String sigla) {

    ICurso licenciatura = null;
    InfoDegree infoLicenciatura = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      licenciatura = sp.getICursoPersistente().readBySigla(sigla);

	  if (licenciatura != null)
	  	infoLicenciatura = new InfoDegree(licenciatura.getSigla(), licenciatura.getNome());
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return infoLicenciatura;
  }

}