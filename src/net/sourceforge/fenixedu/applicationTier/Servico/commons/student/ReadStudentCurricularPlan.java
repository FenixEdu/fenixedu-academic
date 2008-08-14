/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class ReadStudentCurricularPlan extends Service {

    final public InfoStudentCurricularPlan run(final Integer studentCurricularPlanID) throws FenixServiceException {
	if (studentCurricularPlanID == null) {
	    throw new FenixServiceException("Persistence layer error");
	}

	final StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID(studentCurricularPlanID);
	if (studentCurricularPlan == null) {
	    throw new NonExistingServiceException();
	}

	return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}
