package net.sourceforge.fenixedu.domain;

import java.util.ResourceBundle;

public enum ProfessionalSituationConditionType {

    //TODO: RAIDES Provider and beans exclude this value.
    //This enum should be refactored to contain an "isActive"
    UNKNOWN,

    WORKS_FOR_OTHERS,

    EMPLOYEER,

    INDEPENDENT_WORKER,

    WORKS_FOR_FAMILY_WITHOUT_PAYMENT,

    RETIRED,

    UNEMPLOYED,

    HOUSEWIFE,

    STUDENT,

    //TODO: RAIDES Provider and beans exclude this value.
    //This enum should be refactored to contain an "isActive"
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

    public String getLocalizedName() {
        return ResourceBundle.getBundle("resources.EnumerationResources").getString(getQualifiedName());
    }

}
