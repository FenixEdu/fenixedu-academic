package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade {

    public InfoEnrolmentEvaluation run(IEnrolment enrolment) throws ExcepcaoPersistencia {
        if (enrolment == null) {
            return null;
        }

        final IStudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
        final IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        final IDegree degree = degreeCurricularPlan.getDegree();

        return (degree.getTipoCurso() == DegreeType.DEGREE) ?
                    run(enrolment.getAllFinalEnrolmentEvaluations()) : run(enrolment.getEvaluations());
    }

    private InfoEnrolmentEvaluation run(List<IEnrolmentEvaluation> enrolmentEvaluations) throws ExcepcaoPersistencia {
        if(enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) {
            return null;
        }
        
        final IEnrolmentEvaluation evaluation = (IEnrolmentEvaluation) Collections.max(enrolmentEvaluations);
        return getInfoLatestEvaluation(evaluation);
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