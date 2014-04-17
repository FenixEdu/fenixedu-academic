package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.io.IOException;
import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis.ScientificCouncilOrStudentThesisAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class CreateThesisDissertationFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
        ThesisFile dissertation = thesis.getDissertation();
        if (dissertation != null) {
            if (Authenticate.getUser().getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                dissertation.deleteWithoutStateCheck();
            } else {
                dissertation.delete();
            }
        }
    }

    @Override
    protected void updateThesis(Thesis thesis, ThesisFile file, String title, String subTitle, Locale language, String fileName,
            byte[] bytes) throws FenixServiceException, IOException {
        if (title == null) {
            throw new DomainException("thesis.files.dissertation.title.required");
        }

        file.setTitle(title);
        file.setSubTitle(subTitle == null ? "" : subTitle);
        file.setLanguage(language);

        thesis.setDissertation(file);

        final net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication = thesis.getPublication();
        if (publication != null) {
            final ResearchResultDocumentFile researchResultDocumentFile =
                    publication.getResultDocumentFilesSet().iterator().next();

            final FileResultPermittedGroupType permittedGroupType = researchResultDocumentFile.getFileResultPermittedGroupType();
            final Group permittedGroup = researchResultDocumentFile.getPermittedGroup();

            researchResultDocumentFile.delete();

            publication.addDocumentFile(bytes, fileName, file.getDisplayName(), permittedGroupType, permittedGroup);
            publication.setThesis(thesis);

        }
    }

    // Service Invokers migrated from Berserk

    private static final CreateThesisDissertationFile serviceInstance = new CreateThesisDissertationFile();

    @Atomic
    public static ThesisFile runCreateThesisDissertationFile(Thesis thesis, byte[] bytes, String fileName, String title,
            String subTitle, Locale language) throws FenixServiceException, IOException {
        ScientificCouncilOrStudentThesisAuthorizationFilter.instance.execute(thesis);
        return serviceInstance.run(thesis, bytes, fileName, title, subTitle, language);
    }

}