package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan;

import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt) <br/>
 * 
 * Factory for Degree Curricular Plan related operations
 *  
 */

public interface IDegreeCurricularPlanStrategyFactory {

    /**
     * 
     * @param The
     *            Degree Curricular Plan
     * @return The Degree Curricular Plan Strategy
     */
    public IDegreeCurricularPlanStrategy getDegreeCurricularPlanStrategy(
            DegreeCurricularPlan degreeCurricularPlan);
}