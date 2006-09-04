package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class EditExecutionCourse extends Service {

    public InfoExecutionCourse run(InfoExecutionCourseEditor infoExecutionCourse) throws FenixServiceException {
        
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        executionCourse.edit(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(),
                infoExecutionCourse.getTheoreticalHours(), infoExecutionCourse.getTheoPratHours(),
                infoExecutionCourse.getPraticalHours(), infoExecutionCourse.getLabHours(), infoExecutionCourse.getSeminaryHours(),
                infoExecutionCourse.getProblemsHours(), infoExecutionCourse.getFieldWorkHours(), infoExecutionCourse.getTrainingPeriodHours(),
                infoExecutionCourse.getTutorialOrientationHours(), infoExecutionCourse.getComment());

        return new InfoExecutionCourse(executionCourse);
    }

}
