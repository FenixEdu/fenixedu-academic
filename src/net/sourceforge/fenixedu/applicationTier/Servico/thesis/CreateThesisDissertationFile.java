package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.io.FileUtils;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CreateThesisDissertationFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
	ThesisFile dissertation = thesis.getDissertation();
	if (dissertation != null) {
	    if (AccessControl.getUserView().hasRoleType(RoleType.SCIENTIFIC_COUNCIL)) {
		dissertation.deleteWithoutStateCheck();
	    } else {
		dissertation.delete();
	    }
	}
    }

    @Override
    protected void updateThesis(Thesis thesis, ThesisFile file, String title, String subTitle, Language language,
	    String fileName, File fileToUpload) throws FenixServiceException, IOException {
	if (title == null) {
	    throw new DomainException("thesis.files.dissertation.title.required");
	}

	file.setTitle(title);
	file.setSubTitle(subTitle == null ? "" : subTitle);
	file.setLanguage(language);

	thesis.setDissertation(file);

	final net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication = thesis.getPublication();
	if (publication != null) {
	    final ResearchResultDocumentFile researchResultDocumentFile = publication.getResultDocumentFilesIterator().next();

	    final FileResultPermittedGroupType permittedGroupType = researchResultDocumentFile.getFileResultPermittedGroupType();
	    final Group permittedGroup = researchResultDocumentFile.getPermittedGroup();

	    researchResultDocumentFile.delete();

	    Collection<FileSetMetaData> metaData = createMetaData(thesis, fileName);
	    VirtualPath filePath = getVirtualPath(thesis);
	    byte[] content = FileUtils.readFileToByteArray(fileToUpload);

	    publication.addDocumentFile(filePath, metaData, content, fileName, file.getDisplayName(), permittedGroupType, permittedGroup);
	    publication.setThesis(thesis);

	}
    }

}
