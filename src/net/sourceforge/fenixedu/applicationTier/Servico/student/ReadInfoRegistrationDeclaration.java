/*
 * Created on 12/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoRegistrationDeclaration;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class ReadInfoRegistrationDeclaration implements IService {

    public InfoRegistrationDeclaration run(Integer studentNumber, TipoCurso degreeType)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;
        IPersistentStudent persistentStudent = null;
        IStudent student = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        persistentStudent = sp.getIPersistentStudent();
        student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber, degreeType);

        if (student == null) {
            throw new NonExistingStudentServiceException();
        }

        IStudentCurricularPlan scp = student.getActiveStudentCurricularPlan();

        if (scp == null) {
            throw new NonExistingActiveSCPServiceException();
        }

        IExecutionYear executionYear = null;
        if (scp.getEnrolments() != null) {
            for (Iterator iter = scp.getEnrolments().iterator(); iter.hasNext();) {
                IEnrollment enrollment = (IEnrollment) iter.next();
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
