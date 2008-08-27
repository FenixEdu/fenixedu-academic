package net.sourceforge.fenixedu.domain.gratuity;

import java.util.ArrayList;
import java.util.List;

public enum ExemptionGratuityType {

    INSTITUTION,

    INSTITUTION_GRANT_OWNER,

    OTHER_INSTITUTION,

    UNIVERSITY_TEACHER,

    POLYTECHNICAL_TEACHER,

    PALOP_TEACHER,

    STUDENT_TEACH,

    FCT_GRANT_OWNER,

    MILITARY_SON,

    OTHER;

    public static List percentageOfExemption() {
	List percentage = new ArrayList();

	percentage.add(Integer.valueOf(25));
	percentage.add(Integer.valueOf(50));
	percentage.add(Integer.valueOf(75));
	percentage.add(Integer.valueOf(100));

	return percentage;
    }

    public String getName() {
	return name();
    }

}
