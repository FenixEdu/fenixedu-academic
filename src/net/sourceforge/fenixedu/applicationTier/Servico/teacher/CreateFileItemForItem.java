package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.FileItemCreationBean.EducationalResourceType;
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

	public void run(Item item, InputStream inputStream, String originalFilename, String displayName,
			Group permittedGroup, Person person, EducationalResourceType type) throws FenixServiceException, ExcepcaoPersistencia, DomainException {

		ExecutionCourseSite site = (ExecutionCourseSite) item.getSection().getSite();

		final VirtualPath filePath = getVirtualPath(item);

		Collection<FileSetMetaData> metaData = createMetaData(person.getName(), displayName, site.getExecutionCourse().getNome(), type);
		
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

	private List<FileSetMetaData> createMetaData(String author, String title, String executionCourseName, EducationalResourceType educationalType) {
		List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
		metaData.add(FileSetMetaData.createAuthorMeta(author));
		metaData.add(FileSetMetaData.createAuthorMeta(executionCourseName));
		metaData.add(FileSetMetaData.createTitleMeta(title));
		 if(educationalType!=null) {
			metaData.add(new FileSetMetaData("type",null,null,educationalType.toString()));
		}
		return metaData;
	}
	// TODO: avoid depending on ExecutionCourseSite, use Site only
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

		ExecutionCourseSite site = (ExecutionCourseSite) section.getSite();
		final ExecutionCourse executionCourse = site.getExecutionCourse();
		filePath.addNode(0, new VirtualPathNode("EC" + executionCourse.getIdInternal(), executionCourse
				.getNome()));

		final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
		filePath.addNode(0, new VirtualPathNode("EP" + executionPeriod.getIdInternal(), executionPeriod
				.getName()));

		final ExecutionYear executionYear = executionPeriod.getExecutionYear();
		filePath.addNode(0,
				new VirtualPathNode("EY" + executionYear.getIdInternal(), executionYear.getYear()));

		filePath.addNode(0, new VirtualPathNode("Courses","Courses"));
		return filePath;
	}

}