package net.sourceforge.fenixedu.domain;

public enum ProfessionalSituationType {
       
    //Legal Regimens
    PROVISIONS_ADMINISTRATIVE_CONTRACT(false, false),
    PROVISIONS_ADMINISTRATIVE_CONTRACT_SUSPENDED(false, false),
    TERM_CONTRACT(false, false),
    INDETERMINATE_TIME_CONTRACT(false, false),
    TEMPORARY_SUBSTITUTION_CONTRACT(false, false),
    PROVISORY_NOMINATION(false, false),
    DEFINITIVE_NOMINATION(false, false),
    SERVICE_COMMISSION_OVERTIME_NOMINATION(false, false),
    SERVICE_COMMISSION_NOMINATION(false, false),
    SERVICE_PROVISION(false, false),
    DETACHED_FROM(false, false),
    REQUESTED_FROM(false, false),
    
    //Functions Accumulation    
    FUNCTIONS_ACCUMULATION_WITH_LEADING_POSITIONS(false, false),
    
    //End Situations
    DEFINITIVE_NOMINATION_DECLINED(false, true),
    TERM_WORK_CONTRACT_END(false, true),
    CERTAIN_FORWARD_CONTRACT_END(false, true), 
    CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS(false, true),
    CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS(false, true),
    CERTAIN_FORWARD_CONTRACT_RESCISSION(false, true),        
    RETIREMENT(false, true),
    RETIREMENT_IN_PROGRESS(false, true),                   
    DEATH(false, true),
    CONTRACT_END(false, true),
    TEMPORARY_SUBSTITUTION_CONTRACT_END(false, true),
    DENUNCIATION(false, true),
    RESIGNATION(false, true),
    RESCISSION(false, true),
    IST_OUT_NOMINATION(false, true),
    SERVICE_TURN_OFF(false, true),
    EXONERATION(false, true),
    TRANSFERENCE(false, true),
    EMERITUS(false, true),
    LICENSE_WITHOUT_SALARY_LONG(false, true),      
    
    // Service Exemptions
    GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY(true, false),
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY(true, false),    
    SABBATICAL(true, false),    
    MEDICAL_SITUATION(true, false),    
    SPECIAL_LICENSE(true, false),    
    LICENSE_WITHOUT_SALARY_YEAR(true, false),
    MATERNAL_LICENSE(true, false),
    MATERNAL_LICENSE_WITH_SALARY_80PERCENT(true, false),
    CONTRACT_SUSPEND(true, false),
    CONTRACT_SUSPEND_ART_73_ECDU(true, false),
    SERVICE_COMMISSION_IST_OUT(true, false),
    SERVICE_COMMISSION(true, false),
    LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT(true, false),
    REQUESTED_FOR(true, false),
    LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE(true, false),    
    LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS(true, false),
    DANGER_MATERNAL_LICENSE(true, false),
    CHILDBIRTH_LICENSE(true, false),
    MILITAR_SITUATION(true, false),
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL(true, false),
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS(true, false),
    TEACHER_SERVICE_EXEMPTION_DL24_84_ART51_N6_EST_DISC(true, false),
    TEACHER_SERVICE_EXEMPTION_E_C_D_U(true, false),
    FUNCTIONS_MANAGEMENT_SERVICE_EXEMPTION(true, false),
    INCAPACITY_FOR_TOGETHER_DOCTOR_OF_THE_CGA(true, false),
    PUBLIC_MANAGER(true, false),
    DETACHED_TO(true, false),
    GOVERNMENT_MEMBER(true, false);   
    
    private boolean isServiceExemption;
    private boolean isEndSituation;
        
    private ProfessionalSituationType(boolean isServiceExemption, boolean endSituation) {
	this.isServiceExemption = isServiceExemption;
    }    
           
    public String getName() {
	return name();
    }

    public boolean isServiceExemption() {
        return isServiceExemption;
    } 
    
    public boolean isEndSituation() {
        return isEndSituation;
    } 
}
