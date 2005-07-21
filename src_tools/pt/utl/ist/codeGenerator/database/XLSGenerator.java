package pt.utl.ist.codeGenerator.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import pt.utl.ist.codeGenerator.database.loaders.CategoryLoader;
import pt.utl.ist.codeGenerator.database.loaders.CountryLoader;
import pt.utl.ist.codeGenerator.database.loaders.StudentLoader;
import pt.utl.ist.codeGenerator.database.loaders.TeacherLoader;

public class XLSGenerator {

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

        final OutputStream outputStream = new FileOutputStream(destinationFilename);
        workbook.write(outputStream);
        outputStream.close();
	}

    private static HSSFCellStyle createHeaderCellStyle(final HSSFWorkbook workbook) {
        final HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        return cellStyle;
    }

}