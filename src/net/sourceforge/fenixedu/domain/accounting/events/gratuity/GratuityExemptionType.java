package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;

public enum GratuityExemptionType {

    // Teachers,Researchers,Employees and Agents from institution
    INSTITUTION,

    // Institution or Institution Partners Grant Owners
    INSTITUTION_GRANT_OWNER,

    // Teachers,Researcher and Employees from Institution Partners
    OTHER_INSTITUTION,

    // PALOP Teachers
    PALOP_TEACHER,

    SON_OF_DECORATED_MILITARY,

    SOCIAL_SHARE_GRANT_OWNER,

    // Students teaching classes
    STUDENT_TEACH;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return GratuityExemptionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return GratuityExemptionType.class.getName() + "." + name();
    }

    public static List<GratuityExemptionType> getExemptionsFor(final DegreeType degreeType) {
	switch (degreeType) {
	case BOLONHA_MASTER_DEGREE:
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	case BOLONHA_DEGREE:
	case DEGREE:
	    return Arrays.asList(new GratuityExemptionType[] { SON_OF_DECORATED_MILITARY,
		    SOCIAL_SHARE_GRANT_OWNER });
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return Arrays.asList(new GratuityExemptionType[] { INSTITUTION, INSTITUTION_GRANT_OWNER,
		    OTHER_INSTITUTION, PALOP_TEACHER, STUDENT_TEACH });

	default:
	    throw new RuntimeException("Unknown degree type");
	}

    }
}
