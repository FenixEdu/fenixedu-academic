package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.LinkObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public abstract class AbstractExternalUnitResultBean implements Serializable {
    
    transient protected static final List<AccountabilityTypeEnum> ACCOUNTABILITY_TYPES = Arrays
	    .asList(new AccountabilityTypeEnum[] { 
		    AccountabilityTypeEnum.GEOGRAPHIC,
		    AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE,
		    AccountabilityTypeEnum.ACADEMIC_STRUCTURE });
    
    private PartyTypeEnum parentUnitType;
    private String fullName;

    public AbstractExternalUnitResultBean() {
    }
    
    public PartyTypeEnum getParentUnitType() {
        return parentUnitType;
    }

    public void setParentUnitType(PartyTypeEnum parentUnitType) {
        this.parentUnitType = parentUnitType;
    }
    
    private boolean hasParentUnitType() {
	return getParentUnitType() != null;
    }
    
    protected List<Unit> searchFullPath() {
	final List<Unit> units = UnitUtils.getUnitFullPath(getUnit(), ACCOUNTABILITY_TYPES);
	if (hasParentUnitType()) {
	    removeAllUnitsUntilParentUnitType(units);
	}
	return units;
    }
    
    private void removeAllUnitsUntilParentUnitType(final List<Unit> units) {
	final Iterator<Unit> iterUnits = units.iterator();
	while (iterUnits.hasNext()) {
	    if (iterUnits.next().getType() == getParentUnitType()) {
		iterUnits.remove();
		break;
	    } else {
		iterUnits.remove();
	    }
	}
    }
    
    public String getNumberOfUniversities() {
        return "-";
    }
    
    public String getNumberOfSchools() {
        return "-";
    }

    public String getNumberOfDepartments() {
	return "-";
    }

    public String getNumberOfExternalCurricularCourses() {
	return "-";
    }
    
    public String getFullName() {
	if (this.fullName == null) {
	    final StringBuilder unitFullPathName = UnitUtils.getUnitFullPathName(getUnit(), ACCOUNTABILITY_TYPES);
	    setFullName(unitFullPathName.toString() + " > " + getName());
	}
	return this.fullName;
    }
    
    protected void setFullName(final String fullName) {
	this.fullName = fullName;
    }
 
    abstract public Unit getUnit();
    abstract public Enum getType();
    abstract public List<LinkObject> getFullPath();
    abstract public String getName();
}
