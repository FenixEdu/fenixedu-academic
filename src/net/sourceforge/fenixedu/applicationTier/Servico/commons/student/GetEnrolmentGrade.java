package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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

    public InfoEnrolmentEvaluation run(IEnrollment enrolment) throws FenixServiceException {
        List enrolmentEvaluations = enrolment.getEvaluations();

        if ((enrolment == null) || (enrolment.getEvaluations() == null)
                || (enrolment.getEvaluations().size() == 0)) {
            return null;
        }
        // This sorts the list ascendingly so we need to reverse it to get
        // the first object.
        Collections.sort(enrolmentEvaluations);
        Collections.reverse(enrolmentEvaluations);
        try {
            return getInfoLatestEvaluation((IEnrolmentEvaluation) enrolmentEvaluations.get(0));
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
                IEmployee employee = readEmployee(latestEvaluation.getEmployee().getIdInternal()
                        .intValue());
                latestEvaluation.setEmployee(employee);

                infolatestEvaluation.setInfoEmployee(InfoPerson.newInfoFromDomain(employee.getPerson()));
            }
            infolatestEvaluation.setInfoPersonResponsibleForGrade(InfoPerson
                    .newInfoFromDomain(latestEvaluation.getPersonResponsibleForGrade()));
        }
        return infolatestEvaluation;
    }

    private IEmployee readEmployee(int id) throws ExcepcaoPersistencia {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try {
            persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
            employee = persistentEmployee.readByIdInternal(id);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw e;
        }
        return employee;
    }

}