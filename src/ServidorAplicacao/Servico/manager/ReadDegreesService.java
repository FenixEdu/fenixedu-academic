/*
 * Created on 12/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
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
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadDegreesService implements IServico {

  private static ReadDegreesService service = new ReadDegreesService();

  /**
   * The singleton access method of this class.
   */
  public static ReadDegreesService getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDegreesService() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDegreesService";
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
//SERAH PRECISO?
	if (allDegrees == null || allDegrees.isEmpty()) {return allDegrees;}

	// build the result of this service
	Iterator iterator = allDegrees.iterator();
	List result = new ArrayList(allDegrees.size());
    
	while (iterator.hasNext())
		result.add( Cloner.copyIDegree2InfoDegree((ICurso) iterator.next()) );

	return result;
  }
}