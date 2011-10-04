package net.sourceforge.fenixedu.domain.accounting.events;

public enum InsuranceExemptionJustificationType {
    // Directive council authorization dispatch
    DIRECTIVE_COUNCIL_AUTHORIZATION, MIT_AGREEMENT;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return AdministrativeOfficeFeeAndInsuranceExemptionJustificationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return AdministrativeOfficeFeeAndInsuranceExemptionJustificationType.class.getName() + "." + name();
    }

}
