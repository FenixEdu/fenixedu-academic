package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.FileManagerException;

/**
 * Changes the group of people that is allowed to access the file.
 * 
 * @author naat
 */
public class EditFilePermissions extends FileContentService {

    protected void run(Site site, FileContent fileContent, Group group) throws FenixServiceException, DomainException,
            FileManagerException {
        fileContent.setPermittedGroup(group);
    }

    // Service Invokers migrated from Berserk

    private static final EditFilePermissions serviceInstance = new EditFilePermissions();

    @Service
    public static void runEditFilePermissions(Site site, FileContent fileContent, Group group) throws FenixServiceException,
            DomainException, FileManagerException, NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, fileContent, group);
    }
}