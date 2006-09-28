package net.sourceforge.fenixedu.domain.curricularRules;

public class RuleResult {
    
    private Boolean result;
    
    public RuleResult(Boolean result) {
	this.result = result;
    }
    
    public Boolean getResult() {
	return this.result;
    }
    
    public RuleResult and(RuleResult ruleResult) {
	return new RuleResult(this.getResult() && ruleResult.getResult());
    }
    
    public RuleResult or(RuleResult ruleResult) {
	return new RuleResult(this.getResult() || ruleResult.getResult());
    }

    public RuleResult not() {
	return new RuleResult(!this.getResult());
    }
}
