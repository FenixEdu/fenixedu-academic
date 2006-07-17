package net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitServiceAgreementTemplate extends UnitServiceAgreementTemplate_Base {

    private UnitServiceAgreementTemplate() {
        super();
    }

    public UnitServiceAgreementTemplate(Unit unit) {
        this();
        init(unit);
    }

    private void checkParameters(Unit unit) {
        if (unit == null) {
            throw new DomainException(
                    "error.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate.unit.cannot.be.null");
        }

    }

    protected void init(Unit unit) {
        checkParameters(unit);
        super.setUnit(unit);
    }

    @Override
    public void setUnit(Unit unit) {
        throw new DomainException(
                "error.accounting.serviceAgreementTemplates.enclosing_type.cannot.modify.unit");
    }

}
