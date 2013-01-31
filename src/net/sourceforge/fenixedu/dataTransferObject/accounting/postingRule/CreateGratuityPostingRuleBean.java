package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

public class CreateGratuityPostingRuleBean extends CreatePostingRuleBean {

	private static final long serialVersionUID = 1L;

	private List<DegreeCurricularPlan> degreeCurricularPlans;

	public CreateGratuityPostingRuleBean() {
		super();
		this.degreeCurricularPlans = new ArrayList<DegreeCurricularPlan>();
	}

	public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
		final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
		for (final DegreeCurricularPlan each : this.degreeCurricularPlans) {
			result.add(each);
		}

		return result;
	}

	public void setDegreeCurricularPlans(List<DegreeCurricularPlan> variable) {
		final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
		for (final DegreeCurricularPlan each : variable) {
			result.add(each);
		}

		this.degreeCurricularPlans = result;
	}

}
