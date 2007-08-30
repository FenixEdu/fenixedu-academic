package pt.utl.ist.codeGenerator;

import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.fenixedu._development.MetadataManager;
import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
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

    private static void generate(final String dmlFilePath) throws IOException {
	fileWriterAlterTables = createFileWriter("/tmp/alterTables.sql");
	fileWriterUpdates = new FileWriter("/tmp/updates.sql");

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

}
