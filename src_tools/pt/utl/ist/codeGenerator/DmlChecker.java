package pt.utl.ist.codeGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;

import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.Role;
import dml.Slot;
import eu.ist.fenixframework.pstm.MetadataManager;

public class DmlChecker {

    public static void main(String[] args) {
	try {
	    check(args[0]);
	} catch (IOException e) {
	    throw new Error(e);
	}
	System.exit(0);
    }

    static int totalDomainObjects = 0;
    static int correctTables = 0;
    static int incorrectTables = 0;
    static int fixableWithTableRename = 0;

    static Map<String, Set<String>> tableMap = new TreeMap<String, Set<String>>();

    static FileWriter fileWriter;

    static final Set<String> fixedTables = new HashSet<String>();

    private static void check(final String dmlFilePath) throws IOException {
	fileWriter = new FileWriter("/tmp/renameTables.sql");
	MetadataManager.init(dmlFilePath);
	final DomainModel domainModel = MetadataManager.getDomainModel();
	final DescriptorRepository descriptorRepository = MetadataManager.getOjbMetadataManager().getGlobalRepository();
	for (final DomainClass domainClass : domainModel.getDomainClasses()) {
	    final DomainEntity domainEntity = domainClass.getSuperclass();
	    for (final Slot slot : domainClass.getSlotsList()) {
		checkSuperClassSlot(domainClass.getName(), domainEntity, slot);
	    }
	    for (final Role role : domainClass.getRoleSlotsList()) {
		checkSuperClassRole(domainClass.getName(), domainEntity, role);
	    }
	    if (!domainClass.getFullName().equals("net.sourceforge.fenixedu.domain.DomainObject")) {
		totalDomainObjects++;
		checkTableName(descriptorRepository, domainClass);
	    }
	}
	System.out.println("Total domain objects: " + totalDomainObjects);
	System.out.println("Correct tables: " + correctTables);
	System.out.println("Incorrect tables: " + incorrectTables);
	System.out.println("Fixable tables: " + fixableWithTableRename);

	for (final Entry<String, Set<String>> entry : tableMap.entrySet()) {
	    if (entry.getValue().size() != 1) {
		System.out
			.println("Table " + entry.getKey() + " is mapped to " + entry.getValue().size() + " top level classes!");
	    }
	}
	fileWriter.close();
    }

    private static void checkTableName(final DescriptorRepository descriptorRepository, final DomainClass domainClass)
	    throws IOException {
	final String expectedTableName = getExpectedTableName(domainClass);
	final String currentTableName = getCurrentTableName(descriptorRepository, domainClass);
	final ClassDescriptor topLevelClassDescriptor = getTopLevelClassDescriptor(descriptorRepository, domainClass);
	if (expectedTableName == null || !expectedTableName.equals(currentTableName)) {
	    incorrectTables++;
	    // System.out.println("For class " +
	    // StringUtils.substringAfter(domainClass.getFullName(),
	    // "net.sourceforge.fenixedu.domain.") + " expected table " +
	    // expectedTableName + " but has table " + currentTableName);
	    renameTable(expectedTableName, currentTableName);

	    if (currentTableName != null && currentTableName.equals(topLevelClassDescriptor.getFullTableName())) {
		fixableWithTableRename++;
	    } else {
		System.out.println("Unable to fix table name for class " + domainClass.getName() + " expected table "
			+ expectedTableName + " but has table " + currentTableName);
	    }
	} else {
	    correctTables++;
	}

	Set<String> objectNames = tableMap.get(expectedTableName);
	if (objectNames == null) {
	    objectNames = new TreeSet<String>();
	    tableMap.put(expectedTableName, objectNames);
	}
	objectNames.add(topLevelClassDescriptor.getClassNameOfObject());
    }

    private static void renameTable(final String expectedTableName, final String currentTableName) throws IOException {
	if (!fixedTables.contains(currentTableName)) {
	    fileWriter.append("alter table ");
	    fileWriter.append(currentTableName);
	    fileWriter.append(" rename to ");
	    fileWriter.append(expectedTableName);
	    fileWriter.append(";\n");
	    fixedTables.add(currentTableName);
	}
    }

    private static ClassDescriptor getTopLevelClassDescriptor(final DescriptorRepository descriptorRepository,
	    final DomainClass domainClass) {
	if (domainClass.getSuperclass() == null
		|| (domainClass.getSuperclass() instanceof DomainClass && domainClass.getSuperclass().getFullName().equals(
			"net.sourceforge.fenixedu.domain.DomainObject"))) {
	    return descriptorRepository.getDescriptorFor(domainClass.getFullName());
	}
	return getTopLevelClassDescriptor(descriptorRepository, (DomainClass) domainClass.getSuperclass());
    }

    private static String getCurrentTableName(final DescriptorRepository descriptorRepository, final DomainClass domainClass) {
	final ClassDescriptor classDescriptor = descriptorRepository.getDescriptorFor(domainClass.getFullName());
	return classDescriptor == null ? null : classDescriptor.getFullTableName();
    }

    private static String getExpectedTableName(final DomainClass domainClass) {
	if (domainClass.getFullName().equals("net.sourceforge.fenixedu.domain.DomainObject")) {
	    return null;
	}
	if (domainClass.getSuperclass() == null
		|| (domainClass.getSuperclass() instanceof DomainClass && domainClass.getSuperclass().getFullName().equals(
			"net.sourceforge.fenixedu.domain.DomainObject"))) {
	    return getTableName(domainClass.getName());
	}
	return domainClass.getSuperclass() instanceof DomainClass ? getExpectedTableName((DomainClass) domainClass
		.getSuperclass()) : null;
    }

    private static String getTableName(final String name) {
	final StringBuilder stringBuilder = new StringBuilder();
	boolean isFirst = true;
	for (final char c : name.toCharArray()) {
	    if (isFirst) {
		isFirst = false;
		stringBuilder.append(Character.toUpperCase(c));
	    } else {
		if (Character.isUpperCase(c)) {
		    stringBuilder.append('_');
		    stringBuilder.append(c);
		} else {
		    stringBuilder.append(Character.toUpperCase(c));
		}
	    }
	}
	return stringBuilder.toString();
    }

    private static void checkSuperClassSlot(final String domainClassname, final DomainEntity domainEntity, final Slot slot) {
	if (domainEntity instanceof DomainClass) {
	    final DomainClass domainClass = (DomainClass) domainEntity;
	    for (final Slot otherSlot : domainClass.getSlotsList()) {
		if (slot.getName().equals(otherSlot.getName())) {
		    System.out.println(domainClassname + " redefines slot " + slot.getName() + " from its parent "
			    + domainClass.getName());
		}
	    }
	    checkSuperClassSlot(domainClassname, domainClass.getSuperclass(), slot);
	}
    }

    private static void checkSuperClassRole(final String domainClassname, final DomainEntity domainEntity, final Role role) {
	if (domainEntity instanceof DomainClass) {
	    final DomainClass domainClass = (DomainClass) domainEntity;
	    for (final Role otherRole : domainClass.getRoleSlotsList()) {
		if (role.getName().equals(otherRole.getName())) {
		    System.out.println(domainClassname + " redefines role slot " + role.getName() + " from its parent "
			    + domainClass.getName());
		}
	    }
	    checkSuperClassRole(domainClassname, domainClass.getSuperclass(), role);
	}
    }

}
