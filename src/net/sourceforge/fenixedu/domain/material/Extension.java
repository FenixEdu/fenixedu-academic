package net.sourceforge.fenixedu.domain.material;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.space.ExtensionSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;

import org.joda.time.YearMonthDay;

public class Extension extends Extension_Base {

    public Extension(Integer number, Integer barCodeNumber, YearMonthDay acquisition,
	    YearMonthDay cease, Unit owner) {

	super();	
	setNumber(number);
	setBarCodeNumber(barCodeNumber);	
	setOwner(owner);
	setOccupationInterval(acquisition, cease);
    }

    @Override
    public String getMaterialSpaceOccupationSlotName() {
	return "extension";
    }

    @Override
    public Class<? extends MaterialSpaceOccupation> getMaterialSpaceOccupationSubClass() {
	return ExtensionSpaceOccupation.class;
    }

    @Override
    public String getPresentationDetails() {
	return getNumber().toString();
    }
    
    @Override
    public void setNumber(Integer number) {
	if (number == null) {
	    throw new DomainException("error.extension.no.number");
	}
	checkNumber(number);
	super.setNumber(number);
    }

    @Override
    public boolean isExtension() {
        return true;
    }
    
    public static Extension readByNumber(Integer number) {
	for (Resource resource : RootDomainObject.getInstance().getResources()) {
	    if (resource.isExtension() && ((Extension) resource).getNumber() != null
		    && ((Extension) resource).getNumber().equals(number)) {
		return (Extension) resource;
	    }
	}
	return null;
    }
       
    private void checkNumber(Integer number) {
	Extension extension = readByNumber(number);
	if (extension != null && !extension.equals(this)) {
	    throw new DomainException("error.extension.already.exists.one.extension.with.same.number");
	}
    }   
}
