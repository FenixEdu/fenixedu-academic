package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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
public class DeleteBibliographicReference implements IServico {

    private static DeleteBibliographicReference service = new DeleteBibliographicReference();

    public static DeleteBibliographicReference getService() {
        return service;
    }

    private DeleteBibliographicReference() {
    }

    public final String getNome() {
        return "DeleteBibliographicReference";
    }

    public boolean run(Integer infoExecutionCourseCode, Integer bibliographicReferenceCode)
            throws FenixServiceException {

        try {
            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentBibliographicReference persistentBibliographicReference = persistentSupport
                    .getIPersistentBibliographicReference();

            IBibliographicReference ibibliographicReference = (IBibliographicReference) persistentBibliographicReference
                    .readByOID(BibliographicReference.class, bibliographicReferenceCode);

            if (ibibliographicReference != null) {
                persistentBibliographicReference.delete(ibibliographicReference);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return true;
    }
}