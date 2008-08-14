package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum AcademicalInstitutionType {

    NATIONAL_PUBLIC_INSTITUTION, NATIONAL_PRIVATE_INSTITUTION, FOREIGN_INSTITUTION, OTHER_INSTITUTION;

    public String getName() {
	return name();
    }
}
