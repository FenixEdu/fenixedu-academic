package pt.utl.ist.codeGenerator.database;

import java.io.IOException;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.utl.ist.codeGenerator.database.loaders.CategoryLoader;
import pt.utl.ist.codeGenerator.database.loaders.CountryLoader;
import pt.utl.ist.codeGenerator.database.loaders.StudentLoader;
import pt.utl.ist.codeGenerator.database.loaders.TeacherLoader;

public class XlsDump extends XlsGeneratorBase {

	public static void main(String[] args) {
        final String destinationFilename = args[0];

        try {
            dump(destinationFilename);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Dump Complete.");
        System.exit(0);
	}

	private static void dump(final String destinationFilename) throws IOException, ExcepcaoPersistencia {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final HSSFCellStyle cellStyle = createHeaderCellStyle(workbook);

        CountryLoader.dump(workbook, cellStyle);
        StudentLoader.dump(workbook, cellStyle);
        CategoryLoader.dump(workbook, cellStyle);
        TeacherLoader.dump(workbook, cellStyle);

        outputWorkbook(workbook, destinationFilename);
	}

}