package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class UnitName extends UnitName_Base implements Comparable<UnitName> {

    public static class UnitNameLimitedOrderedSet extends TreeSet<UnitName> {

	private final int maxElements;

	public UnitNameLimitedOrderedSet(final int maxElements) {
	    super();
	    this.maxElements = maxElements;
	}

	@Override
	public boolean add(final UnitName unitName) {
	    if (size() < maxElements) {
		return super.add(unitName);
	    }
	    final UnitName lastUnitName = last();
	    if (lastUnitName.compareTo(unitName) > 0) {
		remove(lastUnitName);
		return super.add(unitName);
	    }
	    return false;
	}
    }

    public static class InternalUnitNameLimitedOrderedSet extends UnitNameLimitedOrderedSet {

	public InternalUnitNameLimitedOrderedSet(final int maxElements) {
	    super(maxElements);
	}

	@Override
	public boolean add(final UnitName unitName) {
	    return unitName.getIsExternalUnit() ? false : super.add(unitName);
	}
    }

    public static class ExternalUnitNameLimitedOrderedSet extends UnitNameLimitedOrderedSet {

	public ExternalUnitNameLimitedOrderedSet(final int maxElements) {
	    super(maxElements);
	}

	@Override
	public boolean add(final UnitName unitName) {
	    return unitName.getIsExternalUnit() ? super.add(unitName) : false;
	}
    }

    public UnitName(Unit unit) {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
	setUnit(unit);
	setIsExternalUnit(Boolean.valueOf(!unit.isInternal()));
    }

    public int compareTo(UnitName unitName) {
	final int stringCompare = getName().compareTo(unitName.getName());
	return stringCompare == 0 ? getIdInternal().compareTo(unitName.getIdInternal()) : stringCompare;
    }

    @Override
    public void setName(String name) {
	super.setName(UnitNamePart.normalize(name));
	UnitNamePart.reindex(this);
    }

    private static boolean containsAll(final String normalizedUnitName, final String[] nameParts) {
	for (final String namePart : nameParts) {
	    if (normalizedUnitName.indexOf(namePart) == -1) {
		return false;
	    }
	}
	return true;
    }

    public static void find(final UnitNameLimitedOrderedSet unitNameLimitedOrderedSet, final String name,
	    final int size) {
	final String[] nameParts = UnitNamePart.getNameParts(name);
	if (nameParts.length > 0) {
	    final UnitNamePart unitNamePart = UnitNamePart.find(nameParts[0]);
	    if (unitNamePart != null && nameParts.length == 1) {
		unitNameLimitedOrderedSet.addAll(unitNamePart.getUnitNameSet());
	    } else {
		final Set<UnitName> unitNames = unitNamePart == null ? RootDomainObject.getInstance()
			.getUnitNameSet() : unitNamePart.getUnitNameSet();
		for (final UnitName unitName : unitNames) {
		    final String normalizedUnitName = unitName.getName();
		    if (containsAll(normalizedUnitName, nameParts)) {
			unitNameLimitedOrderedSet.add(unitName);
		    }
		}
	    }
	}
    }

    public static Collection<UnitName> findInternalUnit(final String name, final int size) {
	final InternalUnitNameLimitedOrderedSet unitNameLimitedOrderedSet = new InternalUnitNameLimitedOrderedSet(
		size);
	find(unitNameLimitedOrderedSet, name, size);
	return unitNameLimitedOrderedSet;
    }
    
    public static Collection<UnitName> findExternalUnit(final String name, final int size) {
	final ExternalUnitNameLimitedOrderedSet unitNameLimitedOrderedSet = new ExternalUnitNameLimitedOrderedSet(
		size);
	find(unitNameLimitedOrderedSet, name, size);
	return unitNameLimitedOrderedSet;
    }

    public static Collection<UnitName> find(final String name, final int size) {
	final UnitNameLimitedOrderedSet unitNameLimitedOrderedSet = new UnitNameLimitedOrderedSet(size);
	find(unitNameLimitedOrderedSet, name, size);
	return unitNameLimitedOrderedSet;
    }

    public void delete() {
	final Set<UnitNamePart> unitNameParts = new HashSet<UnitNamePart>(getUnitNamePartSet());
	getUnitNamePartSet().clear();
	removeRootDomainObject();
	deleteDomainObject();
	for (final UnitNamePart unitNamePart : unitNameParts) {
	    unitNamePart.deleteIfEmpty();
	}
    }

}
