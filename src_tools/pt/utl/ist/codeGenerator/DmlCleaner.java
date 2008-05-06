package pt.utl.ist.codeGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.codeGenerator.DmlCleaner.DomainModelWriter.DomainEntityWrapper;
import pt.utl.ist.codeGenerator.DmlCleaner.DomainModelWriter.ValueTypeWrapper;
import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.DomainRelation;
import dml.Role;
import dml.Slot;
import dml.Slot.Option;
import pt.ist.fenixframework.pstm.MetadataManager;

public class DmlCleaner {

    public static class DomainModelWriter {

	private static int maxTypeLength = 0;

	private final SortedSet<DomainEntityWrapper> domainEntities = new TreeSet<DomainEntityWrapper>();

	private final Map<Object, DomainEntityWrapper> domainEntityMap = new HashMap<Object, DomainEntityWrapper>();

	public abstract class DomainEntityWrapper implements Comparable<DomainEntityWrapper> {
	    protected DomainEntity domainEntity;

	    public DomainEntityWrapper(final DomainEntity domainEntity) {
		this.domainEntity = domainEntity;
	    }

	    public abstract int compareTo(final DomainEntityWrapper o);

	    public boolean isDomainClass() {
		return false;
	    }

	    public boolean isValueType() {
		return false;
	    }

	    public boolean isRelation() {
		return false;
	    }

	    public void appendIndent(final FileWriter fileWriter, final String string) throws IOException {
		for (int i = 0; i < indentLevel(); i++) {
		    fileWriter.append('\t');
		}
		fileWriter.append(string);
	    }

	    public int indentLevel() {
		return 0;
	    }

	    public abstract void write(final FileWriter fileWriter) throws IOException;
	}

	public class ValueTypeWrapper extends DomainEntityWrapper {

	    private String type;

	    private String name;

	    public ValueTypeWrapper(final String type) {
		super(null);
		this.type = type;
		maxTypeLength = Math.max(maxTypeLength, type.length());
		int i = type.lastIndexOf('.');
		this.name = type.substring(i + 1);
	    }

	    @Override
	    public boolean isValueType() {
		return true;
	    }

	    @Override
	    public int compareTo(final DomainEntityWrapper o) {
		return o.isValueType() ? type.compareTo(((ValueTypeWrapper) o).type) : -1;
	    }

	    @Override
	    public void write(final FileWriter fileWriter) throws IOException {
		if (!type.startsWith("java.lang.")) {
		    fileWriter.append("valueType ");
		    fileWriter.append(type);
		    fileWriter.append(spaceAlignments());
		    fileWriter.append("as ");
		    fileWriter.append(name);
		    fileWriter.append(";\n");
		}
	    }

	    private String spaceAlignments() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < maxTypeLength + 4 - type.length(); i++) {
		    stringBuilder.append(' ');
		}
		return stringBuilder.toString();
	    }

	    public void setName(String name) {
		this.name = name;
	    }
	}

	public class DomainClassWrapper extends DomainEntityWrapper {
	    private Set<String> keySlots = new HashSet<String>();

	    private SortedSet<Slot> slots = new TreeSet<Slot>(new Comparator<Slot>() {

		public int compare(final Slot s1, final Slot s2) {
		    return s1.getName().compareTo(s2.getName());
		}

	    });

	    @Override
	    public boolean isDomainClass() {
		return true;
	    }

	    public DomainClassWrapper(final DomainClass domainClass) {
		super(domainClass);
		for (final Role role : domainClass.getRoleSlotsList()) {
		    if (role.getMultiplicityUpper() == 1) {
			keySlots.add("key" + StringUtils.capitalize(role.getName()));
		    }
		}
		for (final Slot slot : domainClass.getSlotsList()) {
		    if (!keySlots.contains(slot.getName())) {
			slots.add(slot);
			ValueTypeWrapper valueTypeWrapper = (ValueTypeWrapper) domainEntityMap.get(slot.getType());
			if (valueTypeWrapper == null) {
			    valueTypeWrapper = new ValueTypeWrapper(slot.getType());
			    domainEntities.add(valueTypeWrapper);
			    domainEntityMap.put(slot.getType(), valueTypeWrapper);
			}
		    }
		}
	    }

	    public DomainClass getDomainClass() {
		return (DomainClass) domainEntity;
	    }

	    @Override
	    public int compareTo(final DomainEntityWrapper o) {
		if (o.isValueType()) {
		    return 1;
		}
		if (o.isRelation()) {
		    return -1;
		}
		final DomainClassWrapper oc = (DomainClassWrapper) o;
		return getCompareString().compareTo(oc.getCompareString());
	    }

	    private String getCompareString() {
		return getCompareString(getDomainClass());
	    }

	    private String getCompareString(final DomainEntity domainEntity) {
		return domainEntity == null ? "" : getParentCompareString(domainEntity) + domainEntity.getFullName();
	    }

	    private String getParentCompareString(final DomainEntity domainEntity) {
		return domainEntity instanceof DomainClass ? getCompareString(((DomainClass) domainEntity).getSuperclass()) : "";
	    }

	    @Override
	    public int indentLevel() {
		return indentLevel(getDomainClass());
	    }

	    public int indentLevel(final DomainClass domainClass) {
		if (domainClass == null) {
		    return 0;
		}
		final DomainEntity domainEntity = domainClass.getSuperclass();
		if (domainEntity == null || !(domainEntity instanceof DomainClass)) {
		    return 0;
		}
		return indentLevel((DomainClass) domainEntity) + 1;
	    }

	    @Override
	    public void write(final FileWriter fileWriter) throws IOException {
		final DomainClass domainClass = getDomainClass();
		appendIndent(fileWriter, "class ");
		fileWriter.append(domainClass.getFullName());
		final DomainEntity superClass = domainClass.getSuperclass();
		if (superClass != null) {
		    fileWriter.append(" extends ");
		    fileWriter.append(superClass.getFullName());
		}
		if (domainClass.getInterfaceNamesIterator().hasNext()) {
		    throw new Error("Interfaces not implemented...");
		    // for (final Iterator iterator =
		    // domainClass.getInterfaceNamesIterator();
		    // iterator.hasNext(); ) {
		    // final Object object = iterator.next();
		    // System.out.println("object: " +
		    // object.getClass().getName() + " " + object);
		    // }
		}
		fileWriter.append(" {\n");

		for (final Slot slot : slots) {
		    final ValueTypeWrapper valueTypeWrapper = (ValueTypeWrapper) domainEntityMap.get(slot.getType());
		    appendIndent(fileWriter, "\t");
		    fileWriter.append(valueTypeWrapper.name);
		    fileWriter.append(" ");
		    fileWriter.append(slot.getName());
		    for (final Option option : slot.getOptions()) {
			if (option == Option.REQUIRED) {
			    fileWriter.append(" (REQUIRED)");
			} else {
			    throw new Error("Unkown option: " + option);
			}
		    }
		    fileWriter.append(";\n");
		}

		appendIndent(fileWriter, "}\n\n");
	    }
	}

	public class RelationWrapper extends DomainEntityWrapper {

	    public RelationWrapper(final DomainRelation domainRelation) {
		super(domainRelation);
		final Role firstRole = domainRelation.getFirstRole();
		final Role secondRole = domainRelation.getSecondRole();
		if (firstRole.getMultiplicityUpper() == 1) {
		}
		if (secondRole.getMultiplicityUpper() == 1) {
		}
	    }

	    public DomainRelation getDomainRelation() {
		return (DomainRelation) domainEntity;
	    }

	    @Override
	    public int compareTo(final DomainEntityWrapper o) {
		return o.isRelation() ? getDomainRelation().getFullName().compareTo(
			((RelationWrapper) o).getDomainRelation().getFullName()) : 1;
	    }

	    @Override
	    public boolean isRelation() {
		return true;
	    }

	    @Override
	    public void write(final FileWriter fileWriter) throws IOException {
		final DomainRelation domainRelation = getDomainRelation();

		appendIndent(fileWriter, "relation ");
		fileWriter.append(domainRelation.getFullName());
		fileWriter.append(" {\n");
		final Role firstRole = domainRelation.getFirstRole();
		writeRole(fileWriter, firstRole);
		final Role secondRole = domainRelation.getSecondRole();
		writeRole(fileWriter, secondRole);
		fileWriter.append("}\n\n");
	    }

	    private void writeRole(final FileWriter fileWriter, final Role role) throws IOException {
		appendIndent(fileWriter, "\t");
		fileWriter.append(role.getType().getFullName());
		fileWriter.append(" playsRole ");
		fileWriter.append(role.getName());
		if (role.getMultiplicityUpper() == 1) {
		    fileWriter.append(";\n");
		} else {
		    fileWriter.append(" {\n");
		    appendIndent(fileWriter, "\t\tmultiplicity ");
		    if (role.getMultiplicityLower() == 0) {
			if (role.getMultiplicityUpper() == 1) {
			    fileWriter.append("1");
			} else if (role.getMultiplicityUpper() == Role.MULTIPLICITY_MANY) {
			    fileWriter.append("*");
			} else {
			    appendLowerAndUpperLimits(fileWriter, role);
			}
		    } else {
			appendLowerAndUpperLimits(fileWriter, role);
		    }
		    fileWriter.append(";\n");
		    appendIndent(fileWriter, "\t}\n");
		}
	    }

	    private void appendLowerAndUpperLimits(final FileWriter fileWriter, final Role role) throws IOException {
		fileWriter.append(Integer.toString(role.getMultiplicityLower()));
		fileWriter.append("..");
		fileWriter.append(Integer.toString(role.getMultiplicityUpper()));
	    }

	}

	public DomainModelWriter(final DomainModel domainModel) {
	    for (final DomainClass domainClass : domainModel.getDomainClasses()) {
		final DomainClassWrapper domainClassWrapper = new DomainClassWrapper(domainClass);
		domainEntities.add(domainClassWrapper);
		domainEntityMap.put(domainClass, domainClassWrapper);
	    }
	    for (final Iterator<DomainRelation> iterator = domainModel.getRelations(); iterator.hasNext();) {
		final DomainRelation domainRelation = iterator.next();
		final RelationWrapper relationWrapper = new RelationWrapper(domainRelation);
		domainEntities.add(relationWrapper);
		domainEntityMap.put(domainRelation, relationWrapper);
	    }
	}

	public void write(final String filename) throws IOException {
	    final FileWriter fileWriter = new FileWriter(filename);
	    for (final DomainEntityWrapper domainEntityWrapper : domainEntities) {
		domainEntityWrapper.write(fileWriter);
	    }
	    fileWriter.close();
	}
    }

    public static void main(String[] args) {
	cleanUp(args[0]);
	System.exit(0);
    }

    private static void cleanUp(final String dmlFilePath) {
	MetadataManager.init(dmlFilePath);
	final DomainModel domainModel = MetadataManager.getDomainModel();
	final DomainModelWriter domainModelWriter = new DomainModelWriter(domainModel);
	fixValueTypeNames(domainModelWriter);
	try {
	    domainModelWriter.write("/tmp/domain_model.dml");
	} catch (IOException e) {
	    throw new Error(e);
	}
    }

    private static void fixValueTypeNames(final DomainModelWriter domainModelWriter) {
	final Set<String> usedNames = new HashSet<String>();
	for (final DomainEntityWrapper domainEntityWrapper : domainModelWriter.domainEntityMap.values()) {
	    if (domainEntityWrapper.isValueType()) {
		final ValueTypeWrapper valueTypeWrapper = (ValueTypeWrapper) domainEntityWrapper;
		final String name = findUniqueNameFor(usedNames, valueTypeWrapper);
		usedNames.add(name);
		valueTypeWrapper.name = name;
	    }
	}
    }

    private static String findUniqueNameFor(final Set<String> usedNames, final ValueTypeWrapper valueTypeWrapper) {
	final String simpleName = valueTypeWrapper.name;
	if (!usedNames.contains(simpleName)) {
	    return simpleName;
	}
	final String name = valueTypeWrapper.type;
	for (int i = name.lastIndexOf('.'); i >= 0; i = name.lastIndexOf('.', i - 1)) {
	    if (i < 0) {
		return valueTypeWrapper.type;
	    }
	    final String namePart = name.substring(i + 1);
	    if (!usedNames.contains(namePart)) {
		return namePart;
	    }
	}
	throw new Error("No available name for: " + valueTypeWrapper.type);
    }

}
