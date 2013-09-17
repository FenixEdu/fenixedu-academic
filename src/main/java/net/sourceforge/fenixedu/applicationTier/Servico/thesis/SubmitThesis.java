package net.sourceforge.fenixedu.applicationTier.Servico.thesis;


import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixframework.Atomic;

public class SubmitThesis {

    @Atomic
    public static void run(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis) {
        thesis.submit();
    }

}