package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class AggregateUnit extends AggregateUnit_Base {

    private AggregateUnit() {
	super();
	super.setType(PartyTypeEnum.AGGREGATE_UNIT);
    }

    public static Unit createNewAggregateUnit(String name, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification, 
	    Boolean canBeResponsibleOfSpaces) {

	AggregateUnit aggregateUnit = new AggregateUnit();
	aggregateUnit.init(name, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces);	
	aggregateUnit.addParentUnit(parentUnit, accountabilityType);

	return aggregateUnit;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
    
    @Override
    public boolean isAggregateUnit() {
	return true;
    }
}
