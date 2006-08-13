/**
 * Jul 27, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadActiveStudentCurricularPlanByNumberAndType extends Service {

    public InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan run(
            Integer studentNumber, DegreeType degreeType) throws ExcepcaoPersistencia {

    	Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        StudentCurricularPlan scp = null;
        if(student != null) {
        	scp = student.getActiveOrConcludedStudentCurricularPlan();
        }

        return InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan.newInfoFromDomain(scp);
    }

}
