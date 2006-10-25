package net.sourceforge.fenixedu.domain.material;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.ExtensionSpaceOccupation;

import org.joda.time.YearMonthDay;

public class Extension extends Extension_Base {

    public Extension(Integer number, Integer barCodeNumber, YearMonthDay acquisition,
	    YearMonthDay cease, Unit owner) {
	
	super();
	checkParameters(number, acquisition, cease);
	setNumber(number);
	setBarCodeNumber(barCodeNumber);
	setAcquisition(acquisition);
	setCease(cease);
	setOwner(owner);
    }

    @Override
    public String getMaterialSpaceOccupationSlotName() {
	return "extension";
    }

    @Override
    public Class getMaterialSpaceOccupationSubClass() {
	return ExtensionSpaceOccupation.class;
    }

    @Override
    public String getPresentationDetails() {
	return getNumber().toString();
    }

    private void checkParameters(Integer number, YearMonthDay acquisition, YearMonthDay cease) {
	if (number == null) {
	    throw new DomainException("error.extension.no.number");
	}
	if (acquisition == null) {
	    throw new DomainException("error.extension.no.acquisitionDate");
	}
	if (cease != null && cease.isBefore(acquisition)) {
	    throw new DomainException("error.extension.endDateBeforeBeginDate");
	}
    }

    @Override
    public void setNumber(Integer number) {
	checkNumber(number);
	super.setNumber(number);
    }

    private void checkNumber(Integer number) {
	Extension extension = readByNumber(number);
	if (extension != null && !extension.equals(this)) {
	    throw new DomainException("error.extension.already.exists.one.extension.with.same.number");
	}
    }

    public static Extension readByNumber(Integer number) {
	for (Material material : RootDomainObject.getInstance().getMaterials()) {
	    if (material instanceof Extension && ((Extension) material).getNumber() != null
		    && ((Extension) material).getNumber().equals(number)) {
		return (Extension) material;
	    }
	}
	return null;
    }

    public static List<Extension> readAllExtensions() {
	List<Extension> extensions = new ArrayList<Extension>();
	for (Material material : RootDomainObject.getInstance().getMaterials()) {
	    if (material instanceof Extension) {
		extensions.add((Extension) material);
	    }
	}
	return extensions;
    }
}
