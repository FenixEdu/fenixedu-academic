package net.sourceforge.fenixedu.applicationTier.Servico.equivalence;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoEquivalenceContext;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in May 12, 2004
 */

public class ReadListsOfEnrollmentsWithEquivalences extends EnrollmentEquivalenceServiceUtils implements
        IService {
    public ReadListsOfEnrollmentsWithEquivalences() {
    }

    public InfoEquivalenceContext run(Integer studentNumber, TipoCurso degreeType,
            Integer fromStudentCurricularPlanID) throws FenixServiceException {
        List args = new ArrayList();
        args.add(0, fromStudentCurricularPlanID);
        args.add(1, studentNumber);
        args.add(2, degreeType);

        return (InfoEquivalenceContext) convertDataOutput(execute(convertDataInput(args)));
    }

    /**
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service DataBeans input objects to their
     *      respective Domain objects. These Domain objects are to be used by
     *      the service's logic.
     */
    protected Object convertDataInput(Object object) {
        return object;
    }

    /**
     * @param List
     * @return InfoEquivalenceContext
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service output Domain objects to their
     *      respective DataBeans. These DataBeans are the result of executing
     *      this service logic and are to be passed on to the uper layer of the
     *      architecture.
     */
    protected Object convertDataOutput(Object object) {
        List elements = (List) object;

        List enrollmentsFromEquivalences = (List) elements.get(0);
        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) elements.get(1);

        InfoEquivalenceContext infoEquivalenceContext = new InfoEquivalenceContext();

        infoEquivalenceContext
                .setInfoEnrollmentsFromEquivalences(cloneEnrolmentsToInfoEnrolments(enrollmentsFromEquivalences));

        //CLONER
        //infoEquivalenceContext.setInfoStudentCurricularPlan(Cloner
        //	.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));
        infoEquivalenceContext
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                        .newInfoFromDomain(studentCurricularPlan));

        return infoEquivalenceContext;
    }

    /**
     * @param List
     * @return List
     * @throws FenixServiceException
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method implements the buisiness logic of this service.
     */
    protected Object execute(Object object) throws FenixServiceException {
        List input = (List) object;

        Integer fromStudentCurricularPlanID = (Integer) input.get(0);
        Integer studentNumber = (Integer) input.get(1);
        TipoCurso degreeType = (TipoCurso) input.get(2);

        List output = new ArrayList();

        try {
            ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistenceDAO
                    .getIStudentCurricularPlanPersistente();
            IPersistentEnrolmentEquivalence enrollmentEquivalenceDAO = persistenceDAO
                    .getIPersistentEnrolmentEquivalence();

            IStudentCurricularPlan fromStudentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO
                    .readByOID(StudentCurricularPlan.class, fromStudentCurricularPlanID);

            List studentEnrollments = fromStudentCurricularPlan.getEnrolments();

            List enrollmentsFromEquivalences = new ArrayList();

            for (int i = 0; i < studentEnrollments.size(); i++) {
                IEnrollment enrollment = (IEnrollment) studentEnrollments.get(i);

                IEnrolmentEquivalence enrollmentEquivalence = enrollmentEquivalenceDAO
                        .readByEnrolment(enrollment);
                if (enrollmentEquivalence != null) {
                    enrollmentsFromEquivalences.add(enrollment);
                }
            }

            output.add(0, enrollmentsFromEquivalences);
            output.add(1, getActiveStudentCurricularPlan(studentNumber, degreeType));

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return output;
    }

}