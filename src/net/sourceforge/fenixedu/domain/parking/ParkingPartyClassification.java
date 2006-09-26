package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.degree.DegreeType;


public enum ParkingPartyClassification {
    TEACHER, 
    EMPLOYEE, 
    GRANT_OWNER, 
    MASTER_DEGREE,
    DEGREE,
    BOLONHA_SPECIALIZATION_DEGREE, 
    BOLONHA_ADVANCED_FORMATION_DIPLOMA,
    BOLONHA_ADVANCED_STUDIES_DIPLOMA,
    BOLONHA_MASTER_DEGREE, 
    BOLONHA_INTEGRATED_MASTER_DEGREE,
    BOLONHA_DEGREE, 
    PERSON,
    UNIT;

    public static ParkingPartyClassification getClassificationByDegreeType(DegreeType degreeType) {
        return valueOf(degreeType.name());
    }
}
