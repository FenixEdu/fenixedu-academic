package net.sourceforge.fenixedu.domain.material;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.space.ExtensionSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;
import net.sourceforge.fenixedu.predicates.ResourcePredicates;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Extension extends Extension_Base {

    public Extension(String identification, String barCodeNumber, YearMonthDay acquisition, YearMonthDay cease, Unit owner) {
//        check(this, ResourcePredicates.checkPermissionsToManageMaterial);
        super();
        super.init(identification, barCodeNumber, acquisition, cease, owner);
    }

    @Override
    public void edit(String identification, String barCodeNumber, YearMonthDay acquisition, YearMonthDay cease, Unit owner) {
        check(this, ResourcePredicates.checkPermissionsToManageMaterial);
        super.edit(identification, barCodeNumber, acquisition, cease, owner);
    }

    @Override
    public void delete() {
        check(this, ResourcePredicates.checkPermissionsToManageMaterial);
        super.delete();
    }

    @Override
    public String getMaterialSpaceOccupationSlotName() {
        return "extension";
    }

    public Integer getNumber() {
        return Integer.valueOf(getIdentification());
    }

    @Override
    public Class<? extends MaterialSpaceOccupation> getMaterialSpaceOccupationSubClass() {
        return ExtensionSpaceOccupation.class;
    }

    @Override
    public String getPresentationDetails() {
        return getIdentification();
    }

    @Override
    public void setIdentification(String identification) {
        if (identification != null && !StringUtils.isNumeric(identification)) {
            throw new DomainException("error.extension.no.number");
        }
        super.setIdentification(identification);
    }

    @Override
    public boolean isExtension() {
        return true;
    }

    public static Extension readByNumber(Integer number) {
        for (Resource resource : RootDomainObject.getInstance().getResources()) {
            if (resource.isExtension() && ((Extension) resource).getIdentification() != null
                    && ((Extension) resource).getIdentification().equals(number)) {
                return (Extension) resource;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.material.PersonExtension> getPersons() {
        return getPersonsSet();
    }

    @Deprecated
    public boolean hasAnyPersons() {
        return !getPersonsSet().isEmpty();
    }

}
