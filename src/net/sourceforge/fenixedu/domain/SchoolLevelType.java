package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public enum SchoolLevelType {

    UNKNOWN(false, false),

    DONT_KNOW_HOW_TO_READ_OR_WRITE(false, true),

    KNOWS_HOW_TO_READ_WITHOUT_OLD_FOURTH_YEAR(false, true),

    FIRST_CYCLE_BASIC_SCHOOL(true, true),

    SECOND_CYCLE_BASIC_SCHOOL(true, true),

    THIRD_CYCLE_BASIC_SCHOOL(true, true),

    HIGH_SCHOOL_OR_EQUIVALENT(true, true) {
	@Override
	public boolean isHighSchoolOrEquivalent() {
	    return true;
	}
    },

    TECHNICAL_SPECIALIZATION(true, true),

    MEDIUM_EDUCATION(false, true),

    BACHELOR_DEGREE(true, true) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    DEGREE(true, true) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    MASTER_DEGREE(true, true) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    DOCTORATE_DEGREE(true, true) {
	@Override
	public boolean isHigherEducation() {
	    return true;
	}
    },

    OTHER(true, false),

    OTHER_SITUATION(false, true);

    private boolean forStudent;

    private boolean forStudentHousehould;

    private SchoolLevelType(boolean forStudent, boolean forStudentHousehold) {
	this.forStudent = forStudent;
	this.forStudentHousehould = forStudentHousehold;
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
	return this.forStudent;
    }

    public boolean isForStudentHousehold() {
	return this.forStudentHousehould;
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
