/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.commons.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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