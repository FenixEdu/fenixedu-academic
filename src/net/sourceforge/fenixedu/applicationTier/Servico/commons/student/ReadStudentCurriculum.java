/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentCurriculum extends Service {

    public List run(Integer executionDegreeCode, Integer studentCurricularPlanID)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {
    	
        final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);
        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlan.getEnrolments().iterator();
        List result = new ArrayList();

        GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
        while (iterator.hasNext()) {
            Enrolment enrolmentTemp = (Enrolment) iterator.next();

            InfoEnrolmentEvaluation infoEnrolmentEvaluation = getEnrollmentGrade.run(enrolmentTemp);

            InfoEnrolment infoEnrolment = InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                    .newInfoFromDomain(enrolmentTemp);

            infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

            result.add(infoEnrolment);
        }

        return result;
    }
}