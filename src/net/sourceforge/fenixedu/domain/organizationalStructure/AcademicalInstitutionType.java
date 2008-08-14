package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Arrays;
import java.util.List;

public enum AcademicalInstitutionType {

    NATIONAL_PUBLIC_INSTITUTION,

    NATIONAL_PRIVATE_INSTITUTION,

    FOREIGN_INSTITUTION,

    OTHER_INSTITUTION,

    PUBLIC_HIGH_SCHOOL,

    PRIVATE_HIGH_SCHOOL,

    PRIVATE_AND_PUBLIC_HIGH_SCHOOL;

    public String getName() {
	return name();
    }

    static public List<AcademicalInstitutionType> getHighSchoolTypes() {
	return Arrays.asList(PUBLIC_HIGH_SCHOOL, PRIVATE_HIGH_SCHOOL, PRIVATE_AND_PUBLIC_HIGH_SCHOOL);
    }

}
