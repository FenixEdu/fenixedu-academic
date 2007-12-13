package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import dml.runtime.RelationAdapter;


public class UnitAcronym extends UnitAcronym_Base {
    
    static {
	UnitUnitAcronym.addListener(new RelationAdapter<UnitAcronym, Unit>() {

	    @Override
	    public void afterRemove(UnitAcronym unitAcronym, Unit unit) {
		if(unitAcronym != null) {
		    if(!unitAcronym.hasAnyUnits()) {
			unitAcronym.delete();
		    }
		}
	    }

	});
    }
    
    public  UnitAcronym(final String acronym) {
        super();
        setAcronym(acronym);
    }
    
    @Override
    public void setAcronym(String acronym) {
        super.setAcronym(acronym.toLowerCase());
    }
    
    public void delete() {
	if(!canBeDeleted()) {
	    throw new DomainException("error.unitAcronym.cannot.be.deleted");
	}
	setRootDomainObject(null);
	deleteDomainObject();
    }
    
    private boolean canBeDeleted() {
	return !hasAnyUnits();
    }

    public static UnitAcronym readUnitAcronymByAcronym(final String acronym) {
	final String acronymLowerCase = acronym.toLowerCase();
	for (UnitAcronym unitAcronym : RootDomainObject.getInstance().getUnitAcronymsSet()) {
	    if(unitAcronym.getAcronym().equals(acronymLowerCase)) {
		return unitAcronym;
	    }
	}
	return null;
    }
    
}
