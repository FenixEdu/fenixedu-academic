/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentCurricularPlan extends Service {

    public InfoStudentCurricularPlan run(Integer studentCurricularPlanID) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {
        if (studentCurricularPlanID == null) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        StudentCurricularPlan studentCurricularPlan = null;

        // The student Curricular plan
        studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);

        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoStudentCurricularPlan
                .newInfoFromDomain(studentCurricularPlan);
    }
}