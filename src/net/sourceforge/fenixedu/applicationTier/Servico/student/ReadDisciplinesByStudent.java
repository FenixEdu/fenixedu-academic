package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Nortadas & Rui Figueiredo
 * 
 */

public class ReadDisciplinesByStudent implements IService {

	public Object run(Integer number, DegreeType degreeType) throws ExcepcaoPersistencia {
		List disciplines = new ArrayList();
		List courses = new ArrayList();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
				degreeType);

		if (student != null) {
			List frequencies = sp.getIFrequentaPersistente()
					.readByStudentNumberInCurrentExecutionPeriod(number);
			for (int i = 0; i < frequencies.size(); i++) {
				IAttends frequent = (IAttends) frequencies.get(i);
				IExecutionCourse executionCourse = frequent.getDisciplinaExecucao();

				disciplines.add(executionCourse);

			}
		}
		if (disciplines != null)
			for (int i = 0; i < disciplines.size(); i++) {
				IExecutionCourse executionCourse = (IExecutionCourse) disciplines.get(i);
				InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
						.newInfoFromDomain(executionCourse);
				courses.add(infoExecutionCourse);
			}

		return courses;

	}

}