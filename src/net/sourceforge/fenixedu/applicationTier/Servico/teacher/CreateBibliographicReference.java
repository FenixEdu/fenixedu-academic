package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 */
public class CreateBibliographicReference extends Service {

    public boolean run(Integer infoExecutionCourseID, String newBibliographyTitle,
            String newBibliographyAuthors, String newBibliographyReference, String newBibliographyYear,
            Boolean newBibliographyOptional) throws FenixServiceException, ExcepcaoPersistencia {

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, infoExecutionCourseID);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        executionCourse.createBibliographicReference(newBibliographyTitle, newBibliographyAuthors,
                newBibliographyReference, newBibliographyYear, newBibliographyOptional);
        return true;
    }
}