package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class GetCandidateRegistrationInformation extends Service {

    public InfoCandidateRegistration run(Integer candidateID) {

	MasterDegreeCandidate masterDegreeCandidate = rootDomainObject
		.readMasterDegreeCandidateByOID(candidateID);

	Registration registration = masterDegreeCandidate.getPerson().readStudentByDegreeType(
		DegreeType.MASTER_DEGREE);

	StudentCurricularPlan studentCurricularPlan = null;
	if (registration != null) {
	    studentCurricularPlan = registration.getActiveStudentCurricularPlan();
	}

	final InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();

	infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
		.newInfoFromDomain(masterDegreeCandidate));
	infoCandidateRegistration.setInfoStudentCurricularPlan(InfoStudentCurricularPlan
		.newInfoFromDomain(studentCurricularPlan));

	if (studentCurricularPlan.getEnrolmentsCount() == 0) {
	    infoCandidateRegistration.setEnrolments(null);

	} else {
	    infoCandidateRegistration.setEnrolments(new ArrayList());
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		infoCandidateRegistration.getEnrolments()
			.add(InfoEnrolment.newInfoFromDomain(enrolment));
	    }
	}

	return infoCandidateRegistration;
    }
}