package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchStudents extends FenixService {

    @Service
    public static List<RegistrationWithStateForExecutionYearBean> run(final SearchStudentsByDegreeParametersBean searchbean)
	    throws FenixServiceException {

	final Set<Registration> registrations = new TreeSet<Registration>(Registration.NUMBER_COMPARATOR);

	final Degree degree = searchbean.getDegree();
	final ExecutionYear executionYear = searchbean.getExecutionYear();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	    if ((searchbean.getDegreeType() == null || degreeCurricularPlan.getDegreeType() == searchbean.getDegreeType())
		    && (degree == null || degreeCurricularPlan.getDegree() == degree)) {
		degreeCurricularPlan.getRegistrations(executionYear, registrations);
	    }
	}

	return filterResults(searchbean, registrations, executionYear);
    }

    private static List<RegistrationWithStateForExecutionYearBean> filterResults(SearchStudentsByDegreeParametersBean searchbean,
	    final Set<Registration> registrations, final ExecutionYear executionYear) {

	final List<RegistrationWithStateForExecutionYearBean> result = new ArrayList<RegistrationWithStateForExecutionYearBean>();
	for (final Registration registration : registrations) {

	    if (!searchbean.getRegistrationAgreements().isEmpty()
		    && !searchbean.getRegistrationAgreements().contains(registration.getRegistrationAgreement())) {
		continue;
	    }

	    if (searchbean.hasAnyStudentStatuteType() && !hasStudentStatuteType(searchbean, registration)) {
		continue;
	    }

	    final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);
	    if (lastRegistrationState == null
		    || (!searchbean.getRegistrationStateTypes().isEmpty() && !searchbean.getRegistrationStateTypes().contains(
			    lastRegistrationState.getStateType()))) {
		continue;
	    }

	    if (!searchbean.getActiveEnrolments()
		    || registration.hasAnyEnrolmentsIn(executionYear)) {
		result.add(new RegistrationWithStateForExecutionYearBean(registration, lastRegistrationState.getStateType()));
	    }
	}

	return result;
    }

    static private boolean hasStudentStatuteType(final SearchStudentsByDegreeParametersBean searchbean,
	    final Registration registration) {
	return CollectionUtils.containsAny(searchbean.getStudentStatuteTypes(), registration.getStudent()
		.getStatutesTypesValidOnAnyExecutionSemesterFor(searchbean.getExecutionYear()));
    }

}