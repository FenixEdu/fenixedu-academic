/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadStudentCurricularPlan extends Service {

    public InfoStudentCurricularPlan run(Integer studentCurricularPlanID) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {
        if (studentCurricularPlanID == null) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        ISuportePersistente sp = null;
        StudentCurricularPlan studentCurricularPlan = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // The student Curricular plan
        studentCurricularPlan = (StudentCurricularPlan) sp.getIStudentCurricularPlanPersistente()
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                .newInfoFromDomain(studentCurricularPlan);
    }
}