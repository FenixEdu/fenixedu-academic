/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadStudentCurricularPlansByCurricularCourse implements IServico {

  private static ReadStudentCurricularPlansByCurricularCourse service = new ReadStudentCurricularPlansByCurricularCourse();

  /**
   * The singleton access method of this class.
   */
  public static ReadStudentCurricularPlansByCurricularCourse getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadStudentCurricularPlansByCurricularCourse() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadStudentCurricularPlansByCurricularCourse";
  }

  /**
   * Executes the service. Returns the current collection of infoStudentCurricularPlans.
   */
  public List run(Integer curricularCourseId) throws FenixServiceException {
	ISuportePersistente sp;
	List allStudentCurricularPlans = new ArrayList();
	try {
			sp = SuportePersistenteOJB.getInstance();
			List enrolmentsList = sp.getIStudentCurricularPlanPersistente().readEnrolmentByCurricularCourse(curricularCourseId);
			
			Iterator iter = enrolmentsList.iterator();
			IStudentCurricularPlan studentCurricularPlan;
			
			while(iter.hasNext()){			
				studentCurricularPlan = ((IEnrolment) iter.next()).getStudentCurricularPlan();
				allStudentCurricularPlans.add(studentCurricularPlan);
			}
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allStudentCurricularPlans == null || allStudentCurricularPlans.isEmpty()) 
		return allStudentCurricularPlans;

	// build the result of this service
	Iterator iterator = allStudentCurricularPlans.iterator();
	List result = new ArrayList(allStudentCurricularPlans.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan((IStudentCurricularPlan) iterator.next()));

	return result;
  }
}