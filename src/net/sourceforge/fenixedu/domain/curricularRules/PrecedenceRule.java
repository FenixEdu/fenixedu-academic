package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;


public abstract class PrecedenceRule extends PrecedenceRule_Base {
    
    public PrecedenceRule() {
        super();
    }

    @Override
    public boolean appliesToContext(Context context) {
        return (super.appliesToContext(context) && this.appliesToPeriod(context));
    }
    
    private boolean appliesToPeriod(Context context) {
        return (hasNoCurricularPeriodOrder()
                || (this.getCurricularPeriodType().equals(context.getCurricularPeriod().getPeriodType()) 
                        && this.getCurricularPeriodOrder().equals(context.getCurricularPeriod().getChildOrder())));
    }
    
    private boolean hasNoCurricularPeriodOrder() {
        return (this.getCurricularPeriodType() == null || this.getCurricularPeriodOrder() == null 
                || (this.getCurricularPeriodType().equals(CurricularPeriodType.SEMESTER) 
                        && this.getCurricularPeriodOrder().equals(0)));
    }

    @Override
    protected void removeOwnParameters() {
        removePrecedenceDegreeModule();
    }
}
