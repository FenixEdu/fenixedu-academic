package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class Campus extends Campus_Base {

	public static Comparator<Campus> CAMPUS_COMPARATOR_BY_NAME = new BeanComparator("spaceInformation.name", Collator.getInstance());

	public static abstract class CampusFactory implements Serializable, FactoryExecutor {
		private String name;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	public static class CampusFactoryCreator extends CampusFactory {
		public Campus execute() {
			return new Campus(this);
		}
	}

	public static class CampusFactoryEditor extends CampusFactory {
        private DomainReference<Campus> campusReference;

		public Campus getSpace() {
			return campusReference == null ? null : campusReference.getObject();
		}
		public void setSpace(Campus campus) {
			if (campus != null) {
				this.campusReference = new DomainReference<Campus>(campus);
			}
		}

		public CampusInformation execute() {
			return new CampusInformation(getSpace(), this);
		}
	}

    protected Campus() {
        super();
    }

    public Campus(CampusFactory campusFactory) {
        this();
        new CampusInformation(this, campusFactory);
    }

    @Override
    public CampusInformation getSpaceInformation() {
        return (CampusInformation) super.getSpaceInformation();
    }

    @Override
    public CampusInformation getSpaceInformation(final YearMonthDay when) {
        return (CampusInformation) super.getSpaceInformation(when);
    }

}
