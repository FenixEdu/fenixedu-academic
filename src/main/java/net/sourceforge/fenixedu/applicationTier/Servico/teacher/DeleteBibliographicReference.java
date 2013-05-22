package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteBibliographicReference extends FenixService {

    protected Boolean run(Integer bibliographicReferenceOID) throws FenixServiceException {

        BibliographicReference bibliographicReference =
                rootDomainObject.readBibliographicReferenceByOID(bibliographicReferenceOID);
        if (bibliographicReference == null) {
            throw new InvalidArgumentsServiceException();
        }

        bibliographicReference.delete();
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteBibliographicReference serviceInstance = new DeleteBibliographicReference();

    @Service
    public static Boolean runDeleteBibliographicReference(Integer bibliographicReferenceOID) throws FenixServiceException  , NotAuthorizedException {
        ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter.instance.execute(bibliographicReferenceOID);
        return serviceInstance.run(bibliographicReferenceOID);
    }

}