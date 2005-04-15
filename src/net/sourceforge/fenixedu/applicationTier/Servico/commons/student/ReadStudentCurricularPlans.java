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

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentCurricularPlans implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentCurricularPlans() {
    }

    public List run(Integer studentNumber, TipoCurso degreeType) throws ExcepcaoInexistente,
            FenixServiceException {
        ISuportePersistente sp = null;

        List studentCurricularPlans = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            studentCurricularPlans = sp.getIStudentCurricularPlanPersistente()
                    .readByStudentNumberAndDegreeType(studentNumber, degreeType);

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

        // FIXME: There's a problem with data of the Graduation Students
        //        For now only Master Degree Students can view their Curriculum

        while (iterator.hasNext()) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();

            result.add(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                    .newInfoFromDomain(studentCurricularPlan));
        }

        if (result.size() == 0) {
            throw new NonExistingServiceException();
        }

        return result;
    }
}