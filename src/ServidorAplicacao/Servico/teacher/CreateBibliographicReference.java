package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.BibliographicReference;
import Dominio.ExecutionCourse;
import Dominio.IBibliographicReference;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
    public boolean run(Integer infoExecutionCourseCode,
            String newBibliographyTitle, String newBibliographyAuthors,
            String newBibliographyReference, String newBibliographyYear,
            Boolean newBibliographyOptional) throws FenixServiceException {

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            IPersistentBibliographicReference persistentBibliographicReference = persistentSupport
                    .getIPersistentBibliographicReference();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, infoExecutionCourseCode);

            IBibliographicReference newBibliographicReference = new BibliographicReference();

            persistentBibliographicReference
                    .simpleLockWrite(newBibliographicReference);
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