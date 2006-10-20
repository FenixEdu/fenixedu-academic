package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class PastEnrolmentsFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        Object[] args = request.getServiceParameters().parametersArray();

        Registration registration = null;
        if (args[1] != null) {
            Integer studentCurricularPlanID = (Integer) args[1];
            StudentCurricularPlan studentCurricularPlan = rootDomainObject
                    .readStudentCurricularPlanByOID(studentCurricularPlanID);
            registration = studentCurricularPlan.getRegistration();
        } else if (args.length > 2 && args[2] != null) {
            Integer studentNumber = (Integer) args[2];
            registration = Registration.readByUsername("L" + studentNumber);
        }

        if (registration == null) {
            throw new NotAuthorizedFilterException("noAuthorization");
        }

        ExecutionPeriod actualExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        ExecutionPeriod previousExecutionPeriod = actualExecutionPeriod.getPreviousExecutionPeriod();
        ExecutionPeriod beforePreviousExecutionPeriod = previousExecutionPeriod
                .getPreviousExecutionPeriod();

        for (StudentCurricularPlan scp : registration.getStudentCurricularPlans()) {
            for (Enrolment enrolment : scp.getEnrolments()) {
                if (enrolment.getExecutionPeriod().equals(previousExecutionPeriod)
                        || enrolment.getExecutionPeriod().equals(beforePreviousExecutionPeriod)) {
                    return;
                }
            }
        }
        throw new NotAuthorizedFilterException("error.no.enrolment.two.previous.executions");
    }

    protected Registration readStudent(IUserView id) throws ExcepcaoPersistencia {
        return Registration.readByUsername(id.getUtilizador());
    }

}
