package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteTutorship extends TutorshipManagement {

    public List<TutorshipErrorBean> run(Integer executionDegreeID, Integer tutorNumber, List<Tutorship> tutorsToDelete)
	    throws FenixServiceException {

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

	List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

	for (Tutorship tutorship : tutorsToDelete) {
	    Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
	    Integer studentNumber = registration.getNumber();

	    try {
		validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

		tutorship.delete();

	    } catch (FenixServiceException ex) {
		studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
	    }
	}

	return studentsWithErrors;
    }

}
