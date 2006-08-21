/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentCurricularPlansForSeminaries extends Service {

    public List run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException,
            ExcepcaoPersistencia {
    	Registration registration = Registration.readByUsername(userView.getUtilizador());
    	List<StudentCurricularPlan> studentCurricularPlans = null;
    	if(registration != null) {
    		studentCurricularPlans = registration.getStudentCurricularPlans();
    	}

        if ((studentCurricularPlans == null) || (studentCurricularPlans.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlans.iterator();
        List result = new ArrayList();

        while (iterator.hasNext()) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();

            result.add(InfoStudentCurricularPlan
                    .newInfoFromDomain(studentCurricularPlan));
        }

        if ((result.size() == 0)) {
            throw new NonExistingServiceException();
        }

        return result;
    }

}