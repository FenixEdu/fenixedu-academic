package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateThesisProposalWithAssignment extends Service {

    public Thesis run(DegreeCurricularPlan degreeCurricularPlan, Student student, Proposal proposal) {
    	MultiLanguageString title = new MultiLanguageString(proposal.getTitle());

    	Thesis thesis = new Thesis(degreeCurricularPlan.getDegree(), student.getDissertationEnrolment(degreeCurricularPlan), title);
    	thesis.setOrientator(proposal.getOrientator());
    	thesis.setCoorientator(proposal.getCoorientator());
    	thesis.setOrientatorCreditsDistribution(proposal.getOrientatorsCreditsPercentage());
    	
        return thesis;
    }
    
}
