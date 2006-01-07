package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
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
		Student student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
				degreeType);
		if (student != null) {
			StudentCurricularPlan StudentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
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