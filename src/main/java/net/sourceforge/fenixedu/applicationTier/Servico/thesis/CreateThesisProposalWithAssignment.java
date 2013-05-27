package net.sourceforge.fenixedu.applicationTier.Servico.thesis;


import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateThesisProposalWithAssignment {

    protected Thesis run(DegreeCurricularPlan degreeCurricularPlan, Student student, Proposal proposal, Thesis previousThesis) {

        Integer orientatorsCreditsPercentage;
        Person coorientator;
        Person orientator;
        MultiLanguageString title;

        if (previousThesis != null) {
            orientatorsCreditsPercentage = previousThesis.getOrientatorCreditsDistribution();
            ThesisEvaluationParticipant coorientatorObj = previousThesis.getCoorientator();
            coorientator = coorientatorObj == null ? null : coorientatorObj.getPerson();

            ThesisEvaluationParticipant orientatorObj = previousThesis.getOrientator();
            orientator = orientatorObj == null ? null : previousThesis.getOrientator().getPerson();

            title = previousThesis.getTitle();
        } else {
            orientatorsCreditsPercentage = proposal.getOrientatorsCreditsPercentage();
            coorientator = proposal.getCoorientator();
            orientator = proposal.getOrientator();
            title = new MultiLanguageString(proposal.getTitle());
        }

        Thesis thesis =
                new Thesis(degreeCurricularPlan.getDegree(), student.getDissertationEnrolment(degreeCurricularPlan), title);

        thesis.setOrientator(orientator);
        thesis.setCoorientator(coorientator);
        thesis.setOrientatorCreditsDistribution(orientatorsCreditsPercentage);

        return thesis;
    }

    // Service Invokers migrated from Berserk

    private static final CreateThesisProposalWithAssignment serviceInstance = new CreateThesisProposalWithAssignment();

    @Service
    public static Thesis runCreateThesisProposalWithAssignment(DegreeCurricularPlan degreeCurricularPlan, Student student, Proposal proposal, Thesis previousThesis) {
        return serviceInstance.run(degreeCurricularPlan, student, proposal, previousThesis);
    }

}