package net.sourceforge.fenixedu.domain.phd.access;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

public class PhdProcessAccessTypeList implements Serializable {

    public static final PhdProcessAccessTypeList EMPTY = new EmptyPhdProcessAccessList();

    private static class EmptyPhdProcessAccessList extends PhdProcessAccessTypeList {

	static private final long serialVersionUID = 1L;

	@Override
	public Set<PhdProcessAccessType> getTypes() {
	    return Collections.emptySet();
	}

	@Override
	public String toString() {
	    return "";
	}

    }

    static private final long serialVersionUID = 1L;

    private final Set<PhdProcessAccessType> types = new TreeSet<PhdProcessAccessType>();

    private PhdProcessAccessTypeList() {
    }

    private PhdProcessAccessTypeList(final String types) {
	super();
	this.types.addAll(convertToSet(types));
    }

    public PhdProcessAccessTypeList(Collection<PhdProcessAccessType> types) {
	super();
	this.types.addAll(types);
    }

    private Set<PhdProcessAccessType> convertToSet(String types) {
	final Set<PhdProcessAccessType> result = new HashSet<PhdProcessAccessType>();

	for (final String each : types.split(",")) {
	    String valueToParse = each.trim();
	    if (!StringUtils.isEmpty(valueToParse)) {
		result.add(PhdProcessAccessType.valueOf(valueToParse));
	    }
	}

	return result;

    }

    private String convertToString(Set<PhdProcessAccessType> types) {
	final StringBuilder result = new StringBuilder();

	for (PhdProcessAccessType each : types) {
	    result.append(each.name()).append(",");
	}

	if (result.length() > 0 && result.charAt(result.length() - 1) == ',') {
	    result.deleteCharAt(result.length() - 1);
	}

	return result.toString();
    }

    public Set<PhdProcessAccessType> getTypes() {
	return Collections.unmodifiableSet(types);
    }

    @Override
    public String toString() {
	return convertToString(this.types);
    }

    public PhdProcessAccessTypeList addAccessTypes(PhdProcessAccessType... types) {
	final PhdProcessAccessTypeList result = new PhdProcessAccessTypeList();
	result.types.addAll(Arrays.asList(types));
	result.types.addAll(getTypes());

	return result;
    }

    static public PhdProcessAccessTypeList importFromString(String value) {
	return StringUtils.isEmpty(value) ? EMPTY : new PhdProcessAccessTypeList(value);
    }

}
