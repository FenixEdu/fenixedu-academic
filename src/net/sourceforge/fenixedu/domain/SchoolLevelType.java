package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public enum SchoolLevelType {

    UNKNOWN(false, false, false),

    DONT_KNOW_HOW_TO_READ_OR_WRITE(false, true, false),

    KNOWS_HOW_TO_READ_WITHOUT_OLD_FOURTH_YEAR(false, true, false),

    FIRST_CYCLE_BASIC_SCHOOL(true, true, false),

    SECOND_CYCLE_BASIC_SCHOOL(true, true, false),

    THIRD_CYCLE_BASIC_SCHOOL(true, true, false),

    HIGH_SCHOOL_OR_EQUIVALENT(true, true, false) {
	@Override
	public boolean isHighSchoolOrEquivalent() {
	    return true;
	}
    },

    TECHNICAL_SPECIALIZATION(true, true, false),

    MEDIUM_EDUCATION(false, true, false),

    BACHELOR_DEGREE(true, true, true) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    DEGREE(true, true, false) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    MASTER_DEGREE(true, true, true) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    DOCTORATE_DEGREE(true, true, true) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    OTHER(true, false, true),

    OTHER_SITUATION(false, true, false);

    private boolean forStudent;

    private boolean forStudentHousehold;

    private boolean forMobilityStudent;

    private SchoolLevelType(boolean forStudent, boolean forStudentHousehold, boolean forMobilityStudent) {
	this.forStudent = forStudent;
	this.forStudentHousehold = forStudentHousehold;
	this.forMobilityStudent = forMobilityStudent;
    }

    public boolean isHighSchoolOrEquivalent() {
	return false;
    }

    public boolean isHigherEducation() {
	return false;
    }

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return SchoolLevelType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return SchoolLevelType.class.getName() + "." + name();
    }

    public boolean isForStudent() {
	return forStudent;
    }

    public boolean isForStudentHousehold() {
	return forStudentHousehold;
    }

    public boolean isForMobilityStudent() {
	return forMobilityStudent;
    }

    static public List<SchoolLevelType> getTypesForStudent() {
	final List<SchoolLevelType> result = new ArrayList<SchoolLevelType>();

	for (final SchoolLevelType each : values()) {
	    if (each.isForStudent()) {
		result.add(each);
	    }
	}

	return result;

    }

    static public List<SchoolLevelType> getTypesForMobilityStudent() {
	final List<SchoolLevelType> result = new ArrayList<SchoolLevelType>();

	for (final SchoolLevelType each : values()) {
	    if (each.isForMobilityStudent()) {
		result.add(each);
	    }
	}

	return result;

    }

    static public List<SchoolLevelType> getTypesForStudentHousehold() {
	final List<SchoolLevelType> result = new ArrayList<SchoolLevelType>();

	for (final SchoolLevelType each : values()) {
	    if (each.isForStudentHousehold()) {
		result.add(each);
	    }
	}

	return result;
    }

    public String getLocalizedName() {
	return ResourceBundle.getBundle("resources.EnumerationResources").getString(getQualifiedName());
    }
}
