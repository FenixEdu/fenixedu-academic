package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegree;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadDegreeService implements IServico {

  private static ReadDegreeService service = new ReadDegreeService();

  /**
   * The singleton access method of this class.
   */
  public static ReadDegreeService getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDegreeService() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDegreeService";
  }

  /**
   * Executes the service. Returns the current collection of infodegrees .
   */
  public InfoDegree run(Integer idInternal) throws FenixServiceException {
	ISuportePersistente sp;
	InfoDegree infoDegree = null;
	ICurso degree = null;
	
	try {
		sp = SuportePersistenteOJB.getInstance();
		degree = sp.getICursoPersistente().readByIdInternal(idInternal);
//		d=sp.getICursoPersistente().readByIdInternal(degree.getIdInternal());
//		System.out.println("DEGREE DO SERVICO22222222"+d);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}
//	while (iterator.hasNext())
//		result.add(
      infoDegree = Cloner.copyIDegree2InfoDegree(degree);
      
	return infoDegree;
  }
}