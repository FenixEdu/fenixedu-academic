/*
 * Created on Jul 28, 2004
 *
 */
package ServidorAplicacao.Servico.student.schoolRegistration;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentWithInfoCurricularCourse;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ReadStudentEnrollmentsAndClass implements IService {
	
	public ReadStudentEnrollmentsAndClass() {
		
	}
	
	public List run(UserView userView) 
		throws ExcepcaoPersistencia, FenixServiceException {
		
		 ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
		 
		 IStudentCurricularPlanPersistente pSCP = suportePersistente.getIStudentCurricularPlanPersistente();
		 IPersistentEnrollment pEnrollment = suportePersistente.getIPersistentEnrolment();
		 
		 String user = userView.getUtilizador();
	     Integer studentNumber = new Integer(user.substring(1));
		 
		 IStudentCurricularPlan scp = pSCP.readActiveStudentCurricularPlan(studentNumber,TipoCurso.LICENCIATURA_OBJ);
		 List studentEnrollments = pEnrollment.readAllByStudentCurricularPlan(scp);

		 List infoEnrollments = new ArrayList();
		 InfoClass infoClass = new InfoClass();
		 
		 for(int iterator = 0; iterator < studentEnrollments.size(); iterator++) {
		 	
		 	IEnrollment enrollment = (IEnrollment) studentEnrollments.get(iterator);
		 	
		 	InfoEnrolment infoEnrollment = InfoEnrolmentWithInfoCurricularCourse.newInfoFromDomain(enrollment);
		 	infoEnrollments.add(infoEnrollment);
		 }
		 
		 // TODO : [NRMC] falta ler a turma
		 
		infoClass.setNome("10101");
		 
		 
		List result = new ArrayList();
		result.add(infoEnrollments);
		result.add(infoClass);
		
		return result;
	}

}

