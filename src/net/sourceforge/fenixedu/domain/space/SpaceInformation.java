package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

public abstract class SpaceInformation extends SpaceInformation_Base implements Comparable<SpaceInformation> {

	private transient YearMonthDay validFrom;

    public abstract String getPresentationName();
    
    protected SpaceInformation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    //public abstract void createNewSpaceInformation();

    public void delete() {
        if (getSpace().getSpaceInformationsCount() == 1) {
            throw new DomainException("space.must.have.at.least.one.space.information");
        }
        super.setSpace((Space) null);
        deleteMaintainingReferenceToSpace();
    }

    protected void deleteMaintainingReferenceToSpace() {
        removeRootDomainObject();
        deleteDomainObject();
    }

	public int compareTo(SpaceInformation spaceInformation) {
		if (getValidUntil() == null) {
			return 1;
		} else if (spaceInformation.getValidUntil() == null) {
			return -1;
		} else {
			return getValidUntil().compareTo(spaceInformation.getValidUntil());
		}
	}
    
	public YearMonthDay getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(final YearMonthDay validFrom) {
		this.validFrom = validFrom;
	}

	public abstract FactoryExecutor getSpaceFactoryEditor();

}
