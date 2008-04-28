package pt.utl.ist.codeGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.DomainRelation;
import dml.Role;
import dml.Slot;
import eu.ist.fenixframework.pstm.MetadataManager;

public class OidSqlGenerator {

    private static abstract class Writer {
	protected abstract String getFilename();

	protected abstract void writeContents(final FileWriter fileWriter) throws IOException;

	public void write() throws IOException {
	    final FileWriter fileWriter = new FileWriter(getFilename());
	    fileWriter.append("set autocommit = 0;\n");
	    fileWriter.append("begin;\n\n");

	    writeContents(fileWriter);

	    fileWriter.append("\ncommit;\n");
	    fileWriter.close();
	}
    }

    private static class AlterTableRegistry extends Writer {
	private final Map<String, Set<String>> map = new TreeMap<String, Set<String>>();

	public void addOidColumn(final String tablename) {
	    addColumn(tablename, "OID");
	}

	public void addColumn(final String tablename, final String columnName) {
	    addColumnDefinition(tablename, columnName);
	}

	private void addColumnDefinition(final String tablename, final String columnName) {
	    Set<String> columnNames = map.get(tablename);
	    if (columnNames == null) {
		columnNames = new TreeSet<String>();
		map.put(tablename, columnNames);
	    }
	    columnNames.add(columnName);
	}

	@Override
	protected String getFilename() {
	    return "/tmp/alterTables.sql";
	}

	@Override
	protected void writeContents(final FileWriter fileWriter) throws IOException {
	    for (final Entry<String, Set<String>> entry : map.entrySet()) {
		final String tablename = entry.getKey();
		final Set<String> columnNames = entry.getValue();

		fileWriter.append("alter table ");
		fileWriter.append(tablename);
		boolean isFirst = true;
		for (final String columnName : columnNames) {
		    if (isFirst) {
			isFirst = false;
		    } else {
			fileWriter.append(",");
		    }
		    fileWriter.append("\n    add column ");
		    fileWriter.append(columnName);
		    fileWriter.append(" bigint unsigned default null,\n    add index (");
		    fileWriter.append(columnName);
		    fileWriter.append(")");
		}
		fileWriter.append(";\n");
	    }
	}

    }

    private static class UpdateRegistry extends Writer {
	private final Map<String, String> mapSimple = new TreeMap<String, String>();
	private final Map<String, String> mapFamily = new TreeMap<String, String>();
	private final List<String[]> mapKeys = new ArrayList<String[]>();

	private void addClassnameAndTableForUpdate(final String domainClassName, final String tablename) {
	    mapSimple.put(domainClassName, tablename);
	}

	private void addClassnameAndTableForUpdateInHierchy(final String domainClassName, final String tablename) {
	    mapFamily.put(domainClassName, tablename);
	}

	private void addUpdateKeyInstruction(final String tablename, final String otherTablename, final String key,
		final String newKey) {
	    mapKeys.add(new String[] { tablename, otherTablename, newKey, key });
	}

	@Override
	protected String getFilename() {
	    return "/tmp/updates.sql";
	}

	protected void writeSelect(final FileWriter fileWriter, final String domainClassName, final String tablename)
		throws IOException {
	    fileWriter
		    .append("select @xpto:=null;\nselect @xpto:=DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from DOMAIN_CLASS_INFO where DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = '");
	    fileWriter.append(domainClassName);
	    fileWriter.append("';\n");
	}

	protected void writeUpdate(final FileWriter fileWriter, final String tablename) throws IOException {
	    fileWriter.append("update ");
	    fileWriter.append(tablename);
	    fileWriter.append(" set OID = (@xpto << 32) + ID_INTERNAL;\n");
	}

	protected void writeUpdate(final FileWriter fileWriter, final String domainClassName, final String tablename)
		throws IOException {
	    fileWriter.append("update ");
	    fileWriter.append(tablename);
	    fileWriter.append(" set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = '");
	    fileWriter.append(domainClassName);
	    fileWriter.append("';\n");
	}

	@Override
	protected void writeContents(final FileWriter fileWriter) throws IOException {
	    for (final Entry<String, String> entry : mapSimple.entrySet()) {
		final String domainClassName = entry.getKey();
		final String tablename = entry.getValue();

		writeSelect(fileWriter, domainClassName, tablename);
		writeUpdate(fileWriter, tablename);
	    }

	    for (final Entry<String, String> entry : mapFamily.entrySet()) {
		final String domainClassName = entry.getKey();
		final String tablename = entry.getValue();

		writeSelect(fileWriter, domainClassName, tablename);
		writeUpdate(fileWriter, domainClassName, tablename);
	    }

	    for (final String[] strings : mapKeys) {
		final String tablename = strings[0];
		final String otherTablename = strings[1];
		final String newKey = strings[2];
		final String key = strings[3];

		fileWriter.append("update ");
		fileWriter.append(tablename);
		fileWriter.append(" as t1, ");
		fileWriter.append(otherTablename);
		fileWriter.append(" as t2 set t1.");
		fileWriter.append(newKey);
		fileWriter.append(" = t2.OID where t2.ID_INTERNAL = t1.");
		fileWriter.append(key);
		fileWriter.append(";\n");
	    }

	}

    }

    private static final AlterTableRegistry alterTableRegistry = new AlterTableRegistry();
    private static final UpdateRegistry updateRegistry = new UpdateRegistry();

    public static void main(String[] args) {
	try {
	    generate(args[0]);
	} catch (IOException e) {
	    throw new Error(e);
	}
	System.exit(0);
    }

    private static void generate(final String dmlFilePath) throws IOException {
	MetadataManager.init(dmlFilePath);
	final DomainModel domainModel = MetadataManager.getDomainModel();

	for (final DomainClass domainClass : domainModel.getDomainClasses()) {
	    final int domainClassHierarchyLevel = calculateHierarchyLevel(domainClass);
	    if (domainClassHierarchyLevel == 1) {
		final Slot slot = domainClass.findSlot("ojbConcreteClass");
		if (slot == null) {
		    writeSqlInstructions(domainClass);
		} else {
		    writeSqlInstructions(domainModel, domainClass);
		}
	    }
	    writeKeySqlInstructions(domainClass);
	}

	alterTableRegistry.write();
	updateRegistry.write();
    }

    private static void writeSqlInstructions(final DomainModel domainModel, final DomainClass domainClass) throws IOException {
	final String tablename = getTableName(domainClass.getName());
	alterTableRegistry.addOidColumn(tablename);

	for (final DomainClass otherDomainClass : domainModel.getDomainClasses()) {
	    final int domainClassHierarchyLevel = calculateHierarchyLevel(otherDomainClass);
	    if (domainClassHierarchyLevel > 0) {
		final DomainClass otherDomainObjectDescendent = findDirectDomainObjectDecendent(otherDomainClass);
		if (otherDomainObjectDescendent == domainClass) {
		    updateRegistry.addClassnameAndTableForUpdateInHierchy(otherDomainClass.getFullName(), tablename);
		}
	    }
	}
    }

    private static void writeSqlInstructions(final DomainClass domainClass) throws IOException {
	final String tablename = getTableName(domainClass.getName());
	alterTableRegistry.addOidColumn(tablename);
	updateRegistry.addClassnameAndTableForUpdate(domainClass.getFullName(), tablename);
    }

    private static void writeKeySqlInstructions(final DomainClass domainClass) throws IOException {
	final String tablename = getTopLevelTableName(domainClass);
	if (tablename == null) {
	    return;
	}

	for (final Role role : domainClass.getRoleSlotsList()) {
	    final String otherTablename = getTopLevelTableName((DomainClass) role.getType());

	    if (role.getMultiplicityUpper() == 1) {
		final String key = getKeyFromRole(role);
		final String newKey = getNewKeyFromRole(role);

		alterTableRegistry.addColumn(tablename, newKey);

		updateRegistry.addUpdateKeyInstruction(tablename, otherTablename, key, newKey);
	    } else if (role.getMultiplicityUpper() == -1) {
		if (role.getOtherRole().getMultiplicityUpper() == -1) {
		    writeIndirectionKeySqlInstructions(role.getRelation());
		}
	    } else if (role.getOtherRole().getMultiplicityUpper() != 1) {
		// Probably never happens because people don't use it;
		// if it does we need to know about it now...
		throw new Error("Not yet suported... place implementation here.");
	    }
	}
    }

    private static final Set<DomainRelation> processedRelations = new HashSet<DomainRelation>();

    private static void writeIndirectionKeySqlInstructions(final DomainRelation relation) throws IOException {
	if (processedRelations.contains(relation)) {
	    return;
	}
	processedRelations.add(relation);

	final String tablename = getTableName(relation.getFullName());

	for (final Role role : relation.getRoles()) {
	    final String newKey = getNewIndirectionKeyFromRole(role);
	    alterTableRegistry.addColumn(tablename, newKey);
	}

	for (final Role role : relation.getRoles()) {
	    final String otherTablename = getTopLevelTableName((DomainClass) role.getType());
	    final String key = getIndirectionKeyFromRole(role);
	    final String newKey = getNewIndirectionKeyFromRole(role);
	    updateRegistry.addUpdateKeyInstruction(tablename, otherTablename, key, newKey);
	}
    }

    private static DomainClass findDirectDomainObjectDecendent(final DomainClass domainClass) {
	final int domainClassHierarchyLevel = calculateHierarchyLevel(domainClass);
	return domainClassHierarchyLevel == 1 ? domainClass : findDirectDomainObjectDecendent((DomainClass) domainClass
		.getSuperclass());
    }

    private static int calculateHierarchyLevel(final DomainClass domainClass) {
	final DomainEntity domainEntity = domainClass.getSuperclass();
	return domainEntity == null || !isDomainClass(domainEntity) ? 0 : calculateHierarchyLevel((DomainClass) domainEntity) + 1;
    }

    private static boolean isDomainClass(final DomainEntity domainEntity) {
	return domainEntity instanceof DomainClass;
    }

    private static String getSqlName(final String name) {
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

    private static String getTopLevelTableName(final DomainClass domainClass) {
	final int domainClassHierarchyLevel = calculateHierarchyLevel(domainClass);
	if (domainClassHierarchyLevel == 1) {
	    return getTableName(domainClass.getName());
	}
	if (domainClass.getSuperclass() == null) {
	    return null;
	}
	return getTopLevelTableName((DomainClass) domainClass.getSuperclass());
    }

    private static String getTableName(final String name) {
	return getSqlName(name);
    }

    private static String getAppendedSqlName(final String prefix, final String name) {
	return prefix + getSqlName(name);
    }

    private static String getKeyFromRole(final Role role) {
	return getAppendedSqlName("KEY_", role.getName());
    }

    private static String getNewKeyFromRole(final Role role) {
	return getAppendedSqlName("OID_", role.getName());
    }

    private static String getIndirectionKeyFromRole(final Role role) {
	final String newKey = getAppendedSqlName("KEY_", role.getType().getName());
	return hasMutualTypeInRelation(role) ? newKey + "_" + getSqlName(role.getName()) : newKey;
    }

    private static String getNewIndirectionKeyFromRole(final Role role) {
	final String newKey = getAppendedSqlName("OID_", role.getType().getName());
	return hasMutualTypeInRelation(role) ? newKey + "_" + getSqlName(role.getName()) : newKey;
    }

    private static boolean hasMutualTypeInRelation(final Role role) {
	final DomainEntity domainEntity = role.getType();
	for (final Role otherRole : role.getRelation().getRoles()) {
	    final DomainEntity otherDomainEntity = otherRole.getType();
	    if (!role.getName().equals(otherRole.getName()) && domainEntity.getFullName().equals(otherDomainEntity.getFullName())) {
		return true;
	    }
	}
	return false;
    }

}
