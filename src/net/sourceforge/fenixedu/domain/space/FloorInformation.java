package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Floor.FloorFactory;
import net.sourceforge.fenixedu.domain.space.Floor.FloorFactoryEditor;

public class FloorInformation extends FloorInformation_Base {

    protected FloorInformation(final Floor floor, final FloorFactory floorFactory) {
	super();	
	super.setSpace(floor);	
	setFirstTimeInterval(floorFactory.getBegin(), floorFactory.getEnd());		
	setLevel(floorFactory.getLevel());	
    }
    
    public void editFloorCharacteristics(Integer level, YearMonthDay begin, YearMonthDay end) {
	editTimeInterval(begin, end);
	setLevel(level);
    }     

    @Override
    public Floor getSpace() {
	return (Floor) super.getSpace();
    }

    @Override
    public void setSpace(final Space space) {
	throw new DomainException("error.incompatible.space");
    }
    
    public void setSpace(final Floor floor) {
	throw new DomainException("error.cannot.change.floor");
    }
    
    @Override
    public void setLevel(Integer level) {
	if (level == null) {
	    throw new DomainException("error.floor.level.cannot.be.null");
	}
	super.setLevel(level);
    }

    public FloorFactoryEditor getSpaceFactoryEditor() {
	final FloorFactoryEditor floorFactoryEditor = new FloorFactoryEditor();
	floorFactoryEditor.setSpace(getSpace());
	floorFactoryEditor.setLevel(getLevel());
	floorFactoryEditor.setBegin(getNextPossibleValidFromDate());
	return floorFactoryEditor;
    }

    @Override
    public String getPresentationName() {
	return String.valueOf(getLevel());
    }
}
