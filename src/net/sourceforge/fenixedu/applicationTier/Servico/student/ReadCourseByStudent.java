package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Nortadas & Rui Figueiredo
 * 
 */

public class ReadCourseByStudent implements IService {

	public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {
		InfoDegree infoDegree = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
				degreeType);
		if (student != null) {
			IStudentCurricularPlan StudentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
					.readActiveStudentCurricularPlan(number, degreeType);
			if (StudentCurricularPlan != null) {
				infoDegree = new InfoDegree(StudentCurricularPlan.getDegreeCurricularPlan().getDegree()
						.getSigla(), StudentCurricularPlan.getDegreeCurricularPlan().getDegree()
						.getNome());
			}

		}

		return infoDegree;

	}

}