/*
 * Created on 14/Mar/2003
 *
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentList implements IServico {

	private static GetEnrolmentList servico = new GetEnrolmentList();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static GetEnrolmentList getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private GetEnrolmentList() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "GetEnrolmentList";
	}

	public List run(InfoStudentCurricularPlan infoStudentCurricularPlan, EnrolmentState enrolmentState) throws FenixServiceException, Exception {

		ISuportePersistente sp = null;
		List enrolmentList = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
		    IStudentCurricularPlan iStudentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
			
			// Read the list 
			
			enrolmentList = sp.getIPersistentEnrolment().readEnrolmentsByStudentCurricularPlanAndEnrolmentState(iStudentCurricularPlan, enrolmentState);
	
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 

			
		List result = new ArrayList();
		Iterator iterator = enrolmentList.iterator();

		while(iterator.hasNext()) {	
		    IEnrollment enrolment = (IEnrollment) iterator.next();
		    if(!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE_OBJ)){
		        result.add(Cloner.copyIEnrolment2InfoEnrolment(enrolment));	
		    }
		}
		
		
		return result;		
	}
	
	public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException, Exception {

			ISuportePersistente sp = null;
			List enrolmentList = null;
			try {
				sp = SuportePersistenteOJB.getInstance();
				IStudentCurricularPlan iStudentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
			
				// Read the list 
			
				enrolmentList = sp.getIPersistentEnrolment().readAllByStudentCurricularPlan(iStudentCurricularPlan);
	
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			} 

			
			List result = new ArrayList();
			Iterator iterator = enrolmentList.iterator();

			while(iterator.hasNext()) {	
			    IEnrollment enrolment = (IEnrollment) iterator.next();
			    if(!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE_OBJ)){
			        result.add(Cloner.copyIEnrolment2InfoEnrolment(enrolment));	
			    }
			}
		
		
			return result;		
		}
	
	
}
