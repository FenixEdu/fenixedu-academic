package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class FloorInformation extends FloorInformation_Base {
    
    protected FloorInformation(final Floor floor, final Integer level) {
        super();
        setSpace(floor);
        setLevel(level);
    }

//    @Override
//    public void createNewSpaceInformation() {
//        final Floor floor = (Floor) getSpace();
//        new FloorInformation(floor, getLevel());
//    }

    @Override
    public void setSpace(final Space space) {
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Floor floor) {
        if (floor == null) {
            throw new NullPointerException("error.floor.cannot.be.null");
        } else if (getSpace() != null) {
            throw new DomainException("error.cannot.change.floor");
        }
        super.setSpace(floor);
    }

    public void edit(final Integer level) {
        setLevel(level);
    }

}
