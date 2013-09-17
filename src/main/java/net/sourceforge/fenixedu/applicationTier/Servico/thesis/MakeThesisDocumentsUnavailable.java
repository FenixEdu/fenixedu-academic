package net.sourceforge.fenixedu.applicationTier.Servico.thesis;


import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeScientificCommissionMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import pt.ist.fenixframework.Atomic;

public class MakeThesisDocumentsUnavailable {

    @Atomic
    public static void run(Thesis thesis) {
        final ThesisFile thesisFile = thesis.getDissertation();

        RoleTypeGroup scientificCouncil = new RoleTypeGroup(RoleType.SCIENTIFIC_COUNCIL);
        CurrentDegreeScientificCommissionMembersGroup commissionMembers =
                new CurrentDegreeScientificCommissionMembersGroup(thesis.getDegree());
        PersonGroup student = thesis.getStudent().getPerson().getPersonGroup();

        thesisFile.setPermittedGroup(new GroupUnion(scientificCouncil, commissionMembers, student));

        final net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication = thesis.getPublication();
        if (publication != null) {
            for (final ResearchResultDocumentFile researchResultDocumentFile : publication.getResultDocumentFilesSet()) {
                researchResultDocumentFile.setPermittedGroup(thesisFile.getPermittedGroup());
            }
        }
    }

}