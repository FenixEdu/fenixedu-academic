package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PersonLoader extends XlsLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle, final HSSFRow header) {
		final String classname = Person.class.getName();

        addColumn(cellStyle, header, classname, "username");

        addColumn(cellStyle, header, classname, "nome");
        addColumn(cellStyle, header, classname, "gender");
        addColumn(cellStyle, header, "COUNTRY");
        addColumn(cellStyle, header, classname, "concelhoNaturalidade");
        addColumn(cellStyle, header, classname, "nascimento");
        addColumn(cellStyle, header, classname, "nacionalidade");
        addColumn(cellStyle, header, classname, "freguesiaNaturalidade");
        addColumn(cellStyle, header, classname, "maritalStatus");

        addColumn(cellStyle, header, classname, "nomePai");
        addColumn(cellStyle, header, classname, "nomeMae");

        addColumn(cellStyle, header, classname, "idDocumentType");
        addColumn(cellStyle, header, classname, "numeroDocumentoIdentificacao");
        addColumn(cellStyle, header, classname, "dataEmissaoDocumentoIdentificacao");
        addColumn(cellStyle, header, classname, "dataValidadeDocumentoIdentificacao");
        addColumn(cellStyle, header, classname, "localEmissaoDocumentoIdentificacao");

        addColumn(cellStyle, header, classname, "codigoFiscal");
        addColumn(cellStyle, header, classname, "numContribuinte");

        addColumn(cellStyle, header, classname, "telemovel");
        addColumn(cellStyle, header, classname, "telefone");

        addColumn(cellStyle, header, classname, "morada");
        addColumn(cellStyle, header, classname, "localidade");
        addColumn(cellStyle, header, classname, "localidadeCodigoPostal");
        addColumn(cellStyle, header, classname, "distritoMorada");
        addColumn(cellStyle, header, classname, "codigoPostal");
        addColumn(cellStyle, header, classname, "concelhoMorada");
        addColumn(cellStyle, header, classname, "freguesiaMorada");
        
        addColumn(cellStyle, header, classname, "email");
        addColumn(cellStyle, header, classname, "enderecoWeb");
        addColumn(cellStyle, header, classname, "availableWebSite");

        addColumn(cellStyle, header, classname, "profissao");

        addColumn(cellStyle, header, classname, "distritoNaturalidade");

        addColumn(cellStyle, header, classname, "availableEmail");
        addColumn(cellStyle, header, classname, "workPhone");
	}

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Country.class);
		addSheet(workbook, cellStyle, header);
	}

}
