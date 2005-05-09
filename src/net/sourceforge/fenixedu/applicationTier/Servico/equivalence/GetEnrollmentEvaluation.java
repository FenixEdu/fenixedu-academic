package net.sourceforge.fenixedu.applicationTier.Servico.equivalence;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in May 17, 2004
 */

public class GetEnrollmentEvaluation extends EnrollmentEquivalenceServiceUtils implements IService {
    public GetEnrollmentEvaluation() {
    }

    public InfoEnrolmentEvaluation run(Integer studentNumber, DegreeType degreeType, Integer enrollmentID)
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

        IEnrolment enrollment = null;
        try {
            ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
            enrollment = (IEnrolment) enrollmentDAO.readByOID(Enrolment.class, enrollmentID);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        InfoEnrolmentEvaluation infoEnrolmentEvaluation = service.run(enrollment);

        infoEnrolmentEvaluation.setInfoEnrolment(InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                .newInfoFromDomain(enrollment));

        return infoEnrolmentEvaluation;
    }
}