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
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadCoordinatedDegrees extends Service {

	public List run(IUserView userView) throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {
        // Read the Teacher

		Teacher teacher = Teacher.readTeacherByUsername(userView.getUtilizador());
		if (teacher == null) {
			throw new ExcepcaoInexistente("No Teachers Found !!");
		}
		
		List<DegreeCurricularPlan> plans = teacher.getCoordinatedDegreeCurricularPlans();

		if (plans == null) {
			throw new ExcepcaoInexistente("No Degrees Found !!");
		}
		Iterator iterator = plans.iterator();
		List result = new ArrayList();
		while (iterator.hasNext()) {
			DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) iterator.next();

			if (!degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
				continue;
			}

			result.add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(degreeCurricularPlan));
		}

		return result;
	}
}