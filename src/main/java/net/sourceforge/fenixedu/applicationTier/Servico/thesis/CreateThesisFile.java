package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.io.IOException;
import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeScientificCommissionMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

import org.fenixedu.bennu.core.security.Authenticate;

public abstract class CreateThesisFile {

    public ThesisFile run(Thesis thesis, byte[] bytes, String fileName, String title, String subTitle, Locale language)
            throws FenixServiceException, IOException {

        if (!thesis.isWaitingConfirmation() && !Authenticate.getUser().getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
            throw new DomainException("thesis.files.submit.unavailable");
        }

        if (!thesis.isDeclarationAccepted() && !Authenticate.getUser().getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
            throw new DomainException("thesis.files.submit.unavailable");
        }

        removePreviousFile(thesis);

        if (bytes == null || fileName == null) {
            return null;
        }

        RoleTypeGroup scientificCouncil = new RoleTypeGroup(RoleType.SCIENTIFIC_COUNCIL);
        CurrentDegreeScientificCommissionMembersGroup commissionMembers =
                new CurrentDegreeScientificCommissionMembersGroup(thesis.getDegree());
        PersonGroup student = thesis.getStudent().getPerson().getPersonGroup();
        ThesisFileReadersGroup thesisGroup = new ThesisFileReadersGroup(thesis);
        final GroupUnion permittedGroup = new GroupUnion(scientificCouncil, commissionMembers, student, thesisGroup);

        ThesisFile file = new ThesisFile(fileName, fileName, bytes, permittedGroup);

        updateThesis(thesis, file, title, subTitle, language, fileName, bytes);

        return file;
    }

    protected abstract void removePreviousFile(Thesis thesis);

    protected abstract void updateThesis(Thesis thesis, ThesisFile file, String title, String subTitle, Locale language,
            String fileName, byte[] bytes) throws FenixServiceException, IOException;

}
