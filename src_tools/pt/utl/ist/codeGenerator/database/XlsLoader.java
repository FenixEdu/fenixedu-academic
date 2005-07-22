package pt.utl.ist.codeGenerator.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.utl.ist.codeGenerator.database.loaders.CategoryLoader;
import pt.utl.ist.codeGenerator.database.loaders.CountryLoader;
import pt.utl.ist.codeGenerator.database.loaders.StudentLoader;
import pt.utl.ist.codeGenerator.database.loaders.TeacherLoader;

public class XlsLoader {

	public static void main(String[] args) {
        final String sourceFilename = args[0];

		try {
			load(sourceFilename);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Load Complete.");
		System.exit(0);
	}

	private static void load(final String sourceFilename) throws IOException {
        final InputStream inputStream = new FileInputStream(sourceFilename);
		final HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        inputStream.close();

        CountryLoader.load(workbook);
        CategoryLoader.load(workbook);
        StudentLoader.load(workbook);
        TeacherLoader.load(workbook);
	}

}