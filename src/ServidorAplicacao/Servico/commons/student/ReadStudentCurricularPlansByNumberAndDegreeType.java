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
import DataBeans.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class ReadStudentCurricularPlansByNumberAndDegreeType implements IService {

    public ReadStudentCurricularPlansByNumberAndDegreeType() {
    }

    public List run(Integer studentNumber, TipoCurso degreeType) throws ExcepcaoInexistente,
            FenixServiceException {
        ISuportePersistente sp = null;

        List studentCurricularPlans = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            studentCurricularPlans = sp.getIStudentCurricularPlanPersistente()
                    .readByStudentNumberAndDegreeType(studentNumber, degreeType);

        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException("Persistence layer error", ex);
        }

        if ((studentCurricularPlans == null) || (studentCurricularPlans.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlans.iterator();
        List result = new ArrayList();

        while (iterator.hasNext()) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
            result
                    .add(InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan
                            .newInfoFromDomain(studentCurricularPlan));
        }

        return result;
    }
}