package net.sourceforge.fenixedu.domain.space;

public abstract class SpaceInformation extends SpaceInformation_Base {

    protected SpaceInformation() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }

    public void delete() {
        super.setSpace((Space) null);
        deleteDomainObject();
    }

}
