package net.sourceforge.fenixedu.domain;

public enum FormationType {
    
    POST_BOLONHA_DEGREE_EQUIVALENT_FORMATION,
    PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION,
    NON_DEGREE_ADVANCED_FORMATION,
    PROFESSIONAL_FORMATION;
    
    public String getName() {
        return name();
    }  
}
