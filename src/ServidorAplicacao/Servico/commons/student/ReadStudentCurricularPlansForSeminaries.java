/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentCurricularPlansForSeminaries implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentCurricularPlansForSeminaries() {
    }

    public List run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException {
        ISuportePersistente sp = null;

        List studentCurricularPlans = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            studentCurricularPlans = sp.getIStudentCurricularPlanPersistente().readByUsername(
                    userView.getUtilizador());

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if ((studentCurricularPlans == null) || (studentCurricularPlans.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlans.iterator();
        List result = new ArrayList();

        while (iterator.hasNext()) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();

            result.add(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                    .newInfoFromDomain(studentCurricularPlan));
        }

        if ((result.size() == 0)) {
            throw new NonExistingServiceException();
        }

        return result;
    }

}