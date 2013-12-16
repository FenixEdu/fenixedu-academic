package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean.EducationalResourceType;
import pt.ist.fenixframework.Atomic;

public class CreateScormPackage extends CreateFileContent {

    @Override
    protected void run(Site site, Container container, File file, String originalFilename, String displayName,
            Group permittedGroup, Person person, EducationalResourceType type) throws DomainException, FenixServiceException,
            IOException {

        super.run(site, container, file, originalFilename, displayName, permittedGroup, person, type);
    }

    // Service Invokers migrated from Berserk

    private static final CreateScormPackage serviceInstance = new CreateScormPackage();

    @Atomic
    public static void runCreateScormPackage(Site site, Container container, File file, String originalFilename,
            String displayName, Group permittedGroup, Person person, EducationalResourceType type) throws DomainException,
            FenixServiceException, IOException, NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, container, file, originalFilename, displayName, permittedGroup, person, type);
    }

}