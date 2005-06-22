package net.sourceforge.fenixedu.util;

public enum PrintAllCandidatesFilter {

	INVALID_FILTER,
	FILTERBY_SPECIALIZATION_VALUE,
	FILTERBY_SITUATION_VALUE,
	FILTERBY_GIVESCLASSES_VALUE,
	FILTERBY_DOESNTGIVESCLASSES_VALUE;
    
    public String getName() {
        return name();
    }
}