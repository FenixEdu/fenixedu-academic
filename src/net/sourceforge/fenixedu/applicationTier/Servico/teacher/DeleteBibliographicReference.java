package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.BibliographicReference;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteBibliographicReference extends Service {

    public boolean run(Integer bibliographicReferenceOID) throws FenixServiceException {

	BibliographicReference bibliographicReference = rootDomainObject
		.readBibliographicReferenceByOID(bibliographicReferenceOID);
	if (bibliographicReference == null) {
	    throw new InvalidArgumentsServiceException();
	}

	bibliographicReference.delete();
	return true;
    }

}
