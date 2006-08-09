package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateStudentCurricularPlan extends Service {

    public void run(final Integer studentNumber, final DegreeType degreeType,
            final StudentCurricularPlanState studentCurricularPlanState,
            final Integer degreeCurricularPlanId, final Date startDate) throws ExcepcaoPersistencia,
            FenixServiceException {

        final Student student = Student.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if (student == null) {
            throw new NonExistingServiceException("exception.student.does.not.exist");
        }

        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException("exception.degree.curricular.plan.does.not.exist");
        }

        new StudentCurricularPlan(student, degreeCurricularPlan,
                studentCurricularPlanState, startDate);
    }
}
