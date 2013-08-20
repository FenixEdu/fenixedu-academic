package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Fernanda Quitério
 * 
 */
public class EditBibliographicReference {

    protected Boolean run(String bibliographicReferenceID, String newTitle, String newAuthors, String newReference,
            String newYear, Boolean optional) throws FenixServiceException {

        final BibliographicReference bibliographicReference = AbstractDomainObject.fromExternalId(bibliographicReferenceID);
        if (bibliographicReference == null) {
            throw new InvalidArgumentsServiceException();
        }
        bibliographicReference.edit(newTitle, newAuthors, newReference, newYear, optional);

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditBibliographicReference serviceInstance = new EditBibliographicReference();

    @Service
    public static Boolean runEditBibliographicReference(String bibliographicReferenceID, String newTitle, String newAuthors,
            String newReference, String newYear, Boolean optional) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter.instance.execute(bibliographicReferenceID);
        return serviceInstance.run(bibliographicReferenceID, newTitle, newAuthors, newReference, newYear, optional);
    }

}