package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class CreateBibliographicReference implements IService {

    /**
     * The constructor of this class.
     */
    public CreateBibliographicReference() {
    }

    /**
     * Executes the service.
     */
    public boolean run(Integer infoExecutionCourseCode, String newBibliographyTitle,
            String newBibliographyAuthors, String newBibliographyReference, String newBibliographyYear,
            Boolean newBibliographyOptional) throws FenixServiceException {

        try {

            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            IPersistentBibliographicReference persistentBibliographicReference = persistentSupport
                    .getIPersistentBibliographicReference();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);

            IBibliographicReference newBibliographicReference = new BibliographicReference();

            persistentBibliographicReference.simpleLockWrite(newBibliographicReference);
            newBibliographicReference.setExecutionCourse(executionCourse);
            newBibliographicReference.setTitle(newBibliographyTitle);
            newBibliographicReference.setAuthors(newBibliographyAuthors);
            newBibliographicReference.setReference(newBibliographyReference);
            newBibliographicReference.setYear(newBibliographyYear);
            newBibliographicReference.setOptional(newBibliographyOptional);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}