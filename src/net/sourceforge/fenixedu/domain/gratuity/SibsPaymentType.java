package net.sourceforge.fenixedu.domain.gratuity;

public enum SibsPaymentType {

    SPECIALIZATION_GRATUTITY_TOTAL,

    SPECIALIZATION_GRATUTITY_FIRST_PHASE,

    SPECIALIZATION_GRATUTITY_SECOND_PHASE,

    MASTER_DEGREE_GRATUTITY_TOTAL,

    MASTER_DEGREE_GRATUTITY_FIRST_PHASE,

    MASTER_DEGREE_GRATUTITY_SECOND_PHASE,

    INSURANCE;
    
    public String getName(){
        return name();
    }

}
