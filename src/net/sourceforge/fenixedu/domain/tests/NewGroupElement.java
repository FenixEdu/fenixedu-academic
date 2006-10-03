package net.sourceforge.fenixedu.domain.tests;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class NewGroupElement extends NewGroupElement_Base {

	public NewGroupElement() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setOjbConcreteClass(getClass().getName());
	}

	public void deleteUp() {
		this.removeParent();

		this.delete();
	}

	public void deleteDown() {
		this.removeChild();

		this.delete();
	}

	public void deleteBothWays() {
		this.removeChild();
		this.removeParent();

		this.delete();
	}

	private void delete() {
		this.removeRootDomainObject();

		this.deleteDomainObject();
	}
	
	protected static class PositionComparator implements Comparator<NewGroupElement> {
		public int compare(NewGroupElement o1, NewGroupElement o2) {
			return o1.getPosition().compareTo(o2.getPosition());
		}
	}
	
	public static final Comparator<NewGroupElement> POSITION_COMPARATOR = new PositionComparator();

}
