package net.sourceforge.fenixedu.domain;

import java.util.ResourceBundle;

public enum RaidesEducationLevelType {

    HIGHER_DEGREE_WITHOUT_LEVEL("A"),

    BACHELOR("B"),

    BACHELOR_COMPLEMENT("BC"),

    BACHELOR_DEGREE("BL"),

    TECHNOLOGICAL_SPECIALIZATION_DEGREE("C"),

    TECHNOLOGICAL_SPECIALIZATION_DEGREE_DL88_2006("C0"),

    PRE_BOLONHA_DOCTORATE("D"),

    DOCTORATE("D3"),
    
    BASIC_CYCLE_DIPLOMA("DB"),

    BASIC_AND_SPECIAL_CYCLE_DIPLOMA("DD"),
    
    SPECIAL_CYCLE_DIPLOMA("DL"),
    
    EQUIVALENT_TO_BACHELOR("EB"),
    
    EQUIVALENT_TO_DEGREE("EL"),
    
    PRE_BOLONHA_DEGREE("L"),
    
    DEGREE("L1"),
    
    BACHELOR_AND_DEGREE("LB"),
    
    DEGREE_TERMINAL("LT"),
    
    PRE_BOLONHA_MASTER("M"),
    
    MASTER("M2"),
    
    HIGH_SCHOOL("MD"),

    INTEGRATED_MASTER("MI"),

    INTEGRATED_MASTER_TERMINAL("MT"),
    
    DEGREE_PREPARATION("P"),
    
    BACHELOR_AND_DEGREE_PREPARATION("PB"),
    
    INTEGRATED_MASTER_PREPARATION("PM");

    private String levelCode;

    private RaidesEducationLevelType(String levelCode) {
	this.levelCode = levelCode;
    }

    public String getLevelCode() {
	return levelCode;
    }

    public String getQualifiedName() {
	return SchoolLevelType.class.getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
	return ResourceBundle.getBundle("resources.EnumerationResources").getString(getQualifiedName());
    }
}
