package net.sourceforge.fenixedu.domain;

public enum ProfessionType {

    UNKNOWN,

    PUBLIC_ADMINISTRATION_BOARD_OR_DIRECTOR_AND_BOARD_OF_COMPANIES,

    CIENTIFIC_AND_INTELECTUAL_PROFESSION_SPECIALIST,

    INTERMEDIATE_LEVEL_TECHNICALS_AND_PROFESSIONALS,

    ADMINISTRATIVE_STAFF_AND_SIMMILAR,

    SALES_AND_SERVICE_STAFF,

    FARMERS_AND_AGRICULTURE_AND_FISHING_QUALIFIED_WORKERS,

    WORKERS_CRAFTSMEN_AND_SIMMILAR,

    INSTALLATION_AND_MACHINE_WORKERS_AND_LINE_ASSEMBLY_WORKERS,

    NON_QUALIFIED_WORKERS,

    MILITARY_MEMBER,

    OTHER;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return ProfessionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return ProfessionType.class.getName() + "." + name();
    }

}
