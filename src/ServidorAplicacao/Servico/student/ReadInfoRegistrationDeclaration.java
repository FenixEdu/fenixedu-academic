/*
 * Created on 12/Jan/2005
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoPerson;
import DataBeans.student.InfoRegistrationDeclaration;
import Dominio.IEnrollment;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Ricardo Rodrigues
 *  
 */
public class ReadInfoRegistrationDeclaration implements IService {

    public InfoRegistrationDeclaration run(Integer studentNumber, TipoCurso degreeType)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;
        IPersistentStudent persistentStudent = null;
        List studentCurricularPlans = null;
        IStudent student = null;

        sp = SuportePersistenteOJB.getInstance();

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
