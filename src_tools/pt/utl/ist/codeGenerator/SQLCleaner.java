package pt.utl.ist.codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;

import pt.utl.ist.codeGenerator.database.DatabaseDescriptorFactory;

public class SQLCleaner {

    public static void main(String[] args) {
	try {
	    MetadataManager.init("build/WEB-INF/classes/domain_model.dml");
	    SuportePersistenteOJB.fixDescriptors();
	    generate(args[0]);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	System.out.println("Generation Complete.");
	System.exit(0);
    }

    private static void generate(final String destinationFilename) throws IOException {
	final StringBuilder stringBuilder = new StringBuilder();

        final Map<String, ClassDescriptor> classDescriptorMap = DatabaseDescriptorFactory.getDescriptorTable();
        for (final ClassDescriptor classDescriptor : classDescriptorMap.values()) {
            final String tableName = classDescriptor.getFullTableName();
            if (tableName != null && !tableName.startsWith("OJB")) {
        	stringBuilder.append("alter table ");
        	stringBuilder.append(tableName);
        	stringBuilder.append(" drop column ACK_OPT_LOCK;\n");
        	stringBuilder.append("alter table ");
        	stringBuilder.append(tableName);
        	stringBuilder.append(" drop column ack_opt_lock;\n");

                for (final Iterator iterator = classDescriptor.getCollectionDescriptors().iterator(); iterator.hasNext();) {
                    final CollectionDescriptor collectionDescriptor = (CollectionDescriptor) iterator.next();
                    final String indirectionTablename = collectionDescriptor.getIndirectionTable();
                    if (indirectionTablename != null) {
                	stringBuilder.append("rename table ");
                	stringBuilder.append(indirectionTablename);
                	stringBuilder.append(" to ");
                	stringBuilder.append(getTempTableName(indirectionTablename));
                	stringBuilder.append(";\n");

                	stringBuilder.append("create table ");
                	stringBuilder.append(indirectionTablename);
                	stringBuilder.append(" (");
                	stringBuilder.append(collectionDescriptor.getFksToThisClass()[0]);
                	stringBuilder.append(" int(11) not null, ");
                	stringBuilder.append(collectionDescriptor.getFksToItemClass()[0]);
                	stringBuilder.append(" int(11) not null, ");
                	stringBuilder.append(" primary key (");
                	stringBuilder.append(collectionDescriptor.getFksToThisClass()[0]);
                	stringBuilder.append(", ");
                	stringBuilder.append(collectionDescriptor.getFksToItemClass()[0]);
                	stringBuilder.append("), key(");
                	stringBuilder.append(collectionDescriptor.getFksToThisClass()[0]);
                	stringBuilder.append("), key(");
                	stringBuilder.append(collectionDescriptor.getFksToItemClass()[0]);
                	stringBuilder.append(")");
                	stringBuilder.append(") type=InnoDB;\n");

                	stringBuilder.append("insert into ");
                	stringBuilder.append(indirectionTablename);
                	stringBuilder.append(" (");
                	stringBuilder.append(collectionDescriptor.getFksToThisClass()[0]);
                	stringBuilder.append(", ");
                	stringBuilder.append(collectionDescriptor.getFksToItemClass()[0]);
                	stringBuilder.append(") select ");
                	stringBuilder.append(collectionDescriptor.getFksToThisClass()[0]);
                	stringBuilder.append(", ");
                	stringBuilder.append(collectionDescriptor.getFksToItemClass()[0]);
                	stringBuilder.append(" from ");
                	stringBuilder.append(getTempTableName(indirectionTablename));
                	stringBuilder.append(";\n");

                	stringBuilder.append("drop table ");
                	stringBuilder.append(getTempTableName(indirectionTablename));
                	stringBuilder.append(";\n");
                    }
                }
            } else {
        	System.out.println("Skipping ojb table: " + tableName);
            }
        }

	writeFile(destinationFilename, stringBuilder.toString());
    }

    private static String getTempTableName(String indirectionTablename) {
	return "_" + indirectionTablename + "_";
    }

    public static void writeFile(final String filename, final String fileContents) throws IOException {
	final File file = new File(filename);
	if (!file.exists()) {
	    file.createNewFile();
	}

	final FileWriter fileWriter = new FileWriter(file, false);

	fileWriter.write(fileContents);
	fileWriter.close();
    }

}