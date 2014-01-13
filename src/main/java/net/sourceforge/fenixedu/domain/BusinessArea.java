package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class BusinessArea extends BusinessArea_Base {

    public BusinessArea(Integer level, String code, String description) {
        super();

        if (level == null) {
            throw new DomainException("businessArea.creation.level.null");
        }

        if (code == null) {
            throw new DomainException("businessArea.creation.code.null");
        }

        if (description == null) {
            throw new DomainException("businessArea.creation.description.null");
        }

        setRootDomainObject(Bennu.getInstance());
        setLevel(level);
        setCode(code);
        setDescription(description);
    }

    public BusinessArea(Integer level, String code, String description, BusinessArea parentArea) {
        this(level, code, description);
        setParentArea(parentArea);
    }

    public static List<BusinessArea> getParentBusinessAreas() {
        List<BusinessArea> parentAreas = new ArrayList<BusinessArea>();
        for (BusinessArea area : Bennu.getInstance().getBusinessAreasSet()) {
            if (!area.hasParentArea()) {
                parentAreas.add(area);
            }
        }
        return parentAreas;
    }

    public static Object getChildBusinessAreas(BusinessArea parentArea) {
        List<BusinessArea> childAreas = new ArrayList<BusinessArea>();
        for (BusinessArea area : Bennu.getInstance().getBusinessAreasSet()) {
            if (area.hasParentArea() && area.getParentArea().equals(parentArea)) {
                childAreas.add(area);
            }
        }
        return childAreas;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Job> getParentJobs() {
        return getParentJobsSet();
    }

    @Deprecated
    public boolean hasAnyParentJobs() {
        return !getParentJobsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.BusinessArea> getChildAreas() {
        return getChildAreasSet();
    }

    @Deprecated
    public boolean hasAnyChildAreas() {
        return !getChildAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Job> getJobs() {
        return getJobsSet();
    }

    @Deprecated
    public boolean hasAnyJobs() {
        return !getJobsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasParentArea() {
        return getParentArea() != null;
    }

    @Deprecated
    public boolean hasLevel() {
        return getLevel() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
