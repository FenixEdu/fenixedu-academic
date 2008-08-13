package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean.EducationalResourceType;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

/**
 * @author naat
 */
public class CreateFileContent extends FileContentService {

    public void run(Site site, Container container, File file, String originalFilename, String displayName, Group permittedGroup,
	    Person person, EducationalResourceType type) throws FenixServiceException, DomainException,
	    IOException {

	final VirtualPath filePath = getVirtualPath(site,container);

	Collection<FileSetMetaData> metaData = createMetaData(person.getName(), displayName, site.getAuthorName(), type);

	final FileDescriptor fileDescriptor = saveFile(filePath, originalFilename, !isPublic(permittedGroup), metaData, file);

	checkSiteQuota(site, fileDescriptor.getSize());
	
	FileContent fileContent = new FileContent(fileDescriptor.getFilename(), pt.utl.ist.fenix.tools.util.FileUtils.getFilenameOnly(displayName),
		fileDescriptor.getMimeType(), fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor
			.getSize(), fileDescriptor.getUniqueId(), permittedGroup);
	
	container.addFile(fileContent);
    }

    private void checkSiteQuota(Site site, int size) {
	if (site.hasQuota()) {
	    if (site.getUsedQuota() + size > site.getQuota()) {
		throw new SiteFileQuotaExceededException(site, size);
	    }
	}
    }

    private List<FileSetMetaData> createMetaData(String author, String title, String siteAuthorName,
	    EducationalResourceType educationalType) {
	List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
	metaData.add(FileSetMetaData.createAuthorMeta(author));

	if (siteAuthorName != null) {
	    metaData.add(FileSetMetaData.createAuthorMeta(siteAuthorName));
	}

	metaData.add(FileSetMetaData.createTitleMeta(title));
	if (educationalType != null) {
	    metaData.add(new FileSetMetaData("type", null, null, educationalType.toString()));
	}
	return metaData;
    }

    private VirtualPath getVirtualPath(Site site, Container container) {
	
	List<Content> contents = site.getPathTo(container);
	
	final VirtualPath filePath = new VirtualPath();

	for (Content content : contents.subList(1, contents.size())) {
	    filePath.addNode(0,new VirtualPathNode(content.getClass().getSimpleName().substring(0, 1) + content.getIdInternal(), content.getName().getContent()));
	}

	String authorName = site.getAuthorName();
	filePath.addNode(0,new VirtualPathNode("Site" + site.getIdInternal(), authorName == null ? "Site" + site.getIdInternal()
		: authorName));

	ExecutionSemester executionSemester = site.getExecutionPeriod();
	if (executionSemester == null) {
	    filePath.addNode(0, new VirtualPathNode("Intemporal", "Intemporal"));
	} else {
	    filePath.addNode(0, new VirtualPathNode("EP" + executionSemester.getIdInternal(), executionSemester.getName()));

	    ExecutionYear executionYear = executionSemester.getExecutionYear();
	    filePath.addNode(0, new VirtualPathNode("EY" + executionYear.getIdInternal(), executionYear.getYear()));
	}

	filePath.addNode(0, new VirtualPathNode("Courses", "Courses"));
	return filePath;
    }

}