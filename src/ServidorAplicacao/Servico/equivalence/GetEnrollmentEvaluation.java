package ServidorAplicacao.Servico.equivalence;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import Dominio.Enrolment;
import Dominio.IEnrollment;
import ServidorAplicacao.Servico.commons.student.GetEnrolmentGrade;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author David Santos in May 17, 2004
 */

public class GetEnrollmentEvaluation extends EnrollmentEquivalenceServiceUtils implements IService {
    public GetEnrollmentEvaluation() {
    }

    public InfoEnrolmentEvaluation run(Integer studentNumber, TipoCurso degreeType, Integer enrollmentID)
            throws FenixServiceException {
        return (InfoEnrolmentEvaluation) convertDataOutput(execute(convertDataInput(enrollmentID)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     */
    protected Object convertDataInput(Object object) {
        return object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.Service#convertDataOutput(java.lang.Object)
     */
    protected Object convertDataOutput(Object object) {
        return object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.Service#execute(java.lang.Object)
     */
    protected Object execute(Object object) throws FenixServiceException {
        Integer enrollmentID = (Integer) object;

        GetEnrolmentGrade service = new GetEnrolmentGrade();

        IEnrollment enrollment = null;
        try {
            ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
            IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
            enrollment = (IEnrollment) enrollmentDAO.readByOID(Enrolment.class, enrollmentID);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        InfoEnrolmentEvaluation infoEnrolmentEvaluation = service.run(enrollment);
        //CLONER
        //infoEnrolmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(enrollment));
        infoEnrolmentEvaluation.setInfoEnrolment(InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                .newInfoFromDomain(enrollment));

        return infoEnrolmentEvaluation;
    }
}