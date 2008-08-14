package net.sourceforge.fenixedu.domain.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.resource.ResourceResponsibility;
import net.sourceforge.fenixedu.domain.space.MaterialSpaceOccupation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public abstract class Material extends Material_Base {

    public final static Comparator<Material> COMPARATOR_BY_IDENTIFICATION = new ComparatorChain();
    public final static Comparator<Material> COMPARATOR_BY_CLASS_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_IDENTIFICATION).addComparator(new BeanComparator("identification"));
	((ComparatorChain) COMPARATOR_BY_IDENTIFICATION).addComparator(DomainObject.COMPARATOR_BY_ID);

	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("class.simpleName"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(new BeanComparator("acquisition"));
	((ComparatorChain) COMPARATOR_BY_CLASS_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public abstract String getMaterialSpaceOccupationSlotName();

    public abstract Class<? extends MaterialSpaceOccupation> getMaterialSpaceOccupationSubClass();

    public abstract String getPresentationDetails();

    protected Material() {
	super();
    }

    protected void init(String identification, String barCodeNumber, YearMonthDay acquisition, YearMonthDay cease, Unit owner) {
	setIdentification(identification);
	setBarCodeNumber(barCodeNumber);
	setAcquisition(acquisition);
	setCease(cease);
	setOwner(owner);
    }

    public void edit(String identification, String barCodeNumber, YearMonthDay acquisition, YearMonthDay cease, Unit owner) {
	init(identification, barCodeNumber, acquisition, cease, owner);
    }

    @Override
    public void delete() {
	deleteOwner();
	super.delete();
    }

    public void setOwner(Unit owner) {
	// Each material only have one owner
	deleteOwner();
	new MaterialResponsibility(owner, this);
    }

    public Unit getOwner() {
	List<ResourceResponsibility> list = getResourceResponsibility();
	for (ResourceResponsibility resourceResponsibility : list) {
	    if (resourceResponsibility.isMaterialResponsibility()) {
		return (Unit) resourceResponsibility.getParty();
	    }
	}
	return null;
    }

    private void deleteOwner() {
	List<ResourceResponsibility> list = getResourceResponsibility();
	for (Iterator<ResourceResponsibility> iter = list.iterator(); iter.hasNext();) {
	    ResourceResponsibility responsibility = iter.next();
	    if (responsibility.isMaterialResponsibility()) {
		iter.remove();
		responsibility.delete();
	    }
	}
    }

    @Override
    public void setBarCodeNumber(String barCodeNumber) {
	if (StringUtils.isEmpty(barCodeNumber)) {
	    throw new DomainException("error.Material.empty.barCodeNumber");
	}
	checkBarCodeNumber(barCodeNumber, getClass());
	super.setBarCodeNumber(barCodeNumber);
    }

    private void checkBarCodeNumber(String barCodeNumber, Class<? extends Material> materialClass) {
	for (Resource resource : RootDomainObject.getInstance().getResources()) {
	    if (!resource.equals(this) && resource.isMaterial() && resource.getClass().equals(materialClass)
		    && ((Material) resource).getBarCodeNumber().equals(barCodeNumber)) {
		throw new DomainException("error.Material.already.exists.one.material.wiht.same.barCodeNumber");
	    }
	}
    }

    @Override
    public void setIdentification(String identification) {
	if (StringUtils.isEmpty(identification)) {
	    throw new DomainException("error.Material.empty.identification");
	}
	checkIdentification(identification, getClass());
	super.setIdentification(identification);
    }

    @Override
    public void setAcquisition(YearMonthDay acquisition) {
	if (acquisition == null || (getCease() != null && getCease().isBefore(acquisition))) {
	    throw new DomainException("error.material.invalid.acquisitionDate");
	}
	super.setAcquisition(acquisition);
    }

    @Override
    public void setCease(YearMonthDay cease) {
	if (getAcquisition() == null || (cease != null && cease.isBefore(getAcquisition()))) {
	    throw new DomainException("error.material.invalid.ceaseDate");
	}
	super.setCease(cease);
    }

    @Override
    public boolean isMaterial() {
	return true;
    }

    private void checkIdentification(String identification, Class<? extends Material> materialClass) {
	for (Resource resource : RootDomainObject.getInstance().getResources()) {
	    if (!resource.equals(this) && resource.isMaterial() && resource.getClass().equals(materialClass)
		    && ((Material) resource).getIdentification().equals(identification.trim())) {
		throw new DomainException("error.Material.already.exists.one.material.wiht.same.identification");
	    }
	}
    }

    public static Material getMaterialByTypeAndIdentification(Class<? extends Material> materialType, String identification) {
	for (Resource resource : RootDomainObject.getInstance().getResources()) {
	    if (resource.isMaterial() && ((Material) resource).getIdentification().equals(identification)
		    && resource.getClass().equals(materialType)) {
		return (Material) resource;
	    }
	}
	return null;
    }

    public static List<Material> getAllMaterialByTypeOrderByIdentification(Class<? extends Material> materialClass) {
	List<Material> result = new ArrayList<Material>();
	if (materialClass != null) {
	    for (Resource resource : RootDomainObject.getInstance().getResources()) {
		if (resource.getClass().equals(materialClass)) {
		    result.add((Material) resource);
		}
	    }
	}
	Collections.sort(result, COMPARATOR_BY_IDENTIFICATION);
	return result;
    }
}
