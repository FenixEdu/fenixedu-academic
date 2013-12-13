package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

public class EducationArea extends EducationArea_Base {

    public EducationArea(String code, String description) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCode(code);
        setDescription(description);
    }

    public EducationArea(String code, String description, EducationArea parentArea) {
        this(code, description);
        setParentArea(parentArea);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Formation> getAssociatedFormations() {
        return getAssociatedFormationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedFormations() {
        return !getAssociatedFormationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EducationArea> getChildAreas() {
        return getChildAreasSet();
    }

    @Deprecated
    public boolean hasAnyChildAreas() {
        return !getChildAreasSet().isEmpty();
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
    public boolean hasCode() {
        return getCode() != null;
    }

}
