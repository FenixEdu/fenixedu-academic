/*
 * Created on 12/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IDisciplinaDepartamento;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDepartmentCoursesService implements IServico {

  private static ReadDepartmentCoursesService service = new ReadDepartmentCoursesService();

  /**
   * The singleton access method of this class.
   */
  public static ReadDepartmentCoursesService getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadDepartmentCoursesService() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadDepartmentCoursesService";
  }

  /**
   * Executes the service. Returns the current collection of infoDepartmentCourses.
   */
  public List run() throws FenixServiceException {
	ISuportePersistente sp;
	List allDepartmentCourses = null;

	try {
			sp = SuportePersistenteOJB.getInstance();
			allDepartmentCourses = sp.getIDisciplinaDepartamentoPersistente().lerTodasAsDisciplinasDepartamento();
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allDepartmentCourses == null || allDepartmentCourses.isEmpty()) 
		return allDepartmentCourses;

	// build the result of this service
	Iterator iterator = allDepartmentCourses.iterator();
	List result = new ArrayList(allDepartmentCourses.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIDepartmentCourse2InfoDepartmentCourse((IDisciplinaDepartamento) iterator.next()));

	return result;
  }
}