package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateUnitFile extends Service {

	public void run(java.io.File file, String originalFilename, String displayName, String description,
			String tags, Group permittedGroup, Unit unit, Person person) throws FenixServiceException {

		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new FenixServiceException(e.getMessage());
		}
		
		final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance()
				.getSimpleFileManager().saveFile(getVirtualPath(unit), originalFilename,
						!isPublic(permittedGroup), person.getName(), displayName, is);

		new UnitFile(unit, person, description, tags, fileDescriptor.getFilename(), pt.utl.ist.fenix.tools.util.FileUtils
				.getFilenameOnly(displayName), fileDescriptor.getMimeType(), fileDescriptor
				.getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
				fileDescriptor.getUniqueId(), !isPublic(permittedGroup) ? new GroupUnion(permittedGroup, new PersonGroup(person)) : permittedGroup);
	}

	private VirtualPath getVirtualPath(Unit unit) {

		final VirtualPath filePath = new VirtualPath();

		filePath.addNode(new VirtualPathNode("Research", "Research"));
		filePath.addNode(new VirtualPathNode("ResearchUnit" + unit.getIdInternal(), unit.getName()));

		return filePath;
	}

	private boolean isPublic(Group permittedGroup) {
		if (permittedGroup == null) {
			return true;
		}

		if (permittedGroup instanceof EveryoneGroup) {
			return true;
		}

		return false;
	}
}
