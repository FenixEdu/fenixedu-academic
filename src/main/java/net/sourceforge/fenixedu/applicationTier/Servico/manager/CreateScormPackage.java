package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean.EducationalResourceType;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.FileSetType;
import pt.utl.ist.fenix.tools.file.IScormFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class CreateScormPackage extends CreateFileContent {

    @Override
    protected void run(Site site, Container container, File file, String originalFilename, String displayName,
            Group permittedGroup, Person person, EducationalResourceType type) throws DomainException, FenixServiceException,
            IOException {

        super.run(site, container, file, originalFilename, displayName, permittedGroup, person, type);
    }

    @Override
    protected FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean permission,
            Collection<FileSetMetaData> metaData, File file) throws FenixServiceException, IOException {
        final IScormFileManager fileManager = FileManagerFactory.getFactoryInstance().getScormFileManager();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return fileManager.saveScormFile(filePath, originalFilename, permission, metaData, is, FileSetType.PACKAGE_SCORM_1_2);
        } catch (FileNotFoundException e) {
            throw new FenixServiceException(e.getMessage(), e);
        } finally {
            is.close();
        }

    }

    // Service Invokers migrated from Berserk

    private static final CreateScormPackage serviceInstance = new CreateScormPackage();

    @Service
    public static void runCreateScormPackage(Site site, Container container, File file, String originalFilename,
            String displayName, Group permittedGroup, Person person, EducationalResourceType type) throws DomainException,
            FenixServiceException, IOException, NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        serviceInstance.run(site, container, file, originalFilename, displayName, permittedGroup, person, type);
    }

}