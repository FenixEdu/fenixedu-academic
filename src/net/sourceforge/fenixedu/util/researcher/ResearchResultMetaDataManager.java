package net.sourceforge.fenixedu.util.researcher;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultTeacher;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.util.Month;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.tools.ant.filters.StringInputStream;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IContentFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class ResearchResultMetaDataManager {

	private static IContentFileManager fileManager = FileManagerFactory.getFactoryInstance()
			.getContentFileManager();

	public static Collection<FileSetMetaData> createMetaData(ResearchResult researchResult) {
		ArrayList<FileSetMetaData> metadata = new ArrayList<FileSetMetaData>();

		Set<Person> authors = new HashSet<Person>();

		for (ResultParticipation participation : researchResult.getOrderedResultParticipations()) {
			authors.add(participation.getPerson());
		}
		for (ResultTeacher participation : researchResult.getResultTeachers()) {
			authors.add(participation.getTeacher().getPerson());
		}

		for (Person person : authors) {
			metadata.add(FileSetMetaData.createAuthorMeta(StringNormalizer.normalize(person.getName())));
		}

		Set<Unit> units = new HashSet<Unit>();
		for (ResultUnitAssociation association : researchResult.getResultUnitAssociationsSet()) {
			units.add(association.getUnit());
		}

		for (Unit unit : units) {
			metadata.add(new FileSetMetaData("unit", null, null, StringNormalizer.normalize(unit.getName())));
		}

		metadata.add(FileSetMetaData.createTitleMeta(StringNormalizer.normalize(researchResult.getTitle())));
		metadata.add(new FileSetMetaData("creator", null, null, StringNormalizer.normalize(researchResult
				.getCreator().getName())));

		MultiLanguageString note = researchResult.getNote();
		if (note != null) {
			for (String abstractInAGivenLanguage : note.getAllContents()) {
				if (abstractInAGivenLanguage != null && abstractInAGivenLanguage.length() > 0)
					metadata.add(new FileSetMetaData("description", "abstract", null,
							abstractInAGivenLanguage));
			}
		}

		if (researchResult instanceof ResearchResultPublication) {
			ResearchResultPublication publication = (ResearchResultPublication) researchResult;
			Unit publisher = publication.getPublisher();
			if (publisher != null) {
				metadata.add(new FileSetMetaData("publisher", null, null, StringNormalizer
						.normalize(publisher.getName())));
			}
			Integer year = publication.getYear();
			if (year != null) {
				Month month = publication.getMonth();
				metadata.add(new FileSetMetaData("date", "issued", null, publication.getYear()
						+ ((month != null) ? "-" + month.ordinal() + 1 : "")));
			}
			String type[] = publication.getOjbConcreteClass().split("\\.");
			metadata.add(new FileSetMetaData("type", null, null, type[type.length - 1]));

			for (String keyword : publication.getKeywordsList()) {
				metadata.add(new FileSetMetaData("subject", null, null, keyword));
			}
		}

		return metadata;
	}

	public static void updateMetaDataInStorageFor(ResearchResult publication) {
		fileManager.changeItemMetaData(publication.getUniqueStorageId(), createMetaData(publication));
	}

	public static ResearchResult addDefaultDocument(ResearchResult result) {
		InputStream inputStream;

		inputStream = new StringInputStream("For index purpose only!");
		FileDescriptor fileDescriptor = fileManager.createItemWithFile(getVirtualPath(result), "default.txt",
				false, ResearchResultMetaDataManager.createMetaData(result), inputStream);

		result.setUniqueStorageId(fileDescriptor.getItemStorageId());

		final Group permittedGroup = ResearchResultDocumentFile
				.getPermittedGroup(FileResultPermittedGroupType.PUBLIC);

		result.addDocumentFile(fileDescriptor.getFilename(), "default.txt",
				FileResultPermittedGroupType.PUBLIC, fileDescriptor.getMimeType(), fileDescriptor
						.getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
				fileDescriptor.getUniqueId(), permittedGroup, Boolean.FALSE);

		return result;
	}

	public static VirtualPath getVirtualPath(ResearchResult result) {
		final VirtualPath filePath = new VirtualPath();

		filePath.addNode(new VirtualPathNode("Research", "Research"));
		filePath.addNode(new VirtualPathNode("Results", "Results"));

		if (result instanceof ResearchResultPublication) {
			filePath.addNode(new VirtualPathNode("Publications", "Publications"));
			filePath.addNode(new VirtualPathNode("pub" + result.getIdInternal(), "pub"
					+ result.getIdInternal().toString()));
		}
		if (result instanceof ResearchResultPatent) {
			filePath.addNode(new VirtualPathNode("Patents", "Patents"));
			filePath.addNode(new VirtualPathNode("pat" + result.getIdInternal(), "pat"
					+ result.getIdInternal().toString()));
		}

		return filePath;
	}
}
