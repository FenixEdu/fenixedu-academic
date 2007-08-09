package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchStudents extends Service {

    public List<RegistrationWithStateForExecutionYearBean> run(SearchStudentsByDegreeParametersBean searchbean)
	    throws FenixServiceException {

	final Set<Registration> registrations = new TreeSet<Registration>(Registration.NUMBER_COMPARATOR);

	final ExecutionYear executionYear = searchbean.getExecutionYear();
	for (DegreeCurricularPlan degreeCurricularPlan : searchbean.getDegree().getDegreeCurricularPlansForYear(executionYear)) {
	    degreeCurricularPlan.getRegistrations(executionYear, registrations);
	}

	return filterResults(searchbean, registrations, executionYear);
    }

    private List<RegistrationWithStateForExecutionYearBean> filterResults(SearchStudentsByDegreeParametersBean searchbean,
	    final Set<Registration> registrations, final ExecutionYear executionYear) {

	final List<RegistrationWithStateForExecutionYearBean> result = new ArrayList<RegistrationWithStateForExecutionYearBean>();
	for (Registration registration : registrations) {

	    if (!searchbean.getRegistrationAgreements().isEmpty()
		    && !searchbean.getRegistrationAgreements().contains(registration.getRegistrationAgreement())) {
		continue;
	    }

	    final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);
	    if (lastRegistrationState == null
		    || (!searchbean.getRegistrationStateTypes().isEmpty() && !searchbean.getRegistrationStateTypes().contains(
			    lastRegistrationState.getStateType()))) {
		continue;
	    }

	    result.add(new RegistrationWithStateForExecutionYearBean(registration, lastRegistrationState.getStateType()));
	}

	return result;
    }

}
