/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.CandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.io.FileUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SaveCandidacyDocumentFiles extends FenixService {

	@Service
	public static void run(List<CandidacyDocumentUploadBean> candidacyDocuments) {

		Group masterDegreeOfficeEmployeesGroup =
				new RoleGroup(Role.getRoleByRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE));
		Group coordinatorsGroup = new RoleGroup(Role.getRoleByRoleType(RoleType.COORDINATOR));
		Group permittedGroup = new GroupUnion(masterDegreeOfficeEmployeesGroup, coordinatorsGroup);

		for (CandidacyDocumentUploadBean candidacyDocumentUploadBean : candidacyDocuments) {
			if (candidacyDocumentUploadBean.getTemporaryFile() != null) {

				String filename = candidacyDocumentUploadBean.getFilename();
				CandidacyDocument candidacyDocument = candidacyDocumentUploadBean.getCandidacyDocument();
				Candidacy candidacy = candidacyDocument.getCandidacy();
				Person person = candidacy.getPerson();

				final Collection<FileSetMetaData> metadata = Collections.emptySet();
				final byte[] content = read(candidacyDocumentUploadBean.getTemporaryFile());

				if (candidacyDocument.getFile() != null) {
					candidacyDocument.getFile().delete();
				}

				final CandidacyDocumentFile candidacyDocumentFile =
						new CandidacyDocumentFile(getVirtualPath(candidacy), filename, filename, metadata, content,
								new GroupUnion(permittedGroup, new PersonGroup(person)));
				candidacyDocument.setFile(candidacyDocumentFile);
			}
		}

	}

	private static byte[] read(final File file) {
		try {
			return FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	private static VirtualPath getVirtualPath(Candidacy candidacy) {
		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(new VirtualPathNode("Candidacies", "Candidacies"));
		filePath.addNode(new VirtualPathNode("CANDIDACY" + candidacy.getNumber(), candidacy.getNumber().toString()));
		return filePath;
	}

}