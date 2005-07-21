package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CountryLoader extends XlsLoader {

    public CountryLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        super(workbook, cellStyle, Country.class.getSimpleName());

        addColumn("name");
        addColumn("code");
        addColumn("nationality");
    }

    private void addColumn(final String property) {
        addColumn(Country.class.getName(), property);
    }

}
