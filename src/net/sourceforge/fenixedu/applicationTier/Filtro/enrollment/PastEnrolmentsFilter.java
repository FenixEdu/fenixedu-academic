package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

public class PastEnrolmentsFilter extends Filtro {

	@Override
	public void execute(ServiceRequest request, ServiceResponse response)
			throws Exception {
        Object[] args = request.getServiceParameters().parametersArray();
        
        Student student = null;
        if (args[1] != null) {
            Integer studentCurricularPlanID = (Integer) args[1];
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentObject.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
            student = studentCurricularPlan.getStudent();
        } else if (args.length > 2 && args[2] != null) {
            Integer studentNumber = (Integer) args[2];
            student = persistentSupport.getIPersistentStudent().readByUsername("L" + studentNumber);
        }
        
		if (student == null) {
			throw new NotAuthorizedFilterException("noAuthorization");
		}
		
		ExecutionPeriod actualExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod().readActualExecutionPeriod();
		ExecutionPeriod previousExecutionPeriod = actualExecutionPeriod.getPreviousExecutionPeriod();
		ExecutionPeriod beforePreviousExecutionPeriod = previousExecutionPeriod.getPreviousExecutionPeriod();
		
		for (StudentCurricularPlan scp : student.getStudentCurricularPlans()) {
			for (Enrolment enrolment : scp.getEnrolments()) {
				if(enrolment.getExecutionPeriod().equals(previousExecutionPeriod) || enrolment.getExecutionPeriod().equals(beforePreviousExecutionPeriod)) {
					return;
				}
			}
		}
		throw new NotAuthorizedFilterException("error.no.enrolment.two.previous.executions");
	}
	
    protected Student readStudent(IUserView id) throws ExcepcaoPersistencia {
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        return persistentStudent.readByUsername(id.getUtilizador());
    }


}
