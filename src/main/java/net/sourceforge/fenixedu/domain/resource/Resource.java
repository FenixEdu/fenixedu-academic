package net.sourceforge.fenixedu.domain.resource;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Resource extends Resource_Base {

    protected Resource() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        if (!canBeDeleted()) {
            throw new DomainException("error.resource.cannot.be.deleted");
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private boolean canBeDeleted() {
        return !hasAnyResourceAllocations() && !hasAnyResourceResponsibility();
    }

    public boolean isSpace() {
        return false;
    }

    public boolean isVehicle() {
        return false;
    }

    public boolean isMaterial() {
        return false;
    }

    public boolean isCampus() {
        return false;
    }

    public boolean isBuilding() {
        return false;
    }

    public boolean isFloor() {
        return false;
    }

    public boolean isRoom() {
        return false;
    }

    public boolean isRoomSubdivision() {
        return false;
    }

    public boolean isExtension() {
        return false;
    }

    public boolean isFireExtinguisher() {
        return false;
    }

    public boolean isAllocatableSpace() {
        return false;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.resource.ResourceResponsibility> getResourceResponsibility() {
        return getResourceResponsibilitySet();
    }

    @Deprecated
    public boolean hasAnyResourceResponsibility() {
        return !getResourceResponsibilitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.resource.ResourceAllocation> getResourceAllocations() {
        return getResourceAllocationsSet();
    }

    @Deprecated
    public boolean hasAnyResourceAllocations() {
        return !getResourceAllocationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

}
