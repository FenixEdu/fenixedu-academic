/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTeachers implements IService {

	/**
	 * Executes the service. Returns the current collection of infoTeachers.
	 * @throws ExcepcaoPersistencia 
	 */

	public List run(Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {

		List professorShips = null;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		ExecutionCourse executionCourse = (ExecutionCourse) sp.getIPersistentExecutionCourse()
				.readByOID(ExecutionCourse.class, executionCourseId);
		professorShips = executionCourse.getProfessorships();

		if (professorShips == null || professorShips.isEmpty())
			return null;

		List infoTeachers = new ArrayList();
		Iterator iter = professorShips.iterator();
		Teacher teacher = null;

		while (iter.hasNext()) {
			teacher = ((Professorship) iter.next()).getTeacher();
			infoTeachers.add(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
		}

		return infoTeachers;
	}
}