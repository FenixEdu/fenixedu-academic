package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;

import DataBeans.InfoCandidateRegistration;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.IMasterDegreeCandidate;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidateRegistrationInformation implements IServico {

    private static GetCandidateRegistrationInformation servico = new GetCandidateRegistrationInformation();

    /**
     * The singleton access method of this class.
     */
    public static GetCandidateRegistrationInformation getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private GetCandidateRegistrationInformation() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "GetCandidateRegistrationInformation";
    }

    public InfoCandidateRegistration run(Integer candidateID)
            throws FenixServiceException {

        ISuportePersistente sp = null;

        InfoCandidateRegistration infoCandidateRegistration = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

           

            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(
                            MasterDegreeCandidate.class, candidateID);

            IStudent student = sp.getIPersistentStudent()
                    .readByPersonAndDegreeType(
                            masterDegreeCandidate.getPerson(),
                            TipoCurso.MESTRADO_OBJ);

            IStudentCurricularPlan studentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente()
                    .readActiveStudentCurricularPlan(student.getNumber(),
                            TipoCurso.MESTRADO_OBJ);

            infoCandidateRegistration = new InfoCandidateRegistration();

            infoCandidateRegistration
                    .setInfoMasterDegreeCandidate(Cloner
                            .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate));
            infoCandidateRegistration
                    .setInfoStudentCurricularPlan(Cloner
                            .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));

            if (studentCurricularPlan.getEnrolments().size() == 0) {
                infoCandidateRegistration.setEnrolments(null);
            } else {
                infoCandidateRegistration.setEnrolments(new ArrayList());
                Iterator iterator = studentCurricularPlan.getEnrolments()
                        .iterator();
                while (iterator.hasNext()) {
                    Enrolment enrolment = (Enrolment) iterator.next();
                    infoCandidateRegistration.getEnrolments().add(
                            Cloner.copyIEnrolment2InfoEnrolment(enrolment));
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);

        }

        return infoCandidateRegistration;
    }
}