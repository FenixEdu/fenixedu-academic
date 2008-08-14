package net.sourceforge.fenixedu.dataTransferObject.resourceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.domain.material.FireExtinguisher;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.YearMonthDay;

public class MaterialBean implements Serializable {

    private String identification;

    private MaterialType materialType;

    private String barCodeNumber;

    private YearMonthDay acquisition;

    private YearMonthDay cease;

    private DomainReference<Unit> ownerReference;

    private String delivererEnterprise;

    private YearMonthDay loadedDate;

    private YearMonthDay toBeInspectedDate;

    public String getDelivererEnterprise() {
	return delivererEnterprise;
    }

    public void setDelivererEnterprise(String delivererEnterprise) {
	this.delivererEnterprise = delivererEnterprise;
    }

    public YearMonthDay getLoadedDate() {
	return loadedDate;
    }

    public void setLoadedDate(YearMonthDay loadedDate) {
	this.loadedDate = loadedDate;
    }

    public YearMonthDay getToBeInspectedDate() {
	return toBeInspectedDate;
    }

    public void setToBeInspectedDate(YearMonthDay toBeInspectedDate) {
	this.toBeInspectedDate = toBeInspectedDate;
    }

    public MaterialBean() {
	// Empty Constructor
    }

    public MaterialBean(String identification, MaterialType materialType) {
	setIdentification(identification);
	setMaterialType(materialType);
    }

    public enum MaterialType {

	EXTENSION(Extension.class),

	FIRE_EXTINGUISHER(FireExtinguisher.class);

	private Class<? extends Material> materialClass;

	private MaterialType(Class<? extends Material> materialClass) {
	    this.materialClass = materialClass;
	}

	public String getName() {
	    return name();
	}

	public Class<? extends Material> getMaterialClass() {
	    return materialClass;
	}

	public static MaterialType getMaterialTypeByMaterialClass(Class<? extends Material> materialClass) {
	    for (MaterialType materialType : values()) {
		if (materialType.getMaterialClass().equals(materialClass)) {
		    return materialType;
		}
	    }
	    return null;
	}
    }

    public String getIdentification() {
	return identification;
    }

    public void setIdentification(String identification) {
	this.identification = identification;
    }

    public MaterialType getMaterialType() {
	return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
	this.materialType = materialType;
    }

    public String getBarCodeNumber() {
	return barCodeNumber;
    }

    public void setBarCodeNumber(String barCodeNumber) {
	this.barCodeNumber = barCodeNumber;
    }

    public YearMonthDay getAcquisition() {
	return acquisition;
    }

    public void setAcquisition(YearMonthDay acquisition) {
	this.acquisition = acquisition;
    }

    public YearMonthDay getCease() {
	return cease;
    }

    public void setCease(YearMonthDay cease) {
	this.cease = cease;
    }

    public Unit getOwner() {
	return (this.ownerReference != null) ? this.ownerReference.getObject() : null;
    }

    public void setOwner(Unit owner) {
	this.ownerReference = (owner != null) ? new DomainReference<Unit>(owner) : null;
    }
}
