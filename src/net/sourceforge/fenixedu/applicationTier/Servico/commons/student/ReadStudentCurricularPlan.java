/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentCurricularPlan implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentCurricularPlan() {
    }

    public InfoStudentCurricularPlan run(Integer studentCurricularPlanID) throws ExcepcaoInexistente,
            FenixServiceException {
        if (studentCurricularPlanID == null) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        ISuportePersistente sp = null;
        IStudentCurricularPlan studentCurricularPlan = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // The student Curricular plan
            studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente()
                    .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        //return
        // Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
        return InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                .newInfoFromDomain(studentCurricularPlan);
    }
}