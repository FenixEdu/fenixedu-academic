package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade {

    /**
     * The actor of this class.
     */
    public GetEnrolmentGrade() {
    }

    public InfoEnrolmentEvaluation run(IEnrolment enrolment) throws FenixServiceException {
        List enrolmentEvaluations = enrolment.getEvaluations();

        if ((enrolment == null) || (enrolment.getEvaluations() == null)
                || (enrolment.getEvaluations().size() == 0)) {
            return null;
        }
        IEnrolmentEvaluation evaluation = (IEnrolmentEvaluation) Collections.max(enrolmentEvaluations);
        try {
            return getInfoLatestEvaluation(evaluation);
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }

    }

    private InfoEnrolmentEvaluation getInfoLatestEvaluation(IEnrolmentEvaluation latestEvaluation)
            throws ExcepcaoPersistencia {

        InfoEnrolmentEvaluation infolatestEvaluation = InfoEnrolmentEvaluation
                .newInfoFromDomain(latestEvaluation);
        if (latestEvaluation.getEmployee() != null) {
            if (String.valueOf(latestEvaluation.getEmployee().getIdInternal()) != null
                    || String.valueOf(latestEvaluation.getEmployee().getIdInternal()).length() > 0) {

                infolatestEvaluation.setInfoEmployee(InfoPerson.newInfoFromDomain(latestEvaluation.getEmployee().getPerson()));
            }
            infolatestEvaluation.setInfoPersonResponsibleForGrade(InfoPerson
                    .newInfoFromDomain(latestEvaluation.getPersonResponsibleForGrade()));
        }
        return infolatestEvaluation;
    }
}