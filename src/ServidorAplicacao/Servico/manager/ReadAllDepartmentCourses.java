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

public class ReadAllDepartmentCourses implements IServico {

  private static ReadAllDepartmentCourses service = new ReadAllDepartmentCourses();

  /**
   * The singleton access method of this class.
   */
  public static ReadAllDepartmentCourses getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadAllDepartmentCourses() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadAllDepartmentCourses";
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