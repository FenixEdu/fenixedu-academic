package net.sourceforge.fenixedu.domain.vigilancy;

public enum UnavailableTypes {

    ALREADY_CONVOKED_FOR_ANOTHER_EVALUATION, UNAVAILABLE_PERIOD, INCOMPATIBLE_PERSON, NOT_AVAILABLE_ON_CAMPUS, SERVICE_EXEMPTION, LESSON_AT_SAME_TIME, UNKNOWN;

    public String getName() {
        return name();
    }
}
