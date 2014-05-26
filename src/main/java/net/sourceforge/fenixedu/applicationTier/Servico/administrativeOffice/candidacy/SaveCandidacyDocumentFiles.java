/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.CandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.Files;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SaveCandidacyDocumentFiles {

    @Atomic
    public static void run(List<CandidacyDocumentUploadBean> candidacyDocuments) {

        Group masterDegreeOfficeEmployeesGroup = RoleGroup.get(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        Group coordinatorsGroup = RoleGroup.get(RoleType.COORDINATOR);
        Group permittedGroup = masterDegreeOfficeEmployeesGroup.or(coordinatorsGroup);

        for (CandidacyDocumentUploadBean candidacyDocumentUploadBean : candidacyDocuments) {
            if (candidacyDocumentUploadBean.getTemporaryFile() != null) {

                String filename = candidacyDocumentUploadBean.getFilename();
                CandidacyDocument candidacyDocument = candidacyDocumentUploadBean.getCandidacyDocument();
                Candidacy candidacy = candidacyDocument.getCandidacy();
                Person person = candidacy.getPerson();

                final byte[] content = read(candidacyDocumentUploadBean.getTemporaryFile());

                if (candidacyDocument.getFile() != null) {
                    candidacyDocument.getFile().delete();
                }

                final CandidacyDocumentFile candidacyDocumentFile =
                        new CandidacyDocumentFile(filename, filename, content, permittedGroup.or(UserGroup.of(person.getUser())));
                candidacyDocument.setFile(candidacyDocumentFile);
            }
        }

    }

    private static byte[] read(final File file) {
        try {
            return Files.toByteArray(file);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}