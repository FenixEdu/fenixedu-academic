package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.ScientificCommissionGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class MakeThesisDocumentsUnavailable {

    @Atomic
    public static void run(Thesis thesis) {
        final ThesisFile thesisFile = thesis.getDissertation();
        RoleType roleType = RoleType.SCIENTIFIC_COUNCIL;

        Group scientificCouncil = RoleGroup.get(roleType);
        ScientificCommissionGroup commissionMembers = ScientificCommissionGroup.get(thesis.getDegree());
        Group student = thesis.getStudent().getPerson().getPersonGroup();

        thesisFile.setPermittedGroup(scientificCouncil.or(commissionMembers).or(student));

        final net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication = thesis.getPublication();
        if (publication != null) {
            for (final ResearchResultDocumentFile researchResultDocumentFile : publication.getResultDocumentFilesSet()) {
                researchResultDocumentFile.setPermittedGroup(thesisFile.getPermittedGroup());
            }
        }
    }

}