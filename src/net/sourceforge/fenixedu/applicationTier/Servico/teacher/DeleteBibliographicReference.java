package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteBibliographicReference extends Service {

    public boolean run(Integer bibliographicReferenceOID) throws FenixServiceException,
            ExcepcaoPersistencia {
        BibliographicReference bibliographicReference = (BibliographicReference) persistentObject
                .readByOID(BibliographicReference.class, bibliographicReferenceOID);

        if (bibliographicReference == null) {
            throw new InvalidArgumentsServiceException();
        }
        bibliographicReference.delete();
        persistentObject.deleteByOID(BibliographicReference.class,
                bibliographicReference.getIdInternal());

        return true;
    }
}