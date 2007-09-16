package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.DegreeType;


public enum PartyClassification {    
    TEACHER, 
    EMPLOYEE,
    RESEARCHER,
    GRANT_OWNER, 
    MASTER_DEGREE,
    DEGREE,
    BOLONHA_SPECIALIZATION_DEGREE, 
    BOLONHA_ADVANCED_FORMATION_DIPLOMA,
    BOLONHA_MASTER_DEGREE, 
    BOLONHA_INTEGRATED_MASTER_DEGREE,
    BOLONHA_PHD_PROGRAM,
    BOLONHA_DEGREE, 
    PERSON,
    UNIT;

    public static PartyClassification getClassificationByDegreeType(DegreeType degreeType) {
        return valueOf(degreeType.name());
    }
}
