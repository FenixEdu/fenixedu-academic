package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class EditExecutionCourseInfo extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static InfoExecutionCourse run(InfoExecutionCourseEditor infoExecutionCourse) throws InvalidArgumentsServiceException {

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
	if (executionCourse == null) {
	    throw new InvalidArgumentsServiceException();
	}

	executionCourse.editInformation(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(), infoExecutionCourse
		.getComment(), infoExecutionCourse.getAvailableGradeSubmission(), infoExecutionCourse.getEntryPhase());

	return new InfoExecutionCourse(executionCourse);
    }
}