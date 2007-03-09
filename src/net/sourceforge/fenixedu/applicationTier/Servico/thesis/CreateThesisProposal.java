package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateThesisProposal extends Service {

    public Thesis run(Integer degreeCurricularPlanId, Student student, MultiLanguageString title, String comment) {
        // TODO: find thesis enrolment for student
        // bean.getStudent().getThesisEnrolment();
        DegreeCurricularPlan dcp = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        Thesis thesis = new Thesis(dcp.getDegree(), null);
        
        thesis.setTitle(title);
        thesis.setComment(comment);
        
        return thesis;
    }
    
}
