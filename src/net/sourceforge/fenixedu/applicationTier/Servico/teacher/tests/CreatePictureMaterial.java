package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.tests.NewPictureMaterial;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.domain.tests.PictureMaterialFile;

import org.apache.commons.io.FileUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreatePictureMaterial extends FenixService {

	@Service
	public static NewPictureMaterial run(Teacher teacher, NewTestElement testElement, Boolean inline, File mainFile,
			String originalFilename, String displayName) throws FenixServiceException, DomainException, IOException {

		final VirtualPath filePath = getVirtualPath();

		final Collection<FileSetMetaData> metadata = Collections.emptySet();
		final PictureMaterialFile pictureMaterialFile =
				new PictureMaterialFile(filePath, originalFilename, displayName, metadata,
						FileUtils.readFileToByteArray(mainFile), null);

		return new NewPictureMaterial(testElement, inline, pictureMaterialFile);

	}

	private static VirtualPath getVirtualPath() {
		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(new VirtualPathNode("tests", "Online tests"));
		filePath.addNode(new VirtualPathNode("materials", "Presentation materials"));
		filePath.addNode(new VirtualPathNode("pictures", "Pictures"));

		return filePath;
	}

}