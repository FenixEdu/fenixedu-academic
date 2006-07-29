package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class MasterDegreeCurricularPlanStrategy extends DegreeCurricularPlanStrategy implements
        IMasterDegreeCurricularPlanStrategy {

    public MasterDegreeCurricularPlanStrategy(DegreeCurricularPlan degreeCurricularPlan) {
        super(degreeCurricularPlan);
    }

    /**
     * Checks if the Master Degree Student has finished his scholar part. <br/>
     * All his credits are added and compared to the ones required by his Degree
     * Curricular Plan.
     * 
     * @param The
     *            Student's Curricular Plan
     * @return A boolean indicating if he has fineshed it or not.
     */
    public boolean checkEndOfScholarship(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {
        double studentCredits = 0;

        DegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();

        List enrolments = studentCurricularPlan.getEnrolments();

        Iterator iterator = enrolments.iterator();

        if (studentCurricularPlan.getGivenCredits() != null) {
            studentCredits += studentCurricularPlan.getGivenCredits().doubleValue();
        }

        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();

            if ((enrolment.getEnrollmentState().equals(EnrollmentState.APROVED))
                    && (!(enrolment instanceof EnrolmentInExtraCurricularCourse))) {
                studentCredits += enrolment.getCurricularCourse().getCredits().doubleValue();
            }
        }

        if(degreeCurricularPlan.getNeededCredits() != null){
            return (studentCredits >= degreeCurricularPlan.getNeededCredits().doubleValue());    
        }
        return true;
    }

    public Date dateOfEndOfScholarship(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        Date date = null;
        InfoEnrolmentEvaluation infoEnrolmentEvaluation = null;

        //		float studentCredits = 0;
        //		
        //		DegreeCurricularPlan degreeCurricularPlan =
        // super.getDegreeCurricularPlan();

        List enrolments = studentCurricularPlan.getEnrolments();

        Iterator iterator = enrolments.iterator();

        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();
            if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {

                infoEnrolmentEvaluation = new GetEnrolmentGrade().run(enrolment);

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