/*
 * Created on 16/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByCurricularCourse implements IServico {

  private static ReadExecutionCoursesByCurricularCourse service = new ReadExecutionCoursesByCurricularCourse();

  /**
   * The singleton access method of this class.
   */
  public static ReadExecutionCoursesByCurricularCourse getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadExecutionCoursesByCurricularCourse() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadExecutionCoursesByCurricularCourse";
  }

  /**
   * Executes the service. Returns the current collection of infoExecutionCourses.
   */
  public List run(Integer curricularCourseId) throws FenixServiceException {
	ISuportePersistente sp;
	List allExecutionCourses = null;
	try {
			sp = SuportePersistenteOJB.getInstance();
		ICurricularCourse curricularCourseToRead= new CurricularCourse();
					curricularCourseToRead.setIdInternal(curricularCourseId);
			ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOId(curricularCourseToRead, false);	
		    allExecutionCourses = curricularCourse.getAssociatedExecutionCourses();
//		 
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allExecutionCourses == null || allExecutionCourses.isEmpty()) 
		return allExecutionCourses;

	// build the result of this service
	Iterator iterator = allExecutionCourses.iterator();
	List result = new ArrayList(allExecutionCourses.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIExecutionCourse2InfoExecutionCourse((IDisciplinaExecucao) iterator.next()));

	return result;
  }
}