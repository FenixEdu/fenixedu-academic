/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

/**
 * @author lmac1 modified by Fernanda Quitério
 */
public class InsertExecutionCourseAtExecutionPeriod extends Service {

    public void run(InfoExecutionCourseEditor infoExecutionCourse) throws FenixServiceException {

	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());
	if (executionPeriod == null) {
	    throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
	}

	final ExecutionCourse existentExecutionCourse = executionPeriod.getExecutionCourseByInitials(infoExecutionCourse.getSigla());
	if (existentExecutionCourse != null) {
	    throw new ExistingServiceException("A disciplina execução com sigla "
		    + existentExecutionCourse.getSigla() + " e período execução "
		    + executionPeriod.getName() + "-" + executionPeriod.getExecutionYear().getYear());
	}

	final ExecutionCourse executionCourse = new ExecutionCourse(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(), executionPeriod, infoExecutionCourse.getEntryPhase());
	executionCourse.createSite();
    }
}
