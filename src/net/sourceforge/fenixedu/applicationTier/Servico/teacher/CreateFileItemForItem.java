package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.Node;

/**
 * 
 * @author naat
 * 
 */
public class CreateFileItemForItem extends FileItemService {

    public void run(Integer itemId, InputStream inputStream, String originalFilename,
            String displayName, FileItemPermittedGroupType fileItemPermittedGroupType)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException {

        final Item item = rootDomainObject.readItemByOID(itemId);
        final ExecutionCourse executionCourse = item.getSection().getSite().getExecutionCourse();
        final Group permittedGroup = createPermittedGroup(fileItemPermittedGroupType, executionCourse);
        final FilePath filePath = getFilePath(item);
        final FileMetadata fileMetadata = new FileMetadata(displayName, item.getSection().getSite()
                .getExecutionCourse().getNome());
        final IFileManager fileManager = FileManagerFactory.getFileManager();
        final FileDescriptor fileDescriptor = fileManager.saveFile(filePath, originalFilename,
                (permittedGroup != null) ? true : false, fileMetadata, inputStream);
        final FileItem fileItem = new FileItem(fileDescriptor.getFilename(), displayName, fileDescriptor
                .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(),
                fileDescriptor.getSize(), fileDescriptor.getUniqueId(), permittedGroup,
                fileItemPermittedGroupType);

        item.addFileItems(fileItem);

    }

    private FilePath getFilePath(Item item) {
        final FilePath filePath = new FilePath();
        filePath.addNode(new Node("I" + item.getIdInternal(), item.getName().getContent(Language.pt)));

        final Section section = item.getSection();
        filePath.addNode(0, new Node("S" + section.getIdInternal(), section.getName().getContent(Language.pt)));

        if (section.getSuperiorSection() != null) {
            Section superiorSection = section.getSuperiorSection();
            while (superiorSection != null) {
                filePath.addNode(0, new Node("S" + superiorSection.getIdInternal(), superiorSection
                        .getName().getContent(Language.pt)));

                superiorSection = superiorSection.getSuperiorSection();
            }
        }

        final ExecutionCourse executionCourse = section.getSite().getExecutionCourse();
        filePath.addNode(0, new Node("EC" + executionCourse.getIdInternal(), executionCourse.getNome()));

        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        filePath.addNode(0, new Node("EP" + executionPeriod.getIdInternal(), executionPeriod.getName()));

        final ExecutionYear executionYear = executionPeriod.getExecutionYear();
        filePath.addNode(0, new Node("EY" + executionYear.getIdInternal(), executionYear.getYear()));

        return filePath;
    }

}