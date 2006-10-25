package net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitServiceAgreementTemplate extends UnitServiceAgreementTemplate_Base {

    private UnitServiceAgreementTemplate() {
	super();
    }

    public UnitServiceAgreementTemplate(final Unit unit) {
	this();
	init(unit);
    }

    private void init(Unit unit) {
	checkParameters(unit);
	super.setUnit(unit);

    }

    private void checkParameters(Unit unit) {
	if (unit == null) {
	    throw new DomainException(
		    "error.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate.unit.cannot.be.null");
	}
    }

    @Override
    public void setUnit(Unit unit) {
	throw new DomainException(
		"error.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate.cannot.modify.unit");
    }

}
