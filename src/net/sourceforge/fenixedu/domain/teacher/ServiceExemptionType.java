/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.teacher;


/**
 * @author jpvl
 */
public enum ServiceExemptionType {
        
    GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY,
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY,
    SABBATICAL,    
    MEDICAL_SITUATION,    
    SPECIAL_LICENSE,    
    LICENSE_WITHOUT_SALARY_YEAR,
    LICENSE_WITHOUT_SALARY_LONG,
    MATERNAL_LICENSE,
    CONTRACT_SUSPEND,    
    SERVICE_COMMISSION_IST_OUT,
    SERVICE_COMMISSION,
    LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT,
    REQUESTED_FOR,
    LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE,
    GOVERNMENT_MEMBER;             
    
    
    public String getName() {
        return name();
    }
}