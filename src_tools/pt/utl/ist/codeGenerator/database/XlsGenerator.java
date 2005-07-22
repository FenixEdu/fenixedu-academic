package pt.utl.ist.codeGenerator.database;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.utl.ist.codeGenerator.database.loaders.CategoryLoader;
import pt.utl.ist.codeGenerator.database.loaders.CountryLoader;
import pt.utl.ist.codeGenerator.database.loaders.StudentLoader;
import pt.utl.ist.codeGenerator.database.loaders.TeacherLoader;

public class XlsGenerator extends XlsGeneratorBase {

	public static void main(String[] args) {
        final String destinationFilename = args[0];

		try {
			generate(destinationFilename);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Generation Complete.");
		System.exit(0);
	}

	private static void generate(final String destinationFilename) throws IOException {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final HSSFCellStyle cellStyle = createHeaderCellStyle(workbook);

        CountryLoader.addSheet(workbook, cellStyle);
        StudentLoader.addSheet(workbook, cellStyle);
        CategoryLoader.addSheet(workbook, cellStyle);
        TeacherLoader.addSheet(workbook, cellStyle);

        outputWorkbook(workbook, destinationFilename);
	}

}