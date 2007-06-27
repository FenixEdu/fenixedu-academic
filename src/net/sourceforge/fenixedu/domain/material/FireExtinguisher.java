package net.sourceforge.fenixedu.domain.material;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class FireExtinguisher extends FireExtinguisher_Base {
    
    @Checked("ResourcePredicates.checkPermissionsToManageMaterial")    
    public FireExtinguisher(String identification, String barCodeNumber, YearMonthDay acquisition, YearMonthDay cease, Unit owner,
	    String delivererEnterprise, YearMonthDay loadedDate, YearMonthDay toBeInspectedDate) {	
	
	super();
	super.init(identification, barCodeNumber, acquisition, cease, owner);	
	setDelivererEnterprise(delivererEnterprise);
	setLoadedDate(loadedDate);
	setToBeInspectedDate(toBeInspectedDate);
    }
    
    @Checked("ResourcePredicates.checkPermissionsToManageMaterial")    
    public void edit(String identification, String barCodeNumber, YearMonthDay acquisition, YearMonthDay cease, Unit owner,
	    String delivererEnterprise, YearMonthDay loadedDate, YearMonthDay toBeInspectedDate) {
	
        super.edit(identification, barCodeNumber, acquisition, cease, owner);
        setDelivererEnterprise(delivererEnterprise);
	setLoadedDate(loadedDate);
	setToBeInspectedDate(toBeInspectedDate);
    }
    
    @Checked("ResourcePredicates.checkPermissionsToManageMaterial")
    @Override
    public void delete() {        
	super.delete();
    }
    
    @Override
    public void setDelivererEnterprise(String delivererEnterprise) {
        if(StringUtils.isEmpty(delivererEnterprise)) {
            throw new DomainException("error.FireExtinguisher.empty.delivererEnterprise");
        }
	super.setDelivererEnterprise(delivererEnterprise);
    }
    
    @Override
    public void setLoadedDate(YearMonthDay loadedDate) {
	if(loadedDate == null) {
	    throw new DomainException("error.FireExtinguisher.empty.loadedDate");
	}
	super.setLoadedDate(loadedDate);
    }
    
    @Override
    public void setToBeInspectedDate(YearMonthDay toBeInspectedDate) {
	if(toBeInspectedDate == null) {
	    throw new DomainException("error.FireExtinguisher.empty.toBeInspectedDate");
	}
	super.setToBeInspectedDate(toBeInspectedDate);
    }
    
    @Override
    public String getPresentationDetails() {
	return getIdentification().toString();
    }
    
    @Override
    public String getMaterialSpaceOccupationSlotName() {
	return null;
    }

    @Override
    public Class<? extends MaterialSpaceOccupation> getMaterialSpaceOccupationSubClass() {
	return null;
    }
      
    @Override
    public boolean isFireExtinguisher() {
	return true;
    }
}
