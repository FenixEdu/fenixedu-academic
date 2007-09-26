package pt.utl.ist.codeGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.MetadataManager;
import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.DomainRelation;
import dml.Role;
import dml.Slot;

public class OidSqlGenerator {

    public static void main(String[] args) {
	try {
	    generate(args[0]);
	} catch (IOException e) {
	    throw new Error(e);
	}
	System.exit(0);
    }

    static FileWriter fileWriterAlterTables = null;
    static FileWriter fileWriterUpdates = null;
    static FileWriter fileWriterUpdateKeys = null;

    private static void generate(final String dmlFilePath) throws IOException {
	fileWriterAlterTables = createFileWriter("/tmp/alterTables.sql");
	fileWriterUpdates = new FileWriter("/tmp/updates.sql");
	fileWriterUpdateKeys = new FileWriter("/tmp/updateKeys.sql");

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
	closeFile(fileWriterAlterTables);
	closeFile(fileWriterUpdates);
    }

    private static FileWriter createFileWriter(final String filename) throws IOException {
	final FileWriter fileWriter = new FileWriter(filename);
	fileWriter.append("set autocommit = 0;\n");
	fileWriter.append("begin;\n\n");
	return fileWriter;
    }

    private static void closeFile(final FileWriter fileWriter) throws IOException {
	fileWriter.append("\ncommit;\n");
	fileWriter.close();
    }

    private static void writeSqlInstructions(final DomainModel domainModel, final DomainClass domainClass) throws IOException {
	final String tablename = getTableName(domainClass.getName());
	writeAlterTable(tablename);

	for (final DomainClass otherDomainClass : domainModel.getDomainClasses()) {
	    final int domainClassHierarchyLevel = calculateHierarchyLevel(otherDomainClass);
	    if (domainClassHierarchyLevel > 0) {
		final DomainClass otherDomainObjectDescendent = findDirectDomainObjectDecendent(otherDomainClass);
		if (otherDomainObjectDescendent == domainClass) {
		    fileWriterUpdates.append("select @xpto:=null;\nselect @xpto:=DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from DOMAIN_CLASS_INFO where DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = '");
		    fileWriterUpdates.append(otherDomainClass.getFullName());
		    fileWriterUpdates.append("';\n");

		    fileWriterUpdates.append("update ");
		    fileWriterUpdates.append(tablename);
		    fileWriterUpdates.append(" set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = '");
		    fileWriterUpdates.append(otherDomainClass.getFullName());
		    fileWriterUpdates.append("';\n");
		}
	    }
	}
    }

    private static void writeAlterTable(final String tablename) throws IOException {
	fileWriterAlterTables.append("alter table ");
	fileWriterAlterTables.append(tablename);
	fileWriterAlterTables.append(" add column OID bigint unsigned default null;\n");
    }

    private static void writeSqlInstructions(final DomainClass domainClass) throws IOException {
	final String tablename = getTableName(domainClass.getName());
	writeAlterTable(tablename);

	fileWriterUpdates.append("select @xpto:=null;\nselect @xpto:=DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from DOMAIN_CLASS_INFO where DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = '");
	fileWriterUpdates.append(domainClass.getFullName());
	fileWriterUpdates.append("';\n");

	fileWriterUpdates.append("update ");
	fileWriterUpdates.append(tablename);
	fileWriterUpdates.append(" set OID = (@xpto << 32) + ID_INTERNAL;\n");
    }

    private static void writeKeySqlInstructions(final DomainClass domainClass) throws IOException {
        final String tablename = getTopLevelTableName(domainClass);
        if (tablename == null) {
            return;
        }

        for (final Role role : domainClass.getRoleSlotsList()) {
            final String otherTablename = getTableName(role.getType().getName());

            if (role.getMultiplicityUpper() == 1) {
                final String key = getKeyFromRole(role);
                final String newKey = getNewKeyFromRole(role);

                fileWriterUpdateKeys.append("alter table ");
                fileWriterUpdateKeys.append(tablename);
                fileWriterUpdateKeys.append(" add column ");
                fileWriterUpdateKeys.append(newKey);
                fileWriterUpdateKeys.append(" bigint unsigned default null;\n");

                appendUpdateKeyInstruction(tablename, otherTablename, key, newKey);
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

        fileWriterUpdateKeys.append("alter table ");
        fileWriterUpdateKeys.append(tablename);
        boolean isFirst = true;
        for (final Role role : relation.getRoles()) {
            if (isFirst) {
                isFirst = false;
            } else {
                fileWriterUpdateKeys.append(",");
            }

            final String newKey = getNewIndirectionKeyFromRole(role);
            fileWriterUpdateKeys.append(" add column ");
            fileWriterUpdateKeys.append(newKey);
            fileWriterUpdateKeys.append(" bigint unsigned default null");

        }
        fileWriterUpdateKeys.append(";\n");

        for (final Role role : relation.getRoles()) {
            final String otherTablename = getTableName(role.getType().getName());
            final String key = getIndirectionKeyFromRole(role);
            final String newKey = getNewIndirectionKeyFromRole(role);
            appendUpdateKeyInstruction(tablename, otherTablename, key, newKey);
        }
    }

    private static void appendUpdateKeyInstruction(final String tablename, final String otherTablename, final String key, final String newKey) throws IOException {
        fileWriterUpdateKeys.append("update ");
        fileWriterUpdateKeys.append(tablename);
        fileWriterUpdateKeys.append(" as t1, ");
        fileWriterUpdateKeys.append(otherTablename);
        fileWriterUpdateKeys.append(" as t2 set t1.");
        fileWriterUpdateKeys.append(newKey);
        fileWriterUpdateKeys.append(" = t2.OID where t2.ID_INTERNAL = t1.");
        fileWriterUpdateKeys.append(key);
        fileWriterUpdateKeys.append(";\n");
    }

    private static DomainClass findDirectDomainObjectDecendent(final DomainClass domainClass) {
        final int domainClassHierarchyLevel = calculateHierarchyLevel(domainClass);
        return domainClassHierarchyLevel == 1 ? domainClass : findDirectDomainObjectDecendent((DomainClass) domainClass.getSuperclass());
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
        return getAppendedSqlName("KEY_", role.getType().getName());
    }

    private static String getNewIndirectionKeyFromRole(final Role role) {
        return getAppendedSqlName("OID_", role.getType().getName());
    }

}
