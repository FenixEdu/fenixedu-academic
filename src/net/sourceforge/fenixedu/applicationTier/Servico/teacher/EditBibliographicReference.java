package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author Fernanda Quitério
 *  
 */
public class EditBibliographicReference implements IServico {
    private static EditBibliographicReference service = new EditBibliographicReference();

    /**
     * The singleton access method of this class.
     */
    public static EditBibliographicReference getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private EditBibliographicReference() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "EditBibliographicReference";
    }

    /**
     * Executes the service.
     */
    public boolean run(Integer infoExecutionCourseCode, Integer bibliographicReferenceCode,
            String newTitle, String newAuthors, String newReference, String newYear, Boolean optional)
            throws FenixServiceException {

        IBibliographicReference ibibliographicReference = null;
        try {

            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentBibliographicReference persistentBibliographicReference = persistentSupport
                    .getIPersistentBibliographicReference();

            ibibliographicReference = (IBibliographicReference) persistentBibliographicReference
                    .readByOID(BibliographicReference.class, bibliographicReferenceCode, true);

            if (ibibliographicReference == null) {
                throw new InvalidArgumentsServiceException();
            }

            ibibliographicReference.setTitle(newTitle);
            ibibliographicReference.setAuthors(newAuthors);
            ibibliographicReference.setReference(newReference);
            ibibliographicReference.setYear(newYear);
            ibibliographicReference.setOptional(optional);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return true;
    }
}