package ServidorAplicacao.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoPerson;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade implements IService {

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