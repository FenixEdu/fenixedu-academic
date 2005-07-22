package pt.utl.ist.codeGenerator.database.loaders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
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
        
        addColumn(cellStyle, header, classname, "nascimento");
        addColumn(cellStyle, header, classname, "nacionalidade");
        addColumn(cellStyle, header, classname, "freguesiaNaturalidade");
        addColumn(cellStyle, header, classname, "concelhoNaturalidade");
        addColumn(cellStyle, header, classname, "distritoNaturalidade");
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

        addColumn(cellStyle, header, classname, "availableEmail");
        addColumn(cellStyle, header, classname, "workPhone");
	}

	public static void load(final HSSFWorkbook workbook, final Map<String, Country> countries)
            throws ExcepcaoPersistencia {
		load(workbook, calculateSheetName(Person.class), countries);
	}

	public static Map<String, Person> load(final HSSFWorkbook workbook, final String sheetname, final Map<String, Country> countries)
            throws ExcepcaoPersistencia {
        final Map<String, Person> people = new HashMap<String, Person>();

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

        final IPersistentRole persistentRole = persistentSupport.getIPersistentRole();
        final IRole personRole = persistentRole.readByRoleType(RoleType.PERSON);
        final List<IPerson> peopleWithPersonRole = personRole.getAssociatedPersons();

		final HSSFSheet sheet = workbook.getSheet(sheetname);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String username = row.getCell((short) 0).getStringCellValue();
			final String name = row.getCell((short) 1).getStringCellValue();
			final String gender = row.getCell((short) 2).getStringCellValue();
			final String COUNTRY = row.getCell((short) 3).getStringCellValue();
			final Date nascimento = row.getCell((short) 4).getDateCellValue();
			final String nacionalidade = row.getCell((short) 5).getStringCellValue();
			final String freguesiaNaturalidade = row.getCell((short) 6).getStringCellValue();
            final String concelhoNaturalidade = row.getCell((short) 7).getStringCellValue();
            final String distritoNaturalidade = row.getCell((short) 8).getStringCellValue();
			final String maritalStatus = row.getCell((short) 9).getStringCellValue();
			final String nomePai = row.getCell((short) 10).getStringCellValue();
			final String nomeMae = row.getCell((short) 11).getStringCellValue();
			final String idDocumentType = row.getCell((short) 12).getStringCellValue();
			final String numeroDocumentoIdentificacao = row.getCell((short) 13).getStringCellValue();
			final Date dataEmissaoDocumentoIdentificacao = row.getCell((short) 14).getDateCellValue();
			final Date dataValidadeDocumentoIdentificacao = row.getCell((short) 15).getDateCellValue();
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
            final boolean availableEmail = row.getCell((short) 30).getBooleanCellValue();
			final boolean availableWebSite = row.getCell((short) 31).getBooleanCellValue();
			final String profissao = row.getCell((short) 32).getStringCellValue();
			final String workPhone = row.getCell((short) 33).getStringCellValue();

            final Person person = new Person();
            person.setUsername(username);
            person.setNome(name);
            person.setGender(Gender.valueOf(gender));
            person.setPais(countries.get(COUNTRY));
            person.setNascimento(nascimento);
            person.setNacionalidade(nacionalidade);
            person.setFreguesiaNaturalidade(freguesiaNaturalidade);
            person.setConcelhoNaturalidade(concelhoNaturalidade);
            person.setDistritoNaturalidade(distritoNaturalidade);
            person.setMaritalStatus(MaritalStatus.valueOf(maritalStatus));
            person.setNomePai(nomePai);
            person.setNomeMae(nomeMae);
            person.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
            person.setNumeroDocumentoIdentificacao(numeroDocumentoIdentificacao);
            person.setDataEmissaoDocumentoIdentificacao(dataEmissaoDocumentoIdentificacao);
            person.setDataValidadeDocumentoIdentificacao(dataValidadeDocumentoIdentificacao);
            person.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
            person.setCodigoFiscal(codigoFiscal);
            person.setNumContribuinte(numContribuinte);
            person.setTelemovel(telemovel);
            person.setTelefone(telefone);
            person.setMorada(morada);
            person.setLocalidade(localidade);
            person.setLocalidadeCodigoPostal(localidadeCodigoPostal);
            person.setDistritoMorada(distritoMorada);
            person.setCodigoPostal(codigoPostal);
            person.setConcelhoMorada(concelhoMorada);
            person.setFreguesiaMorada(freguesiaMorada);
            person.setEmail(email);
            person.setEnderecoWeb(enderecoWeb);
            person.setAvailableEmail(Boolean.valueOf(availableEmail));
            person.setAvailableWebSite(Boolean.valueOf(availableWebSite));
            person.setProfissao(profissao);
            person.setWorkPhone(workPhone);

            persistentSupport.confirmarTransaccao();
            persistentSupport.iniciarTransaccao();

            peopleWithPersonRole.add(person);

            people.put(username, person);
		}

		persistentSupport.confirmarTransaccao();

        return people;
	}

    public static void dump(final HSSFRow row, final IPerson person) {
        final HSSFCell usernameCell = row.createCell((short) 0);
        final HSSFCell nameCell = row.createCell((short) 1);
        final HSSFCell genderCell = row.createCell((short) 2);
        final HSSFCell COUNTRYCell = row.createCell((short) 3);
        final HSSFCell nascimentoCell = row.createCell((short) 4);
        final HSSFCell nacionalidadeCell = row.createCell((short) 5);
        final HSSFCell freguesiaNaturalidadeCell = row.createCell((short) 6);
        final HSSFCell concelhoNaturalidadeCell = row.createCell((short) 7);
        final HSSFCell distritoNaturalidadeCell = row.createCell((short) 8);
        final HSSFCell maritalStatusCell = row.createCell((short) 9);
        final HSSFCell nomePaiCell = row.createCell((short) 10);
        final HSSFCell nomeMaeCell = row.createCell((short) 11);
        final HSSFCell idDocumentTypeCell = row.createCell((short) 12);
        final HSSFCell numeroDocumentoIdentificacaoCell = row.createCell((short) 13);
        final HSSFCell dataEmissaoDocumentoIdentificacaoCell = row.createCell((short) 14);
        final HSSFCell dataValidadeDocumentoIdentificacaoCell = row.createCell((short) 15);
        final HSSFCell localEmissaoDocumentoIdentificacaoCell = row.createCell((short) 16);
        final HSSFCell codigoFiscalCell = row.createCell((short) 17);
        final HSSFCell numContribuinteCell = row.createCell((short) 18);
        final HSSFCell telemovelCell = row.createCell((short) 19);
        final HSSFCell telefoneCell = row.createCell((short) 20);
        final HSSFCell moradaCell = row.createCell((short) 21);
        final HSSFCell localidadeCell = row.createCell((short) 22);
        final HSSFCell localidadeCodigoPostalCell = row.createCell((short) 23);
        final HSSFCell distritoMoradaCell = row.createCell((short) 24);
        final HSSFCell codigoPostalCell = row.createCell((short) 25);
        final HSSFCell concelhoMoradaCell = row.createCell((short) 26);
        final HSSFCell freguesiaMoradaCell = row.createCell((short) 27);
        final HSSFCell emailCell = row.createCell((short) 28);
        final HSSFCell enderecoWebCell = row.createCell((short) 29);
        final HSSFCell availableEmailCell = row.createCell((short) 30);
        final HSSFCell availableWebSiteCell = row.createCell((short) 31);
        final HSSFCell profissaoCell = row.createCell((short) 32);
        final HSSFCell workPhoneCell = row.createCell((short) 33);

        usernameCell.setCellValue(person.getUsername());
        nameCell.setCellValue(person.getNome());
        genderCell.setCellValue(person.getGender().toString());
        if (person.getPais() != null) {
            COUNTRYCell.setCellValue(person.getPais().getName());
        } else {
            System.out.println("Country null for user: " + person.getUsername());
        }
        nascimentoCell.setCellValue(person.getNascimento());
        nacionalidadeCell.setCellValue(person.getNacionalidade());
        freguesiaNaturalidadeCell.setCellValue(person.getFreguesiaNaturalidade());
        concelhoNaturalidadeCell.setCellValue(person.getConcelhoNaturalidade());
        distritoNaturalidadeCell.setCellValue(person.getDistritoNaturalidade());
        maritalStatusCell.setCellValue(person.getMaritalStatus().toString());
        nomePaiCell.setCellValue(person.getNomePai());
        nomeMaeCell.setCellValue(person.getNomeMae());
        idDocumentTypeCell.setCellValue(person.getIdDocumentType().toString());
        numeroDocumentoIdentificacaoCell.setCellValue(person.getNumeroDocumentoIdentificacao());
        if (person.getDataEmissaoDocumentoIdentificacao() != null) {
            dataEmissaoDocumentoIdentificacaoCell.setCellValue(person.getDataEmissaoDocumentoIdentificacao());
        }
        if (person.getDataValidadeDocumentoIdentificacao() != null) {
            dataValidadeDocumentoIdentificacaoCell.setCellValue(person.getDataValidadeDocumentoIdentificacao());
        }
        localEmissaoDocumentoIdentificacaoCell.setCellValue(person.getLocalEmissaoDocumentoIdentificacao());
        codigoFiscalCell.setCellValue(person.getCodigoFiscal());
        numContribuinteCell.setCellValue(person.getNumContribuinte());
        telemovelCell.setCellValue(person.getTelemovel());
        telefoneCell.setCellValue(person.getTelefone());
        moradaCell.setCellValue(person.getMorada());
        localidadeCell.setCellValue(person.getLocalidade());
        localidadeCodigoPostalCell.setCellValue(person.getLocalidadeCodigoPostal());
        distritoMoradaCell.setCellValue(person.getDistritoMorada());
        codigoPostalCell.setCellValue(person.getCodigoPostal());
        concelhoMoradaCell.setCellValue(person.getConcelhoMorada());
        freguesiaMoradaCell.setCellValue(person.getFreguesiaMorada());
        emailCell.setCellValue(person.getEmail());
        enderecoWebCell.setCellValue(person.getEnderecoWeb());
        availableEmailCell.setCellValue(person.getAvailableEmail());
        availableWebSiteCell.setCellValue(person.getAvailableWebSite());
        profissaoCell.setCellValue(person.getProfissao());
        workPhoneCell.setCellValue(person.getWorkPhone());
    }

}
