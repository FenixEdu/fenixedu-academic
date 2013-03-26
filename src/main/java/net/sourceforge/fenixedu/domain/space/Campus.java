package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class Campus extends Campus_Base {

    private static final String ALAMEDA_NAME = "Alameda";
    private static final String TAGUSPARK_NAME = "Taguspark";

    private static final String ALAMEDA_UNIVERSITY_CODE = "0807";
    private static final String TAGUSPARK_UNIVERSITY_CODE = "0808";
    public static final String DEFAULT_UNIVERSITY_CODE = ALAMEDA_UNIVERSITY_CODE;

    public Campus(String name, YearMonthDay begin, YearMonthDay end, String blueprintNumber) {
        super();
        new CampusInformation(this, name, begin, end, blueprintNumber);
    }

    @Override
    @Checked("SpacePredicates.checkPermissionsToManageSpace")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted campus", parameters = {})
    public void delete() {
        super.delete();
    }

    @Override
    public void setSuroundingSpace(Space suroundingSpace) {
        throw new DomainException("error.Space.invalid.suroundingSpace");
    }

    @Override
    public CampusInformation getSpaceInformation() {
        return (CampusInformation) super.getSpaceInformation();
    }

    @Override
    public CampusInformation getSpaceInformation(final YearMonthDay when) {
        return (CampusInformation) super.getSpaceInformation(when);
    }

    public String getLocation() {
        return getSpaceInformation().hasLocality() ? getSpaceInformation().getLocality().getName() : null;
    }

    public String getName() {
        return getSpaceInformation().getName();
    }

    public static Campus readActiveCampusByName(String campusName) {
        for (Resource space : RootDomainObject.getInstance().getResources()) {
            if (space.isCampus() && ((Campus) space).isActive()
                    && ((Campus) space).getSpaceInformation().getName().equals(campusName)) {
                return (Campus) space;
            }
        }
        return null;
    }

    public static Campus readCampusByName(String name) {
        for (Campus campus : Space.getAllCampus()) {
            if (campus.getName().equalsIgnoreCase(name)) {
                return campus;
            }
        }
        return null;
    }

    @Override
    public boolean isCampus() {
        return true;
    }

    @Override
    public Integer getExamCapacity() {
        // Necessary for Renderers
        return null;
    }

    @Override
    public Integer getNormalCapacity() {
        // Necessary for Renderers
        return null;
    }

    static public String getUniversityCode(final Campus campus) {
        if (campus == null) {
            return DEFAULT_UNIVERSITY_CODE;
        }

        if (campus.getName().equalsIgnoreCase(ALAMEDA_NAME)) {
            return ALAMEDA_UNIVERSITY_CODE;
        } else if (campus.getName().equalsIgnoreCase(TAGUSPARK_NAME)) {
            return TAGUSPARK_UNIVERSITY_CODE;
        } else {
            return DEFAULT_UNIVERSITY_CODE;
        }
    }

    public Boolean isCampusAlameda() {
        return this.getName().equals(ALAMEDA_NAME);
    }

    public Boolean isCampusTaguspark() {
        return this.getName().equals(TAGUSPARK_NAME);
    }

    public static abstract class CampusFactory implements Serializable, FactoryExecutor {
        private String name;

        private YearMonthDay begin;

        private YearMonthDay end;

        private String blueprintNumber;

        public YearMonthDay getBegin() {
            return begin;
        }

        public void setBegin(YearMonthDay begin) {
            this.begin = begin;
        }

        public YearMonthDay getEnd() {
            return end;
        }

        public void setEnd(YearMonthDay end) {
            this.end = end;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBlueprintNumber() {
            return blueprintNumber;
        }

        public void setBlueprintNumber(String blueprintNumber) {
            this.blueprintNumber = blueprintNumber;
        }
    }

    public static class CampusFactoryCreator extends CampusFactory {
        @Override
        public Campus execute() {
            return new Campus(getName(), getBegin(), getEnd(), getBlueprintNumber());
        }
    }

    public static class CampusFactoryEditor extends CampusFactory {
        private Campus campusReference;

        public Campus getSpace() {
            return campusReference;
        }

        public void setSpace(Campus campus) {
            if (campus != null) {
                this.campusReference = campus;
            }
        }

        @Override
        public CampusInformation execute() {
            return new CampusInformation(getSpace(), getName(), getBegin(), getEnd(), getBlueprintNumber());
        }
    }
}
