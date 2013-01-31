package net.sourceforge.fenixedu.domain.phd.debts;

public enum PhdEventExemptionJustificationType {

	DIRECTIVE_COUNCIL_AUTHORIZATION, PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION, FINE_EXEMPTION;
	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return getClass().getSimpleName() + "." + name();
	}

}
