package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public class SchoolClass extends SchoolClass_Base {

    public SchoolClass() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void associateShift(Shift shift) {
        if (shift == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedShifts().contains(shift)) {
            this.getAssociatedShifts().add(shift);
        }
        if (!shift.getAssociatedClasses().contains(this)) {
            shift.getAssociatedClasses().add(this);
        }
    }

}
