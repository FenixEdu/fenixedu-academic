/**
 * Jul 27, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadActiveStudentCurricularPlanByNumberAndType extends Service {

    public InfoStudentCurricularPlan run(
            Integer studentNumber, DegreeType degreeType) throws ExcepcaoPersistencia {

    	Registration registration = Registration.readRegisteredRegistrationByNumberAndDegreeType(studentNumber, degreeType);
        StudentCurricularPlan scp = null;
        if(registration != null) {
        	scp = registration.getActiveOrConcludedStudentCurricularPlan();
        }

        return InfoStudentCurricularPlan.newInfoFromDomain(scp);
    }

}
