package net.sourceforge.fenixedu.domain.phd.debts;

public enum PhdEventExemptionJustificationType {

    DIRECTIVE_COUNCIL_AUTHORIZATION;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

}
