package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.joda.time.DateTime;

public class EvaluateThesis extends Service {

    public void run(Integer degreeCurricularPlanId, Thesis thesis, String mark, DateTime discussion) {
        thesis.confirm(mark, discussion);
    }
    
}
