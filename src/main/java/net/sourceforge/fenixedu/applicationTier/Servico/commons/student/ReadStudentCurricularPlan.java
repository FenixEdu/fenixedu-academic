/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentCurricularPlan {

    @Atomic
    public static InfoStudentCurricularPlan run(final String studentCurricularPlanID) throws FenixServiceException {
        if (studentCurricularPlanID == null) {
            throw new FenixServiceException("Persistence layer error");
        }

        final StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanID);
        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}