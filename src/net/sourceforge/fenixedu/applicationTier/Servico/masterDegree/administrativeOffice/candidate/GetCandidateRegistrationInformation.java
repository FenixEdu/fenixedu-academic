package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidateRegistrationInformation extends Service {

    public InfoCandidateRegistration run(Integer candidateID) throws FenixServiceException,
            ExcepcaoPersistencia {
        InfoCandidateRegistration infoCandidateRegistration = null;

        MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);

        Registration student = masterDegreeCandidate.getPerson().readStudentByDegreeType(
                DegreeType.MASTER_DEGREE);

        StudentCurricularPlan studentCurricularPlan = null;
        if(student != null) {
        	studentCurricularPlan = student.getActiveStudentCurricularPlan();
        }

        infoCandidateRegistration = new InfoCandidateRegistration();

        infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate));
        infoCandidateRegistration
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlan
                        .newInfoFromDomain(studentCurricularPlan));

        if (studentCurricularPlan.getEnrolments().size() == 0) {
            infoCandidateRegistration.setEnrolments(null);
        } else {
            infoCandidateRegistration.setEnrolments(new ArrayList());
            Iterator iterator = studentCurricularPlan.getEnrolments().iterator();
            while (iterator.hasNext()) {
                Enrolment enrolment = (Enrolment) iterator.next();
                InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                        .newInfoFromDomain(enrolment);
                infoCandidateRegistration.getEnrolments().add(infoEnrolment);
            }
        }

        return infoCandidateRegistration;
    }
}