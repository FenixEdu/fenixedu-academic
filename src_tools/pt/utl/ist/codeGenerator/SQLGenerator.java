package pt.utl.ist.codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.codeGenerator.database.DatabaseDescriptorFactory;
import pt.utl.ist.codeGenerator.database.SqlTable;

public class SQLGenerator {

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

        final Map<String, SqlTable> sqlTables = DatabaseDescriptorFactory.getSqlTables();
        for (final SqlTable sqlTable : sqlTables.values()) {
            sqlTable.appendCreateTableMySql(stringBuilder);
            stringBuilder.append("\n\n");            
        }

        writeFile(destinationFilename, stringBuilder.toString());
	}

    public static void writeFile(final String filename, final String fileContents)
            throws IOException {
        final File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }

        final FileWriter fileWriter = new FileWriter(file, false);

        fileWriter.write(fileContents);
        fileWriter.close();
    }

}