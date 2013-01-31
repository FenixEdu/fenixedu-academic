package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.FileContentService;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateFileContentForBoard extends FileContentService {

	public void run(AnnouncementBoard board, File file, String originalFilename, String displayName, Group permittedGroup,
			Person person) throws FenixServiceException, DomainException, IOException {

		if (!board.hasWriter(person)) {
			throw new FenixServiceException("error.person.not.board.writer");
		}

		if (StringUtils.isEmpty(displayName)) {
			displayName = file.getName();
		}

		final VirtualPath filePath = getVirtualPath(board);

		Collection<FileSetMetaData> metaData = createMetaData(person.getName(), displayName);

		final byte[] bs = FileUtils.readFileToByteArray(file);
		FileContent fileContent = new FileContent(filePath, originalFilename, displayName, metaData, bs, permittedGroup);

		board.addFile(fileContent);
	}

	private List<FileSetMetaData> createMetaData(String author, String title) {
		List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
		metaData.add(FileSetMetaData.createAuthorMeta(author));
		metaData.add(FileSetMetaData.createTitleMeta(title));
		return metaData;
	}

	private VirtualPath getVirtualPath(AnnouncementBoard board) {

		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(0, new VirtualPathNode("B" + board.getIdInternal(), board.getName().getContent()));
		filePath.addNode(0, new VirtualPathNode("Announcements", "Announcements"));
		return filePath;
	}
}
