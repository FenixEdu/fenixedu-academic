package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;

public class CreateGratuityPostingRuleBean extends CreatePostingRuleBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<DomainReference<DegreeCurricularPlan>> degreeCurricularPlans;

    public CreateGratuityPostingRuleBean() {
	super();
	this.degreeCurricularPlans = new ArrayList<DomainReference<DegreeCurricularPlan>>();
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final DomainReference<DegreeCurricularPlan> each : this.degreeCurricularPlans) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setDegreeCurricularPlans(List<DegreeCurricularPlan> variable) {
	final List<DomainReference<DegreeCurricularPlan>> result = new ArrayList<DomainReference<DegreeCurricularPlan>>();
	for (final DegreeCurricularPlan each : variable) {
	    result.add(new DomainReference<DegreeCurricularPlan>(each));
	}

	this.degreeCurricularPlans = result;
    }

}
