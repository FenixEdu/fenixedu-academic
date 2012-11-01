package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.io.FileUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateUnitFile extends FenixService {

    private static byte[] read(final File file) {
	try {
	    return FileUtils.readFileToByteArray(file);
	} catch (IOException e) {
	    throw new Error(e);
	}
    }

    @Service
    public static void run(java.io.File file, String originalFilename, String displayName, String description, String tags,
	    Group permittedGroup, Unit unit, Person person) throws FenixServiceException {

	final Collection<FileSetMetaData> metaData = Collections.emptySet();
	final byte[] content = read(file);
	new UnitFile(unit, person, description, tags, getVirtualPath(unit), originalFilename, displayName, metaData, content,
		!isPublic(permittedGroup) ? new GroupUnion(permittedGroup, new PersonGroup(person)) : permittedGroup);
    }

    private static VirtualPath getVirtualPath(Unit unit) {

	final VirtualPath filePath = new VirtualPath();

	filePath.addNode(new VirtualPathNode("Research", "Research"));
	filePath.addNode(new VirtualPathNode("ResearchUnit" + unit.getIdInternal(), unit.getName()));

	return filePath;
    }

    private static boolean isPublic(Group permittedGroup) {
	if (permittedGroup == null) {
	    return true;
	}

	if (permittedGroup instanceof EveryoneGroup) {
	    return true;
	}

	return false;
    }
}