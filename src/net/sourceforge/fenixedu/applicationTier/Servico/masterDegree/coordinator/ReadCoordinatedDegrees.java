/**
 * ReadCoordinatedDegrees class, implements the service that given a teacher returns 
 * a list containing the degree curricular plans for that teacher.
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;

public class ReadCoordinatedDegrees extends Service {

	public List run(IUserView userView) throws FenixServiceException {
		Teacher teacher = Teacher.readTeacherByUsername(userView.getUtilizador());
		if (teacher == null) {
			throw new InvalidArgumentsServiceException();
		}
		
		List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>();
		for (DegreeCurricularPlan degreeCurricularPlan : teacher.getCoordinatedActiveDegreeCurricularPlans()) {
			result.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
		}

		return result;
	}
    
}
