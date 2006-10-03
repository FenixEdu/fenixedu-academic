package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.tests.NewPictureMaterial;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.domain.tests.PictureMaterialFile;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.Node;

public class CreatePictureMaterial extends Service {

	public NewPictureMaterial run(Teacher teacher, NewTestElement testElement, Boolean inline,
			InputStream inputStream, String originalFilename, String displayName)
			throws FenixServiceException, ExcepcaoPersistencia, DomainException {

		final IFileManager fileManager = FileManagerFactory.getFileManager();
		final FilePath filePath = getFilePath();
		final FileMetadata fileMetadata = new FileMetadata(displayName == null ? originalFilename : displayName, teacher.getPerson().getName());
		final FileDescriptor fileDescriptor = fileManager.saveFile(filePath, originalFilename, false,
				fileMetadata, inputStream);
		final PictureMaterialFile pictureMaterialFile = new PictureMaterialFile(fileDescriptor
				.getFilename(), displayName, fileDescriptor.getMimeType(), fileDescriptor.getChecksum(),
				fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor
						.getUniqueId(), null);

		return new NewPictureMaterial(testElement, inline, pictureMaterialFile);

	}

	private FilePath getFilePath() {
		final FilePath filePath = new FilePath();
		filePath.addNode(new Node("tests", "Online tests"));
		filePath.addNode(new Node("materials", "Presentation materials"));
		filePath.addNode(new Node("pictures", "Pictures"));

		return filePath;
	}

}