/*
 * Created on 4/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadCurricularCoursesByDegreeCurricularPlan implements IServico {

  private static ReadCurricularCoursesByDegreeCurricularPlan service = new ReadCurricularCoursesByDegreeCurricularPlan();

  /**
   * The singleton access method of this class.
   */
  public static ReadCurricularCoursesByDegreeCurricularPlan getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadCurricularCoursesByDegreeCurricularPlan() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadCurricularCoursesByDegreeCurricularPlan";
  }

  /**
   * Executes the service. Returns the current collection of infoCurricularCourses.
   */
  public List run(Integer idDegreeCurricularPlan) throws FenixServiceException {
	ISuportePersistente sp;
	List allCurricularCourses = null;
	try {
			sp = SuportePersistenteOJB.getInstance();
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class, idDegreeCurricularPlan);
			allCurricularCourses = sp.getIPersistentCurricularCourse().readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allCurricularCourses == null || allCurricularCourses.isEmpty()) 
		return allCurricularCourses;

	// build the result of this service
	Iterator iterator = allCurricularCourses.iterator();
	List result = new ArrayList(allCurricularCourses.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyCurricularCourse2InfoCurricularCourse((ICurricularCourse) iterator.next()));

	return result;
  }
}