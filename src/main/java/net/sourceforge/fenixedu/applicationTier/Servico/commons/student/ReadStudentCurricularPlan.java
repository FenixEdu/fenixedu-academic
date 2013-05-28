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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadStudentCurricularPlan {

    @Service
    public static InfoStudentCurricularPlan run(final Integer studentCurricularPlanID) throws FenixServiceException {
        if (studentCurricularPlanID == null) {
            throw new FenixServiceException("Persistence layer error");
        }

        final StudentCurricularPlan studentCurricularPlan =
                AbstractDomainObject.fromExternalId(studentCurricularPlanID);
        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}