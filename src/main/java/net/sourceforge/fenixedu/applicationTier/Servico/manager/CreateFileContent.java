package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

/**
 * @author naat
 */
public class CreateFileContent {

    protected void run(Site site, CmsContent container, byte[] bytes, String originalFilename, String displayName,
            Group permittedGroup, Person person, EducationalResourceType type) throws DomainException, IOException {

        checkSiteQuota(site, bytes.length);
        FileContent fileContent = new FileContent(originalFilename, displayName, bytes, permittedGroup, type);
        container.addFileContent(fileContent);
    }

    private void checkSiteQuota(Site site, int size) {
        if (site.hasQuota()) {
            if (site.getUsedQuota() + size > site.getQuota()) {
                throw new SiteFileQuotaExceededException(site, size);
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreateFileContent serviceInstance = new CreateFileContent();

    @Atomic
    public static void runCreateFileContent(Site site, CmsContent container, byte[] bytes, String originalFilename,
            String displayName, Group permittedGroup, Person person, EducationalResourceType type) throws FenixServiceException,
            DomainException, IOException, NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, container, bytes, originalFilename, displayName, permittedGroup, person, type);
    }

}