package net.sourceforge.fenixedu.domain;

public enum ProfessionalSituationType {
       
    //Legal Regimens
    PROVISIONS_ADMINISTRATIVE_CONTRACT(false),
    PROVISIONS_ADMINISTRATIVE_CONTRACT_SUSPENDED(false),
    TERM_CONTRACT(false),
    INDETERMINATE_TIME_CONTRACT(false),
    TEMPORARY_SUBSTITUTION(false),
    PROVISORY_NOMINATION(false),
    DEFINITIVE_NOMINATION(false),
    REFUSED_DEFINITIVE_NOMINATION(false),
    COMMISSION_OVERTIME_NOMINATION(false),    
    SERVICE_PROVISION(false),
    DETACHED_FROM(false),
    REQUESTED_FROM(false),
    
    //Functions Accumulation    
    FUNCTIONS_ACCUMULATION_WITH_LEADING_POSITIONS(false),
    
    //End Situations
    TERM_WORK_CONTRACT_END(false),
    CERTAIN_FORWARD_CONTRACT_END(false), 
    CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS(false),
    CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS(false),
    CERTAIN_FORWARD_CONTRACT_RESCISSION(false),        
    RETIREMENT(false),
    RETIREMENT_IN_PROGRESS(false),                   
    DEATH(false),
    CONTRACT_END(false),
    TEMPORARY_SUBSTITUTION_CONTRACT_END(false),
    DENUNCIATION(false),
    RESIGNATION(false),
    RESCISSION(false),
    IST_OUT_NOMINATION(false),
    SERVICE_TURN_OFF(false),
    EXONERATION(false),
    TRANSFERENCE(false),
    EMERITUS(false),
    
    // Service Exemptions
    GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY(true),
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY(true),    
    SABBATICAL(true),    
    MEDICAL_SITUATION(true),    
    SPECIAL_LICENSE(true),    
    LICENSE_WITHOUT_SALARY_YEAR(true),
    LICENSE_WITHOUT_SALARY_LONG(true),      
    MATERNAL_LICENSE(true),
    MATERNAL_LICENSE_WITH_SALARY_80PERCENT(true),
    CONTRACT_SUSPEND(true),
    CONTRACT_SUSPEND_ART_73_ECDU(true),
    SERVICE_COMMISSION_IST_OUT(true),
    SERVICE_COMMISSION(true),
    LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT(true),
    REQUESTED_FOR(true),
    LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE(true),    
    LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS(true),
    DANGER_MATERNAL_LICENSE(true),
    CHILDBIRTH_LICENSE(true),
    MILITAR_SITUATION(true),
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL(true),
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS(true),
    TEACHER_SERVICE_EXEMPTION_DL24_84_ART51_N6_EST_DISC(true),
    TEACHER_SERVICE_EXEMPTION_E_C_D_U(true),
    FUNCTIONS_MANAGEMENT_SERVICE_EXEMPTION(true),
    INCAPACITY_FOR_TOGETHER_DOCTOR_OF_THE_CGA(true),
    PUBLIC_MANAGER(true),
    DETACHED_TO(true),
    GOVERNMENT_MEMBER(true);   
    
    private boolean isServiceExemption;
        
    private ProfessionalSituationType(boolean isServiceExemption) {
	this.isServiceExemption = isServiceExemption;
    }    
           
    public String getName() {
	return name();
    }

    public boolean isServiceExemption() {
        return isServiceExemption;
    }    
}
