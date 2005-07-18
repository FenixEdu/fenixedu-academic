package pt.utl.ist.codeGenerator.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import pt.utl.ist.codeGenerator.database.SqlTable.Column;

public class XLSGenerator {

    private final static Set<String> hiddenColumns = new HashSet<String>();
    static {
        hiddenColumns.add("ACK_OPT_LOCK");
        hiddenColumns.add("ID_INTERNAL");
    }

	public static void main(String[] args) {
        final String destinationFilename = args[0];
        final Set<String> tablesToInclude = new HashSet<String>();
        for (int i = 1; i < args.length; i++) {
            tablesToInclude.add(args[i]);
        }

		try {
			generate(destinationFilename, tablesToInclude);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Generation Complete.");
		System.exit(0);
	}

	private static void generate(final String destinationFilename, final Set<String> tablesToInclude) throws IOException {
        final HSSFWorkbook workbook = new HSSFWorkbook();

        final HSSFCellStyle cellStyle = createHeaderCellStyle(workbook);

        final Map<String, SqlTable> sqlTables = DatabaseDescriptorFactory.getSqlTables();
        for (final SqlTable sqlTable : sqlTables.values()) {
            if (tablesToInclude.contains(sqlTable.tablename)) {
                final HSSFSheet sheet = workbook.createSheet(calculateSheetName(sqlTable));
                sheet.setDefaultColumnWidth((short) 20);
                final HSSFRow row = sheet.createRow(0);

                int i = 0;
                for (final Column column : sqlTable.columns) {
                    if (!hiddenColumns.contains(column.name)) {
                        final HSSFCell cell = row.createCell((short) i++);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(column.name);
                    }
                }
            }
        }

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

    private static String calculateSheetName(final SqlTable sqlTable) {
        final String tablename = sqlTable.tablename;
        return (tablename.length() <= 31) ? tablename : tablename.substring(0, 31);
    }

}