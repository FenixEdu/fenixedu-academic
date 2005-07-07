package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class CreateBibliographicReference implements IService {

    public boolean run(Integer infoExecutionCourseID, String newBibliographyTitle,
            String newBibliographyAuthors, String newBibliographyReference, String newBibliographyYear,
            Boolean newBibliographyOptional) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, infoExecutionCourseID);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        persistentExecutionCourse.simpleLockWrite(executionCourse);
        executionCourse.createBibliographicReference(newBibliographyTitle, newBibliographyAuthors,
                newBibliographyReference, newBibliographyYear, newBibliographyOptional);
        return true;
    }
}