/*
 * Created on 12/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoRegistrationDeclaration;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */
public class ReadInfoRegistrationDeclaration extends Service {

    public InfoRegistrationDeclaration run(Integer studentNumber, DegreeType degreeType)
            throws FenixServiceException, ExcepcaoPersistencia {

        Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if (student == null) {
            throw new NonExistingStudentServiceException();
        }

        StudentCurricularPlan scp = student.getActiveStudentCurricularPlan();
        if (scp == null) {
            throw new NonExistingActiveSCPServiceException();
        }

        ExecutionYear executionYear = null;
        if (scp.getEnrolments() != null) {
            for (Iterator iter = scp.getEnrolments().iterator(); iter.hasNext();) {
                Enrolment enrollment = (Enrolment) iter.next();
                Calendar calendar = Calendar.getInstance();
                Date actualDate = calendar.getTime();
                Date beginDate = enrollment.getExecutionPeriod().getExecutionYear().getBeginDate();
                Date endDate = enrollment.getExecutionPeriod().getExecutionYear().getEndDate();

                if (actualDate.before(endDate) && actualDate.after(beginDate)) {
                    executionYear = enrollment.getExecutionPeriod().getExecutionYear();
                    break;
                }
            }
        }

        if (executionYear == null) {
            throw new NonExistingEnrollmentServiceException();
        }

        String degreeName = scp.getDegreeCurricularPlan().getDegree().getNome();
        InfoPerson infoPerson = InfoPerson.newInfoFromDomain(student.getPerson());
        InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);

        return new InfoRegistrationDeclaration(degreeName, infoExecutionYear, infoPerson);
    }

    public class NonExistingStudentServiceException extends NonExistingServiceException {
    }

    public class NonExistingActiveSCPServiceException extends NonExistingServiceException {
    }

    public class NonExistingEnrollmentServiceException extends NonExistingServiceException {
    }

}
