package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class MasterDegreeCurricularPlanStrategy extends DegreeCurricularPlanStrategy implements
        IMasterDegreeCurricularPlanStrategy {

    public MasterDegreeCurricularPlanStrategy(IDegreeCurricularPlan degreeCurricularPlan) {
        super(degreeCurricularPlan);
    }

    /**
     * Checks if the Master Degree Student has finished his scholar part. <br>
     * All his credits are added and compared to the ones required by his Degree
     * Curricular Plan.
     * 
     * @param The
     *            Student's Curricular Plan
     * @return A boolean indicating if he has fineshed it or not.
     */
    public boolean checkEndOfScholarship(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        double studentCredits = 0;

        IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();

        List enrolments = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentEnrolment()
                .readAllByStudentCurricularPlan(studentCurricularPlan);

        Iterator iterator = enrolments.iterator();

        if (studentCurricularPlan.getGivenCredits() != null) {
            studentCredits += studentCurricularPlan.getGivenCredits().doubleValue();
        }

        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();

            if ((enrolment.getEnrollmentState().equals(EnrollmentState.APROVED))
                    && (!(enrolment instanceof IEnrolmentInExtraCurricularCourse))) {
                studentCredits += enrolment.getCurricularCourse().getCredits().doubleValue();
            }
        }

        return (studentCredits >= degreeCurricularPlan.getNeededCredits().doubleValue());
    }

    public Date dateOfEndOfScholarship(IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        Date date = null;
        InfoEnrolmentEvaluation infoEnrolmentEvaluation = null;

        //		float studentCredits = 0;
        //		
        //		IDegreeCurricularPlan degreeCurricularPlan =
        // super.getDegreeCurricularPlan();

        List enrolments = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentEnrolment()
                .readAllByStudentCurricularPlan(studentCurricularPlan);

        Iterator iterator = enrolments.iterator();

        while (iterator.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iterator.next();
            if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {

                try {
                    infoEnrolmentEvaluation = new GetEnrolmentGrade().run(enrolment);
                } catch (FenixServiceException e) {
                    e.printStackTrace();
                    continue;
                }

                if (infoEnrolmentEvaluation.getExamDate() == null) {
                    continue;
                }

                if (date == null) {
                    date = new Date(infoEnrolmentEvaluation.getExamDate().getTime());
                    continue;
                }

                if (infoEnrolmentEvaluation.getExamDate().after(date)) {
                    date.setTime(infoEnrolmentEvaluation.getExamDate().getTime());
                }

            }
        }
        return date;
    }

}