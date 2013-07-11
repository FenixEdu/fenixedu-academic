package net.sourceforge.fenixedu.domain.tests;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class NewGroupElement extends NewGroupElement_Base {

    public NewGroupElement() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void deleteUp() {
        this.setParent(null);

        this.delete();
    }

    public void deleteDown() {
        this.setChild(null);

        this.delete();
    }

    public void deleteBothWays() {
        this.setChild(null);
        this.setParent(null);

        this.delete();
    }

    private void delete() {
        this.setRootDomainObject(null);

        this.deleteDomainObject();
    }

    protected static class PositionComparator implements Comparator<NewGroupElement> {
        @Override
        public int compare(NewGroupElement o1, NewGroupElement o2) {
            return o1.getPosition().compareTo(o2.getPosition());
        }
    }

    public static final Comparator<NewGroupElement> POSITION_COMPARATOR = new PositionComparator();

}
