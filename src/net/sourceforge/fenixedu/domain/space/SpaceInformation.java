package net.sourceforge.fenixedu.domain.space;

public abstract class SpaceInformation extends SpaceInformation_Base {

    protected SpaceInformation() {
        super();
    }

    public void delete() {
        setSpace(null);
        deleteDomainObject();
    }

}
