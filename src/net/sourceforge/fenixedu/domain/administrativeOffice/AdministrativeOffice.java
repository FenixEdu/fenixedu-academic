package net.sourceforge.fenixedu.domain.administrativeOffice;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class AdministrativeOffice extends AdministrativeOffice_Base {

    private AdministrativeOffice() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());

    }

    public AdministrativeOffice(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
        this();
        init(administrativeOfficeType, unit);
    }

    private void checkParameters(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
        if (administrativeOfficeType == null) {
            throw new DomainException(
                    "error.administrativeOffice.AdministrativeOffice.administrativeOfficeType.cannot.be.null");
        }
        if (unit == null) {
            throw new DomainException(
                    "error.administrativeOffice.AdministrativeOffice.unit.cannot.be.null");
        }

        checkIfExistsAdministrativeOfficeForType(administrativeOfficeType);

    }

    private void checkIfExistsAdministrativeOfficeForType(
            AdministrativeOfficeType administrativeOfficeType) {

        for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
                .getAdministrativeOffices()) {
            if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
                throw new DomainException(
                        "error.administrativeOffice.AdministrativeOffice.already.exists.with.administrativeOfficeType");
            }
        }
    }

    protected void init(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
        checkParameters(administrativeOfficeType, unit);
        super.setAdministrativeOfficeType(administrativeOfficeType);
        super.setUnit(unit);

    }

    @Override
    public void setAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {
        throw new DomainException(
                "error.administrativeOffice.AdministrativeOffice.cannot.modify.administrativeOfficeType");
    }

    @Override
    public void setUnit(Unit unit) {
        throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.unit");
    }

    // static methods
    public static AdministrativeOffice readByAdministrativeOfficeType(
            AdministrativeOfficeType administrativeOfficeType) {

        for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
                .getAdministrativeOffices()) {

            if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
                return administrativeOffice;
            }

        }
        return null;

    }

}
