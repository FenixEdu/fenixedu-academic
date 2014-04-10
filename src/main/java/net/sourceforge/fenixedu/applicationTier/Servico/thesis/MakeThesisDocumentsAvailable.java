package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.ScientificCommissionGroup;
import net.sourceforge.fenixedu.domain.accessControl.ThesisReadersGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class MakeThesisDocumentsAvailable {

    @Atomic
    public static void run(Thesis thesis) {
        final ThesisFile thesisFile = thesis.getDissertation();
        RoleType roleType = RoleType.SCIENTIFIC_COUNCIL;

        Group scientificCouncil = RoleGroup.get(roleType);
        Group commissionMembers = ScientificCommissionGroup.get(thesis.getDegree());
        Group student = thesis.getStudent().getPerson().getPersonGroup();
        Group thesisGroup = ThesisReadersGroup.get(thesis);

        thesisFile.setPermittedGroup(scientificCouncil.or(commissionMembers).or(student).or(thesisGroup));

        final net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication = thesis.getPublication();
        if (publication != null) {
            for (final File file : publication.getResultDocumentFilesSet()) {
                file.setPermittedGroup(thesisFile.getPermittedGroup());
            }
        }
    }

}