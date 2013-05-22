package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DeleteFileContentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.FileManagerException;

/**
 * @author naat
 */
public class DeleteFileContent extends FenixService {

    protected void run(FileContent fileContent) throws FenixServiceException, DomainException, FileManagerException {
        fileContent.delete();
    }
    // Service Invokers migrated from Berserk

    private static final DeleteFileContent serviceInstance = new DeleteFileContent();

    @Service
    public static void runDeleteFileContent(FileContent fileContent) throws FenixServiceException, DomainException, FileManagerException  , NotAuthorizedException {
        DeleteFileContentFilter.instance.execute(fileContent);
        serviceInstance.run(fileContent);
    }

}