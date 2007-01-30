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
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreatePictureMaterial extends Service {

	public NewPictureMaterial run(Teacher teacher, NewTestElement testElement, Boolean inline,
			InputStream inputStream, String originalFilename, String displayName)
			throws FenixServiceException, ExcepcaoPersistencia, DomainException {

		final IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
		final VirtualPath filePath = getVirtualPath();
		
		final FileDescriptor fileDescriptor = fileManager.saveFile(filePath, originalFilename, false,
				teacher.getPerson().getName(), displayName == null ? originalFilename : displayName, inputStream);
		final PictureMaterialFile pictureMaterialFile = new PictureMaterialFile(fileDescriptor
				.getFilename(), displayName, fileDescriptor.getMimeType(), fileDescriptor.getChecksum(),
				fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor
						.getUniqueId(), null);

		return new NewPictureMaterial(testElement, inline, pictureMaterialFile);

	}

	private VirtualPath getVirtualPath() {
		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(new VirtualPathNode("tests", "Online tests"));
		filePath.addNode(new VirtualPathNode("materials", "Presentation materials"));
		filePath.addNode(new VirtualPathNode("pictures", "Pictures"));

		return filePath;
	}

}