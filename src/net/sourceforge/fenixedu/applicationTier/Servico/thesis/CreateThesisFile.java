package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeScientificCommissionMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public abstract class CreateThesisFile extends Service {

	public ThesisFile run(Thesis thesis, File fileToUpload, String fileName, String title,
			String subTitle, Language language) throws FenixServiceException, IOException {

		if (!thesis.isWaitingConfirmation()) {
			throw new DomainException("thesis.files.submit.unavailable");
		}

		if (!thesis.isDeclarationAccepted()) {
			throw new DomainException("thesis.files.submit.unavailable");
		}

		removePreviousFile(thesis);

		if (fileToUpload == null || fileName == null) {
			return null;
		}

		VirtualPath filePath = getVirtualPath(thesis);
		Collection<FileSetMetaData> metaData = createMetaData(thesis, fileName);

		FileDescriptor descriptor = saveFile(filePath, fileName, true, metaData, fileToUpload);

		ThesisFile file = new ThesisFile(descriptor.getUniqueId(), fileName);
		file.setSize(descriptor.getSize());
		file.setMimeType(descriptor.getMimeType());
		file.setChecksum(descriptor.getChecksum());
		file.setChecksumAlgorithm(descriptor.getChecksumAlgorithm());

		RoleTypeGroup scientificCouncil = new RoleTypeGroup(RoleType.SCIENTIFIC_COUNCIL);
		CurrentDegreeScientificCommissionMembersGroup commissionMembers = new CurrentDegreeScientificCommissionMembersGroup(
				thesis.getDegree());
		PersonGroup student = thesis.getStudent().getPerson().getPersonGroup();
		ThesisFileReadersGroup thesisGroup = new ThesisFileReadersGroup(thesis);

		file.setPermittedGroup(new GroupUnion(scientificCouncil, commissionMembers, student, thesisGroup));

		updateThesis(thesis, file, title, subTitle, language);

		return file;
	}

	private VirtualPath getVirtualPath(Thesis thesis) {
		// TODO: thesis, review path

		VirtualPathNode[] nodes = { new VirtualPathNode("Thesis", "Thesis"),
				new VirtualPathNode("Student" + thesis.getStudent().getIdInternal(), "Student") };

		VirtualPath path = new VirtualPath();
		for (VirtualPathNode node : nodes) {
			path.addNode(node);
		}

		return path;
	}

	private Collection<FileSetMetaData> createMetaData(Thesis thesis, String fileName) {
		List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();

		metaData.add(FileSetMetaData.createAuthorMeta(thesis.getStudent().getPerson().getName()));
		metaData.add(FileSetMetaData.createTitleMeta(thesis.getTitle().getContent()));

		return metaData;
	}

	private FileDescriptor saveFile(VirtualPath filePath, String fileName, boolean isPrivate,
			Collection<FileSetMetaData> metaData, File file) throws FenixServiceException, IOException {
		IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return fileManager.saveFile(filePath, fileName, isPrivate, metaData, is);
		} catch (FileNotFoundException e) {
			throw new FenixServiceException(e.getMessage());
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	protected abstract void removePreviousFile(Thesis thesis);

	protected abstract void updateThesis(Thesis thesis, ThesisFile file, String title, String subTitle,
			Language language);

}
