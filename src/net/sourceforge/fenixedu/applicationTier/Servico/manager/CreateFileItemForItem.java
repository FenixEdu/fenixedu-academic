package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

/**
 * @author naat
 */
public class CreateFileItemForItem extends FileItemService {

	public void run(Site site, Item item, InputStream inputStream, String originalFilename, String displayName,
			Group permittedGroup, Person person, EducationalResourceType type) throws FenixServiceException, ExcepcaoPersistencia, DomainException {

		final VirtualPath filePath = getVirtualPath(item);

		Collection<FileSetMetaData> metaData = createMetaData(person.getName(), displayName, site.getAuthorName(), type);
		
		final FileDescriptor fileDescriptor = saveFile(filePath, originalFilename, !isPublic(permittedGroup),
				metaData, inputStream);

		new FileItem(item, fileDescriptor.getFilename(), displayName, fileDescriptor.getMimeType(),
				fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(),
				fileDescriptor.getSize(), fileDescriptor.getUniqueId(), permittedGroup);
	}

	protected FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean permission,
			Collection<FileSetMetaData> metaData, InputStream inputStream) {
		final IFileManager fileManager = FileManagerFactory.getFileManager();
		return fileManager.saveFile(filePath, originalFilename, permission, metaData, inputStream);
	}

	private List<FileSetMetaData> createMetaData(String author, String title, String siteAuthorName, EducationalResourceType educationalType) {
		List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
		metaData.add(FileSetMetaData.createAuthorMeta(author));
        
        if (siteAuthorName != null) {
            metaData.add(FileSetMetaData.createAuthorMeta(siteAuthorName));
        }
        
		metaData.add(FileSetMetaData.createTitleMeta(title));
		 if(educationalType!=null) {
			metaData.add(new FileSetMetaData("type",null,null,educationalType.toString()));
		}
		return metaData;
	}
    
	private VirtualPath getVirtualPath(Item item) {
		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(new VirtualPathNode("I" + item.getIdInternal(), item.getName().getContent()));

		final Section section = item.getSection();
		filePath.addNode(0,
				new VirtualPathNode("S" + section.getIdInternal(), section.getName().getContent()));

		if (section.getSuperiorSection() != null) {
			Section superiorSection = section.getSuperiorSection();
			while (superiorSection != null) {
				filePath.addNode(0, new VirtualPathNode("S" + superiorSection.getIdInternal(),
						superiorSection.getName().getContent()));

				superiorSection = superiorSection.getSuperiorSection();
			}
		}

        Site site = section.getSite();
		String authorName = site.getAuthorName();
        filePath.addNode(0, new VirtualPathNode("Site" + site.getIdInternal(), authorName == null ? "Site" + site.getIdInternal() : authorName));

        ExecutionPeriod executionPeriod = site.getExecutionPeriod();
        if (executionPeriod == null) {
            filePath.addNode(0, new VirtualPathNode("Intemporal", "Intemporal"));
        }
        else {
            filePath.addNode(0, new VirtualPathNode("EP" + executionPeriod.getIdInternal(), executionPeriod
                    .getName()));
    
            ExecutionYear executionYear = executionPeriod.getExecutionYear();
            filePath.addNode(0,
                    new VirtualPathNode("EY" + executionYear.getIdInternal(), executionYear.getYear()));
        }
        
        filePath.addNode(0, new VirtualPathNode("Courses", "Courses"));
		return filePath;
	}

}