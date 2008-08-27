package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;

public class EditPosGradStudentCurricularPlanStateAndCredits extends Service {

    public void run(IUserView userView, Integer studentCurricularPlanId, String currentState, Double credits, String startDate,
	    List<Integer> extraCurricularOIDs, String observations, Integer branchId, String specialization)
	    throws FenixServiceException {
	final StudentCurricularPlan scp = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanId);
	if (scp == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final Person person = Person.readPersonByUsername(userView.getUtilizador());
	if (person == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final Employee employee = person.getEmployee();
	if (employee == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final Branch branch = rootDomainObject.readBranchByOID(branchId);
	if (branch == null) {
	    throw new InvalidArgumentsServiceException();
	}

	scp.setStartDate(stringDateToCalendar(startDate).getTime());
	// scp.setCurrentState(newState);
	scp.setEmployee(employee);
	scp.setGivenCredits(credits);
	scp.setObservations(observations);
	scp.setBranch(branch);
	scp.setSpecialization(Specialization.valueOf(specialization));

	for (final Enrolment enrolment : scp.getEnrolments()) {
	    if (extraCurricularOIDs.contains(enrolment.getIdInternal())) {
		if (enrolment.isExtraCurricular()) {
		    enrolment.setIsExtraCurricular(false);
		} else {
		    enrolment.markAsExtraCurricular();
		}
	    }
	}
    }

    private Calendar stringDateToCalendar(String startDate) throws NumberFormatException {
	final Calendar result = Calendar.getInstance();

	String[] aux = startDate.split("/");
	result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(aux[0]));
	result.set(Calendar.MONTH, Integer.parseInt(aux[1]) - 1);
	result.set(Calendar.YEAR, Integer.parseInt(aux[2]));

	return result;
    }

}
