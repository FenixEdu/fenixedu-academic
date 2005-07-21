package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PersonLoader extends BaseLoader {

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

	public static void load(final HSSFWorkbook workbook) {
		load(workbook, calculateSheetName(Person.class));
	}

	public static void load(final HSSFWorkbook workbook, final String sheetname) {
		PersistenceBrokerFactory.defaultPersistenceBroker().beginTransaction();

		final HSSFSheet sheet = workbook.getSheet(sheetname);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String username = row.getCell((short) 1).getStringCellValue();
			final String name = row.getCell((short) 2).getStringCellValue();
			final String gender = row.getCell((short) 3).getStringCellValue();
			final String COUNTRY = row.getCell((short) 4).getStringCellValue();
			final String concelhoNaturalidade = row.getCell((short) 5).getStringCellValue();
			final String nascimento = row.getCell((short) 6).getStringCellValue();
			final String nacionalidade = row.getCell((short) 7).getStringCellValue();
			final String freguesiaNaturalidade = row.getCell((short) 8).getStringCellValue();
			final String maritalStatus = row.getCell((short) 9).getStringCellValue();
			final String nomePai = row.getCell((short) 10).getStringCellValue();
			final String nomeMae = row.getCell((short) 11).getStringCellValue();
			final String idDocumentType = row.getCell((short) 12).getStringCellValue();
			final String numeroDocumentoIdentificacao = row.getCell((short) 13).getStringCellValue();
			final String dataEmissaoDocumentoIdentificacao = row.getCell((short) 14).getStringCellValue();
			final String dataValidadeDocumentoIdentificacao = row.getCell((short) 15).getStringCellValue();
			final String localEmissaoDocumentoIdentificacao = row.getCell((short) 16).getStringCellValue();
			final String codigoFiscal = row.getCell((short) 17).getStringCellValue();
			final String numContribuinte = row.getCell((short) 18).getStringCellValue();
			final String telemovel = row.getCell((short) 19).getStringCellValue();
			final String telefone = row.getCell((short) 20).getStringCellValue();
			final String morada = row.getCell((short) 21).getStringCellValue();
			final String localidade = row.getCell((short) 22).getStringCellValue();
			final String localidadeCodigoPostal = row.getCell((short) 23).getStringCellValue();
			final String distritoMorada = row.getCell((short) 24).getStringCellValue();
			final String codigoPostal = row.getCell((short) 25).getStringCellValue();
			final String concelhoMorada = row.getCell((short) 26).getStringCellValue();
			final String freguesiaMorada = row.getCell((short) 27).getStringCellValue();
			final String email = row.getCell((short) 28).getStringCellValue();
			final String enderecoWeb = row.getCell((short) 29).getStringCellValue();
			final String availableWebSite = row.getCell((short) 30).getStringCellValue();
			final String profissao = row.getCell((short) 31).getStringCellValue();
			final String distritoNaturalidade = row.getCell((short) 32).getStringCellValue();
			final String availableEmail = row.getCell((short) 33).getStringCellValue();
			final String workPhone = row.getCell((short) 34).getStringCellValue();
		}

		PersistenceBrokerFactory.defaultPersistenceBroker().commitTransaction();
	}

}
