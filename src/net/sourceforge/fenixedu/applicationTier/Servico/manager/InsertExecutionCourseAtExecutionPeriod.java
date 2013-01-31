/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1 modified by Fernanda Quitério
 */
public class InsertExecutionCourseAtExecutionPeriod extends FenixService {

	@Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
	@Service
	public static void run(InfoExecutionCourseEditor infoExecutionCourse) throws FenixServiceException {

		final ExecutionSemester executionSemester =
				rootDomainObject.readExecutionSemesterByOID(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());
		if (executionSemester == null) {
			throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
		}

		final ExecutionCourse existentExecutionCourse =
				executionSemester.getExecutionCourseByInitials(infoExecutionCourse.getSigla());
		if (existentExecutionCourse != null) {
			throw new ExistingServiceException("A disciplina execução com sigla " + existentExecutionCourse.getSigla()
					+ " e período execução " + executionSemester.getName() + "-" + executionSemester.getExecutionYear().getYear());
		}

		final ExecutionCourse executionCourse =
				new ExecutionCourse(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(), executionSemester,
						infoExecutionCourse.getEntryPhase());
		executionCourse.createSite();
	}
}