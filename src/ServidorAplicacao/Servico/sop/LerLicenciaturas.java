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

import DataBeans.InfoDegree;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


public class LerLicenciaturas implements IServico {

  private static LerLicenciaturas _servico = new LerLicenciaturas();
  /**
   * The singleton access method of this class.
   **/
  public static LerLicenciaturas getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerLicenciaturas() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerLicenciaturas";
  }

  public Object run() {
                        
    ArrayList infoLicenciaturas = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      List licenciaturas = sp.getICursoPersistente().readAll();
      
      Iterator iterator = licenciaturas.iterator();
      infoLicenciaturas = new ArrayList();
      while(iterator.hasNext()) {
      	ICurso elem = (ICurso)iterator.next();
        infoLicenciaturas.add(new InfoDegree(elem.getSigla(), elem.getNome()));
      }
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
    return infoLicenciaturas;
  }

}