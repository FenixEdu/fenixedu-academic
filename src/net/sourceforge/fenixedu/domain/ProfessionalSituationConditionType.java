package net.sourceforge.fenixedu.domain;

public enum ProfessionalSituationConditionType {

    UNKNOWN,

    WORKS_FOR_OTHERS,

    EMPLOYEER,

    INDEPENDENT_WORKER,

    WORKS_FOR_FAMILY_WITHOUT_PAYMENT,

    RETIRED,

    UNEMPLOYED,

    HOUSEWIFE,

    STUDENT,

    MILITARY_SERVICE,

    OTHER;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return ProfessionalSituationConditionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return ProfessionalSituationConditionType.class.getName() + "." + name();
    }
}
