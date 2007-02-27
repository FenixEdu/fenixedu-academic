package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class UnitNamePart extends UnitNamePart_Base {

    public UnitNamePart(final String namePart) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setNamePart(namePart);
    }

    @Override
    public void setNamePart(final String namePart) {
	if (namePart == null) {
	    throw new DomainException("error.name.part.cannot.be.null");
	}
	final UnitNamePart unitNamePart = find(namePart);
	if (unitNamePart != null && unitNamePart != this) {
	    throw new DomainException("error.duplicate.name.part", namePart);
	}
	super.setNamePart(namePart);
    }

    public static String normalize(final String string) {
	return StringNormalizer.normalize(string.trim()).toLowerCase();
    }
    
    public static String[] getNameParts(final String name) {
	return normalize(name).split(" ");
    }

    private static final Map<String, UnitNamePart> unitNamePartIndexMap = new HashMap<String, UnitNamePart>();

    public static UnitNamePart find(final String namePart) {
	final String normalizedNamePart = StringNormalizer.normalize(namePart);

	final UnitNamePart indexedUnitNamePart = unitNamePartIndexMap.get(normalizedNamePart);
	if (indexedUnitNamePart != null) {
	    return indexedUnitNamePart;
	}

	for (final UnitNamePart unitNamePart : RootDomainObject.getInstance().getUnitNamePartSet()) {
	    final String otherUnitNamePart = unitNamePart.getNamePart();
	    if (!unitNamePartIndexMap.containsKey(otherUnitNamePart)) {
		unitNamePartIndexMap.put(otherUnitNamePart, unitNamePart);
	    }
	    if (normalizedNamePart.equals(otherUnitNamePart)) {
		return unitNamePart;
	    }
	}
	return null;
    }

    protected static UnitNamePart findAndCreateIfNotFound(final String namePart) {
	final UnitNamePart unitNamePart = find(namePart);
	return unitNamePart == null ? new UnitNamePart(namePart) : unitNamePart;
    }

    protected static void index(final UnitName unitName, final String namePart) {
	final UnitNamePart unitNamePart = findAndCreateIfNotFound(namePart);
	unitNamePart.addUnitName(unitName);
    }

    protected static void index(final UnitName unitName, final String[] nameParts) {
	for (final String namePart : nameParts) {
	    index(unitName, namePart);
	}
    }

    protected static void index(final UnitName unitName) {
	index(unitName, getNameParts(unitName.getName()));
    }

    public static void reindex(final UnitName unitName) {
	unitName.getUnitNamePartSet().clear();
	index(unitName);
    }

    public void deleteIfEmpty() {
	if (getUnitNameSet().isEmpty()) {
	    removeRootDomainObject();
	    deleteDomainObject();
	}
    }

}
