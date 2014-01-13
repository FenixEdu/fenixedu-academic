package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.io.FileUtils;

import pt.ist.fenixframework.Atomic;

/**
 * @author naat
 */
public class CreateFileContent extends FileContentService {

    protected void run(Site site, Container container, File file, String originalFilename, String displayName,
            Group permittedGroup, Person person, EducationalResourceType type) throws DomainException, IOException {

        final byte[] bs = FileUtils.readFileToByteArray(file);

        checkSiteQuota(site, bs.length);

        FileContent fileContent = new FileContent(originalFilename, displayName, bs, permittedGroup, type);

        container.addFile(fileContent);
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
    public static void runCreateFileContent(Site site, Container container, File file, String originalFilename,
            String displayName, Group permittedGroup, Person person, EducationalResourceType type) throws FenixServiceException,
            DomainException, IOException, NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, container, file, originalFilename, displayName, permittedGroup, person, type);
    }

}