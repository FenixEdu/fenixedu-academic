package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.InputStream;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.FileSetType;
import pt.utl.ist.fenix.tools.file.IScormFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class CreateScormPackageForItem extends CreateFileItemForItem {

    public void run(Site site, Item item, InputStream inputStream,
            String originalFilename, String displayName, Group permittedGroup,
            Person person, EducationalResourceType type)
            throws DomainException, FenixServiceException, ExcepcaoPersistencia {

        super.run(site, item, inputStream, originalFilename, displayName,
                permittedGroup, person, type);
    }

    @Override
    protected FileDescriptor saveFile(VirtualPath filePath,
            String originalFilename, boolean permission,
            Collection<FileSetMetaData> metaData, InputStream inputStream) {
        final IScormFileManager fileManager = FileManagerFactory.getFactoryInstance().getScormFileManager();

        return fileManager.saveScormFile(filePath, originalFilename,
                permission, metaData, inputStream,
                FileSetType.PACKAGE_SCORM_1_2);

    }

}
