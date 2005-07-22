package pt.utl.ist.codeGenerator.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class XlsGeneratorBase {

	protected static void outputWorkbook(final HSSFWorkbook workbook, final String destinationFilename)
            throws IOException {
        final OutputStream outputStream = new FileOutputStream(destinationFilename);
        workbook.write(outputStream);
        outputStream.close();
	}

    protected static HSSFCellStyle createHeaderCellStyle(final HSSFWorkbook workbook) {
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