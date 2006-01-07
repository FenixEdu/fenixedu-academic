package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.precedences.InfoPrecedence;
import net.sourceforge.fenixedu.dataTransferObject.precedences.InfoPrecedenceWithRestrictions;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPrecedencesFromDegreeCurricularPlan implements IService {

	public Map run(Integer degreeCurricularPlanID) throws FenixServiceException, ExcepcaoPersistencia {

		Map finalListOfInfoPrecedences = new HashMap();

		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = persistentSuport
				.getIPersistentDegreeCurricularPlan();

		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) degreeCurricularPlanDAO
				.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

		List curricularCourses = degreeCurricularPlan.getCurricularCourses();

		int size = curricularCourses.size();

		for (int i = 0; i < size; i++) {
			CurricularCourse curricularCourse = (CurricularCourse) curricularCourses.get(i);
			List precedences = curricularCourse.getPrecedences();
			putInMap(finalListOfInfoPrecedences, curricularCourse, precedences);
		}

		return finalListOfInfoPrecedences;
	}

	private void putInMap(Map finalListOfInfoPrecedences, CurricularCourse curricularCourse,
			List precedences) {

		if (!precedences.isEmpty()) {
			InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
					.newInfoFromDomain(curricularCourse);

			List infoPrecedences = clone(precedences);

			finalListOfInfoPrecedences.put(infoCurricularCourse, infoPrecedences);
		}
	}

	private List clone(List precedences) {

		List result = new ArrayList();

		int size = precedences.size();

		for (int i = 0; i < size; i++) {
			Precedence precedence = (Precedence) precedences.get(i);
			InfoPrecedence infoPrecedence = InfoPrecedenceWithRestrictions.newInfoFromDomain(precedence);
			result.add(infoPrecedence);
		}

		return result;
	}
}