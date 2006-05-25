package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CampusInformation extends CampusInformation_Base {

    protected CampusInformation(final Campus campus, final String campusName) {
        super();
        setSpace(campus);
        setName(campusName);
    }

//    @Override
//    public void createNewSpaceInformation() {
//        final Campus campus = (Campus) getSpace();
//        new CampusInformation(campus, getName());
//    }

    @Override
    public void setName(final String name) {
        if (name == null) {
            throw new NullPointerException("error.campus.name.cannot.be.null");
        }
        super.setName(name);
    }

    @Override
    public void setSpace(final Space space) {
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Campus campus) {
        if (campus == null) {
            throw new NullPointerException("error.campus.cannot.be.null");
        } else if (getSpace() != null) {
            throw new DomainException("error.cannot.change.campus");
        }
        super.setSpace(campus);
    }

    public void edit(final String name) {
    	setName(name);
    }

}
