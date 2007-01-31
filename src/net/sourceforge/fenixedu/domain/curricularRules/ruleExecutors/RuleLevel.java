package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

public enum RuleLevel {
    
    WITH_RULES,
    NO_RULES;
    
    public String getName() {
	return name();
    }
    
    static public RuleLevel defaultLevel() {
	return WITH_RULES;
    }
}
