/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 * @author Francisco Paulo 27/Out/04 frnp@mega.ist.utl.pt (edit)
 * 
 * ReadCoordinatedDegrees class, implements the service that given a teacher returns 
 * a list containing the degree curricular plans for that teacher.
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.DegreeCurricularPlanState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadCoordinatedDegrees implements IService {

    public ReadCoordinatedDegrees() {
    }

    public List run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

        List plans = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Read the Teacher

            ITeacher teacher = sp.getIPersistentTeacher()
                    .readTeacherByUsername(userView.getUtilizador());
            if (teacher == null) {
                throw new ExcepcaoInexistente("No Teachers Found !!");
            }
            plans = sp.getIPersistentCoordinator().readCurricularPlansByTeacher(teacher);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }
        if (plans == null) {
            throw new ExcepcaoInexistente("No Degrees Found !!");
        }
        Iterator iterator = plans.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();

            if (!degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE_OBJ)) {
                continue;
            }

            result.add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(degreeCurricularPlan));
        }

        return result;
    }
}