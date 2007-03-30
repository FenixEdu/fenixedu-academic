package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateThesisProposal extends Service {

    public Thesis run(DegreeCurricularPlan degreeCurricularPlan, Student student, MultiLanguageString title, String comment) {
        Thesis thesis = new Thesis(degreeCurricularPlan.getDegree(), student.getDissertationEnrolment(degreeCurricularPlan), title);

        thesis.setComment(comment);
        
        return thesis;
    }
    
}
