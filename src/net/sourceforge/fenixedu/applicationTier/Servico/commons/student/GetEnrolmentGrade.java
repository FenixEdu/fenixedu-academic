package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade {

    public InfoEnrolmentEvaluation run(Enrolment enrolment) throws ExcepcaoPersistencia {
        if (enrolment == null) {
            return null;
        }

        final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();

        return (degree.getTipoCurso() == DegreeType.DEGREE) ?
                    run(enrolment.getAllFinalEnrolmentEvaluations()) : run(enrolment.getEvaluations());
    }

    private InfoEnrolmentEvaluation run(List<EnrolmentEvaluation> enrolmentEvaluations) throws ExcepcaoPersistencia {
        if(enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) {
            return null;
        }
        
        final EnrolmentEvaluation evaluation = (EnrolmentEvaluation) Collections.max(enrolmentEvaluations);
        return getInfoLatestEvaluation(evaluation);
    }

    private InfoEnrolmentEvaluation getInfoLatestEvaluation(EnrolmentEvaluation latestEvaluation)
            throws ExcepcaoPersistencia {

        InfoEnrolmentEvaluation infolatestEvaluation = InfoEnrolmentEvaluation
                .newInfoFromDomain(latestEvaluation);
        if (latestEvaluation.getEmployee() != null) {
            if (String.valueOf(latestEvaluation.getEmployee().getIdInternal()) != null
                    || String.valueOf(latestEvaluation.getEmployee().getIdInternal()).length() > 0) {
            	final Person person = latestEvaluation.getEmployee().getPerson();
            	if (person != null) {
            		infolatestEvaluation.setInfoEmployee(newInfoPersonFromDomain(person));
            	}
            }
            final Person person = latestEvaluation.getPersonResponsibleForGrade();
            infolatestEvaluation.setInfoPersonResponsibleForGrade(newInfoPersonFromDomain(person));
        }
        return infolatestEvaluation;
    }

    private InfoPerson newInfoPersonFromDomain(final Person person) {
		final InfoPerson infoPerson = new InfoPerson();
		infoPerson.setIdInternal(person.getIdInternal());
		infoPerson.setUsername(person.getUsername());
		infoPerson.setNome(person.getName());
		return infoPerson;
    }

}