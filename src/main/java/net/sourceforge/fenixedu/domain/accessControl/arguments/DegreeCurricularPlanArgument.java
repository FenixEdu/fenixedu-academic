package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class DegreeCurricularPlanArgument extends DomainObjectArgumentParser<DegreeCurricularPlan> {
    @Override
    public Class<DegreeCurricularPlan> type() {
        return DegreeCurricularPlan.class;
    }
}
