package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.FileContentService;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.Atomic;

public class CreateFileContentForBoard extends FileContentService {

    protected void run(AnnouncementBoard board, File file, String originalFilename, String displayName, Group permittedGroup,
            Person person) throws FenixServiceException, DomainException, IOException {

        if (!board.hasWriter(person)) {
            throw new FenixServiceException("error.person.not.board.writer");
        }

        if (StringUtils.isEmpty(displayName)) {
            displayName = file.getName();
        }

        final byte[] bs = FileUtils.readFileToByteArray(file);
        FileContent fileContent = new FileContent(originalFilename, displayName, bs, permittedGroup, null);

        board.addFile(fileContent);
    }

    // Service Invokers migrated from Berserk

    private static final CreateFileContentForBoard serviceInstance = new CreateFileContentForBoard();

    @Atomic
    public static void runCreateFileContentForBoard(AnnouncementBoard board, File file, String originalFilename,
            String displayName, Group permittedGroup, Person person) throws FenixServiceException, DomainException, IOException {
        serviceInstance.run(board, file, originalFilename, displayName, permittedGroup, person);
    }

}