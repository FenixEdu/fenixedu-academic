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

public class ReadDegree implements IServico {

  private static ReadDegree service = new ReadDegree();

  /**
   * The singleton access method of this class.
   */
  public static ReadDegree getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDegree() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDegree";
  }

  /**
   * Executes the service. Returns the current infodegree.
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
   
       
	if (degree == null) {
		throw new NonExistingServiceException();
	}

	infoDegree = Cloner.copyIDegree2InfoDegree(degree); 
	return infoDegree;
  }
}