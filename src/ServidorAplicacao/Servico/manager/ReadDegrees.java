package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class ReadDegrees implements IServico {

  private static ReadDegrees service = new ReadDegrees();

  /**
   * The singleton access method of this class.
   */
  public static ReadDegrees getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDegrees() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDegrees";
  }

  /**
   * Executes the service. Returns the current collection of infodegrees .
   */
  public List run() throws FenixServiceException {
	ISuportePersistente sp;
	List allDegrees = null;

	try {
			sp = SuportePersistenteOJB.getInstance();
			allDegrees = sp.getICursoPersistente().readAll();
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allDegrees == null || allDegrees.isEmpty()) 
		return allDegrees;

	// build the result of this service
	Iterator iterator = allDegrees.iterator();
	List result = new ArrayList(allDegrees.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIDegree2InfoDegree((ICurso) iterator.next()));

	return result;
  }
}