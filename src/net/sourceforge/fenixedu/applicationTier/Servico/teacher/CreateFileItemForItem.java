package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileItem;
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
 * @author naat
 */
public class CreateFileItemForItem extends FileItemService {

    public void run(Item item, InputStream inputStream, String originalFilename,
            String displayName, Group permittedGroup)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException {

        ExecutionCourseSite site = (ExecutionCourseSite) item.getSection().getSite();
        
        final FilePath filePath = getFilePath(item);
        final FileMetadata fileMetadata = new FileMetadata(displayName, site
                .getExecutionCourse().getNome());
        final IFileManager fileManager = FileManagerFactory.getFileManager();
        final FileDescriptor fileDescriptor = fileManager.saveFile(filePath, originalFilename,
                isPublic(permittedGroup), fileMetadata, inputStream);
        
        new FileItem(item, fileDescriptor.getFilename(), displayName, fileDescriptor.getMimeType(),
                fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor
                        .getSize(), fileDescriptor.getUniqueId(), permittedGroup);
    }

    // TODO: avoid depending on ExecutionCourseSite, use Site only
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

        ExecutionCourseSite site = (ExecutionCourseSite) section.getSite();
        final ExecutionCourse executionCourse = site.getExecutionCourse();
        filePath.addNode(0, new Node("EC" + executionCourse.getIdInternal(), executionCourse.getNome()));

        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        filePath.addNode(0, new Node("EP" + executionPeriod.getIdInternal(), executionPeriod.getName()));

        final ExecutionYear executionYear = executionPeriod.getExecutionYear();
        filePath.addNode(0, new Node("EY" + executionYear.getIdInternal(), executionYear.getYear()));

        return filePath;
    }

}