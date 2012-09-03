package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

public enum AcademicEventJustificationType {

    DIRECTIVE_COUNCIL_AUTHORIZATION, INSTITUTION, MILITARY_SCOPE, MOBILITY_SCOPE;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

}
