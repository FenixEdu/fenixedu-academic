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
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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