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
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class ReadStudentsFromDegreeCurricularPlan implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentsFromDegreeCurricularPlan() {
    }

    public List run(Integer degreeCurricularPlanID, TipoCurso degreeType) throws ExcepcaoInexistente,
            FenixServiceException {

        ISuportePersistente sp = null;

        List students = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the Students

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            degreeCurricularPlanID);

            students = sp.getIStudentCurricularPlanPersistente().readByDegreeCurricularPlan(
                    degreeCurricularPlan);

        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException("Persistence layer error", ex);
        }

        if ((students == null) || (students.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = students.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();

            result.add(Cloner
                    .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));
        }

        return result;
    }
}