package net.sourceforge.fenixedu.domain;

public enum SchoolLevelType {
    
    UNKNOWN,
    
    DONT_KNOW_HOW_TO_READ_OR_WRITE,
    
    KNOWS_HOW_TO_READ_WITHOUT_OLD_FOURTH_YEAR,
    
    FIRST_CYCLE_BASIC_SCHOOL,
    
    SECOND_CYCLE_BASIC_SCHOOL,
    
    THIRD_CYCLE_BASIC_SCHOOL,
    
    HIGH_SCHOOL_OR_EQUIVALENT,
    
    TECHNICAL_SPECIALIZATION,
    
    MEDIUM_EDUCATION,
    
    BACHELOR_DEGREE,
    
    DEGREE,
    
    MASTER_DEGREE,
    
    DOCTORATE_DEGREE;
    
    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return SchoolLevelType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return SchoolLevelType.class.getName() + "." + name();
    }
  

}
