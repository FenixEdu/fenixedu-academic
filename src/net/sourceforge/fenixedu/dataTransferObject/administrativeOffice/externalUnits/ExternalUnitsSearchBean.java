package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ExternalUnitsSearchBean implements Serializable {
    
    private String unitName;
    private PartyTypeEnum unitType;
    private DomainReference<Unit> earthUnit;
    private List<AbstractExternalUnitResultBean> results;
    
    public ExternalUnitsSearchBean() {
    }
    
    public ExternalUnitsSearchBean(final Unit earthUnit) {
	setEarthUnit(earthUnit);
    }
    
    public PartyTypeEnum getUnitType() {
        return unitType;
    }

    public void setUnitType(PartyTypeEnum type) {
        this.unitType = type;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    public Unit getEarthUnit() {
	return (this.earthUnit != null) ? this.earthUnit.getObject() : null;
    }

    public void setEarthUnit(Unit earthUnit) {
	this.earthUnit = (earthUnit != null) ? new DomainReference<Unit>(earthUnit) : null;
    }

    public List<AbstractExternalUnitResultBean> getResults() {
        return results;
    }

    public void setResults(List<AbstractExternalUnitResultBean> results) {
        this.results = results;
    }
    
    public boolean add(AbstractExternalUnitResultBean resultBean) {
	return this.results.add(resultBean);
    }
    
    public void clearResults() {
	if (this.results == null) {
	    this.results = new ArrayList<AbstractExternalUnitResultBean>();
	} else {
	    this.results.clear();
	}
    }

    public List<PartyTypeEnum> getValidPartyTypes() {
	final List<PartyTypeEnum> result = new ArrayList<PartyTypeEnum>(5);
	result.add(PartyTypeEnum.COUNTRY);
	result.add(PartyTypeEnum.UNIVERSITY);
	result.add(PartyTypeEnum.SCHOOL);
	result.add(PartyTypeEnum.DEPARTMENT);
	return result;
    }
}
