/*
 * Created on 8/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByExecutionPeriod implements IServico {

  private static ReadExecutionCoursesByExecutionPeriod service = new ReadExecutionCoursesByExecutionPeriod();

  /**
   * The singleton access method of this class.
   */
  public static ReadExecutionCoursesByExecutionPeriod getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadExecutionCoursesByExecutionPeriod() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadExecutionCoursesByExecutionPeriod";
  }

  /**
   * Executes the service. Returns the current collection of infoExecutionCourses.
   */
  public List run(Integer executionPeriodId) throws FenixServiceException {
	ISuportePersistente sp;
	List allExecutionCoursesFromExecutionPeriod = null;
	try {
			sp = SuportePersistenteOJB.getInstance();
		IExecutionPeriod executionPeriodToRead= new ExecutionPeriod();
		executionPeriodToRead.setIdInternal(executionPeriodId);
		
			IExecutionPeriod executionPeriod = (IExecutionPeriod) sp.getIPersistentExecutionPeriod().readByOId(executionPeriodToRead, false);
			allExecutionCoursesFromExecutionPeriod = 
			(List) sp.getIDisciplinaExecucaoPersistente().readByExecutionPeriod(executionPeriod);
		    
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allExecutionCoursesFromExecutionPeriod == null || allExecutionCoursesFromExecutionPeriod.isEmpty()) 
		return allExecutionCoursesFromExecutionPeriod;

	// build the result of this service
	Iterator iterator = allExecutionCoursesFromExecutionPeriod.iterator();
	List result = new ArrayList(allExecutionCoursesFromExecutionPeriod.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIExecutionCourse2InfoExecutionCourse((IDisciplinaExecucao) iterator.next()));

	return result;
  }
}