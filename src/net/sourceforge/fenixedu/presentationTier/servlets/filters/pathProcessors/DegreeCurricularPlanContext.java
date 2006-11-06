package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

public interface DegreeCurricularPlanContext {

    public Degree getDegree();
    public DegreeCurricularPlan getDegreeCurricularPlan();
    
}
