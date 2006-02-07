package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

public abstract class SpaceInformation extends SpaceInformation_Base implements Comparable<SpaceInformation> {

	private transient YearMonthDay validFrom;

    protected SpaceInformation() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }

    public void delete() {
        super.setSpace((Space) null);
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

}
