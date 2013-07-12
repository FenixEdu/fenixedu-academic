package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitFileTag extends UnitFileTag_Base {

    public UnitFileTag(Unit unit, String name) {
        setRootDomainObject(RootDomainObject.getInstance());
        setUnit(unit);
        setName(name);
    }

    public Integer getFileTagCount(Person person) {
        int count = 0;
        for (UnitFile file : getTaggedFiles()) {
            if (file.isPersonAllowedToAccess(person)) {
                count++;
            }
        }
        return count;
    }

    public boolean isTagAccessibleToUser(Person person) {
        return getFileTagCount(person) > 0;
    }

    public void delete() {
        if (getTaggedFilesSet().size() > 0) {
            throw new DomainException("error.cannot.delete.tag.with.files");
        }
        setUnit(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Set<UnitFileTag> getNeighboursTags() {
        Set<UnitFileTag> tags = new HashSet<UnitFileTag>();
        for (UnitFile file : getTaggedFiles()) {
            tags.addAll(file.getUnitFileTags());
        }
        tags.remove(this);
        return tags;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitFile> getTaggedFiles() {
        return getTaggedFilesSet();
    }

    @Deprecated
    public boolean hasAnyTaggedFiles() {
        return !getTaggedFilesSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
