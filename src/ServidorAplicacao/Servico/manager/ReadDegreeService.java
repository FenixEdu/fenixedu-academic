package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegree;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
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
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}
    if(degree != null)
      	infoDegree = Cloner.copyIDegree2InfoDegree(degree);
    else
    	throw new NonExistingServiceException();
    
	return infoDegree;
  }
}