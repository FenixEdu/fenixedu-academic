/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
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
			ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOId(new CurricularCourse(curricularCourseId), false);
			IPersistentEnrolment persistentEnrolment = (IPersistentEnrolment) sp.getIPersistentEnrolment();
			
//TODO redefinir este read
//			
////read curricularCourseScopesIds
//			List curricularCourseScopeList = (List) sp.getIPersistentCurricularCourseScope().readCurricularCourseScopesByCurricularCourse(curricularCourse);
//			Iterator scopes = curricularCourseScopeList.iterator();
//			
//			List executionCourseList = curricularCourse.getAssociatedExecutionCourses(); 
//			Iterator iter1 = executionCourseList.iterator();
//			
//			List enrolmentsList = new ArrayList();
//			
////		read executionPeriodsIds
//			while(scopes.hasNext()){
//				Integer curricularCourseScopeId = ((ICurricularCourseScope) scopes.next()).getIdInternal();
//
//				
//				while(iter1.hasNext()){
//					Integer executionPeriodId = ((IDisciplinaExecucao) iter1.next()).getExecutionPeriod().getIdInternal();
//			
//					List enrolments = sp.getIStudentCurricularPlanPersistente().readEnrolmentsByCurricularCourseScopeAndExecutionPeriod(curricularCourseScopeId, executionPeriodId );
//					enrolmentsList.add(enrolments);
//									}
//			
//			
//			}
//			
////	agora tenho os enrolments
//			
//			
//			
//			
			
	/*****************************************************************/			
			
			
			
		
	//read curricularCourseScopesIds
		List curricularCourseScopeList = (List) sp.getIPersistentCurricularCourseScope().readCurricularCourseScopesByCurricularCourse(curricularCourse);
		List curricularCourseScopesIds = new ArrayList(curricularCourseScopeList.size());
		Iterator scopes = curricularCourseScopeList.iterator();
			
		while(scopes.hasNext()){
			Integer curricularCourseScopeId = ((ICurricularCourseScope) scopes.next()).getIdInternal();
			curricularCourseScopesIds.add(curricularCourseScopeId);
		}

//		read executionPeriodsIds
		List executionCourseList = curricularCourse.getAssociatedExecutionCourses(); 
		List executionPeriodsIds = new ArrayList(executionCourseList.size());
		Iterator iter1 = executionCourseList.iterator();
		Integer executionPeriodId;
		 
		while(iter1.hasNext()){
			executionPeriodId = ((IDisciplinaExecucao) iter1.next()).getExecutionPeriod().getIdInternal();
			executionPeriodsIds.add(executionPeriodId);
		}
							  
			
		List enrolmentsIds =
			persistentEnrolment.readEnrolmentsByExecutionPeriodsAndCurricularCourseScopes(
	                                                                                  curricularCourseScopesIds, 
											                                          executionPeriodsIds);
			
		Iterator idsIter = enrolmentsIds.iterator();
		IEnrolment enrolment = null;
		IEnrolment enrolmentAux = null;
		List enrolmentsList = new ArrayList(enrolmentsIds.size());
			
//este read so sai daqui se copiarmos o que ta dentro do metodo readByOId para o nosso
//readEnrolmentsByExecutionPeriodsAndCurricularCourseScopes

		while(idsIter.hasNext()){ 
			enrolmentAux = new Enrolment();
			enrolmentAux.setIdInternal((Integer) idsIter.next());				
			enrolment = (IEnrolment) persistentEnrolment.readByOId(enrolmentAux, false);
			enrolmentsList.add(enrolment);
		}
			
			
			
	 /**************************************************************/
			
			
		Iterator iter = enrolmentsList.iterator();
		IStudentCurricularPlan studentCurricularPlan;
		
			while(iter.hasNext()){			//1 enrolment tem mts studentCurricularPlans
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