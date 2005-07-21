package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PersonLoader extends XlsLoader {

    protected PersonLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle, final Class domainObjectClass) {
        super(workbook, cellStyle, domainObjectClass.getSimpleName());

        addColumn("username");

        addColumn("nome");
        addColumn("gender");
        addKeyColumn("COUNTRY");
        addColumn("concelhoNaturalidade");
        addColumn("nascimento");
        addColumn("nacionalidade");
        addColumn("freguesiaNaturalidade");
        addColumn("maritalStatus");

        addColumn("nomePai");
        addColumn("nomeMae");

        addColumn("idDocumentType");
        addColumn("numeroDocumentoIdentificacao");
        addColumn("dataEmissaoDocumentoIdentificacao");
        addColumn("dataValidadeDocumentoIdentificacao");
        addColumn("localEmissaoDocumentoIdentificacao");

        addColumn("codigoFiscal");
        addColumn("numContribuinte");

        addColumn("telemovel");
        addColumn("telefone");

        addColumn("morada");
        addColumn("localidade");
        addColumn("localidadeCodigoPostal");
        addColumn("distritoMorada");
        addColumn("codigoPostal");
        addColumn("concelhoMorada");
        addColumn("freguesiaMorada");
        
        addColumn("email");
        addColumn("enderecoWeb");
        addColumn("availableWebSite");

        addColumn("profissao");

        addColumn("distritoNaturalidade");

        addColumn("availableEmail");
        addColumn("workPhone");
    }

    private void addColumn(final String property) {
        addColumn(Person.class.getName(), property);
    }

    public PersonLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        this(workbook, cellStyle, Person.class);
    }

}
