package middleware.personMigration;

import java.util.List;

import middleware.middlewareDomain.MWStudent;
import middleware.middlewareDomain.MWPerson;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Country;
import Dominio.ICountry;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.PersonRole;
import Dominio.Pessoa;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EstadoCivil;
import Util.RoleType;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class PersonUtils {

	/**
	 * 
	 * @param countryCode
	 * @return The right Country
	 * @throws ExcepcaoPersistencia
	 */
	public static ICountry convertCountry(String countryCode) {

		Criteria criteria = new Criteria();

		if (countryCode.equals("01")
			|| countryCode.equals("02")
			|| countryCode.equals("03")
			|| countryCode.equals("04")
			|| countryCode.equals("05")
			|| countryCode.equals("06")
			|| countryCode.equals("1")
			|| countryCode.equals("2")
			|| countryCode.equals("3")
			|| countryCode.equals("4")
			|| countryCode.equals("5")
			|| countryCode.equals("6")
			|| countryCode.equals("0")) {
			criteria.addEqualTo("name", "PORTUGAL");
		} else if (countryCode.equals("10")) {
			criteria.addEqualTo("name", "ANGOLA");
		} else if (countryCode.equals("11")) {
			criteria.addEqualTo("name", "BRASIL");
		} else if (countryCode.equals("12")) {
			criteria.addEqualTo("name", "CABO VERDE");
		} else if (countryCode.equals("13")) {
			criteria.addEqualTo("name", "GUINE-BISSAO");
		} else if (countryCode.equals("14")) {
			criteria.addEqualTo("name", "MOCAMBIQUE");
		} else if (countryCode.equals("15")) {
			criteria.addEqualTo("name", "SAO TOME E PRINCIPE");
		} else if (countryCode.equals("16")) {
			criteria.addEqualTo("name", "TIMOR LORO SAE");
		} else if (countryCode.equals("20")) {
			criteria.addEqualTo("name", "BELGICA");
		} else if (countryCode.equals("21")) {
			criteria.addEqualTo("name", "DINAMARCA");
		} else if (countryCode.equals("22")) {
			criteria.addEqualTo("name", "ESPANHA");
		} else if (countryCode.equals("23")) {
			criteria.addEqualTo("name", "FRANCA");
		} else if (countryCode.equals("24")) {
			criteria.addEqualTo("name", "HOLANDA");
		} else if (countryCode.equals("25")) {
			criteria.addEqualTo("name", "IRLANDA");
		} else if (countryCode.equals("26")) {
			criteria.addEqualTo("name", "ITALIA");
		} else if (countryCode.equals("27")) {
			criteria.addEqualTo("name", "LUXEMBURGO");
		} else if (countryCode.equals("28")) {
			criteria.addEqualTo("name", "ALEMANHA");
		} else if (countryCode.equals("29")) {
			criteria.addEqualTo("name", "REINO UNIDO");
		} else if (countryCode.equals("30")) {
			criteria.addEqualTo("name", "SUECIA");
		} else if (countryCode.equals("31")) {
			criteria.addEqualTo("name", "NORUEGA");
		} else if (countryCode.equals("32")) {
			criteria.addEqualTo("name", "POLONIA");
		} else if (countryCode.equals("33")) {
			criteria.addEqualTo("name", "AFRICA DO SUL");
		} else if (countryCode.equals("34")) {
			criteria.addEqualTo("name", "ARGENTINA");
		} else if (countryCode.equals("35")) {
			criteria.addEqualTo("name", "CANADA");
		} else if (countryCode.equals("36")) {
			criteria.addEqualTo("name", "CHILE");
		} else if (countryCode.equals("37")) {
			criteria.addEqualTo("name", "EQUADOR");
		} else if (countryCode.equals("38")) {
			criteria.addEqualTo("name", "ESTADOS UNIDOS DA AMERICA");
		} else if (countryCode.equals("39")) {
			criteria.addEqualTo("name", "IRAO");
		} else if (countryCode.equals("40")) {
			criteria.addEqualTo("name", "MARROCOS");
		} else if (countryCode.equals("41")) {
			criteria.addEqualTo("name", "VENEZUELA");
		} else if (countryCode.equals("42")) {
			criteria.addEqualTo("name", "AUSTRALIA");
		} else if (countryCode.equals("43")) {
			criteria.addEqualTo("name", "PAQUISTAO");
		} else if (countryCode.equals("44")) {
			criteria.addEqualTo("name", "REPUBLICA DO ZAIRE");
		} else if (countryCode.equals("47")) {
			criteria.addEqualTo("name", "LIBIA");
		} else if (countryCode.equals("48")) {
			criteria.addEqualTo("name", "PALESTINA");
		} else if (countryCode.equals("49")) {
			criteria.addEqualTo("name", "ZIMBABUE");
		} else if (countryCode.equals("50")) {
			criteria.addEqualTo("name", "MEXICO");
		} else if (countryCode.equals("51")) {
			criteria.addEqualTo("name", "RUSSIA");
		} else if (countryCode.equals("52")) {
			criteria.addEqualTo("name", "AUSTRIA");
		} else if (countryCode.equals("53")) {
			criteria.addEqualTo("name", "IRAQUE");
		} else if (countryCode.equals("54")) {
			criteria.addEqualTo("name", "PERU");
		} else if (countryCode.equals("55")) {
			criteria.addEqualTo("name", "ESTADOS UNIDOS DA AMERICA");
		} else if (countryCode.equals("60")) {
			criteria.addEqualTo("name", "ROMENIA");
		} else if (countryCode.equals("61")) {
			criteria.addEqualTo("name", "REPUBLICA CHECA");
		} else {
			System.out.println("COUNTRY > NULL");
			return null;
		}

		Query query = new QueryByCriteria(Country.class, criteria);
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

		broker.beginTransaction();
		List result = (List) broker.getCollectionByQuery(query);
		broker.commitTransaction();

		if (result.size() == 0)
			return null;
		else
			return (ICountry) result.get(0);
	}

	/**
	 * @param Fenix Person
	 * @param Old Database Person
	 * 
	 * This method updates the personal information
	 * The email, Mobile Phone, HomePage and Password won't be updated because one can change 
	 * this in Fenix
	 * 
	 */
	public static void updateStudentPerson(IPessoa fenixPersonTemp, MWPerson oldPerson) throws ExcepcaoPersistencia {

		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		// Get Fenix Person
		IPessoa personTemp = new Pessoa();
		personTemp.setIdInternal(fenixPersonTemp.getIdInternal());

		IPessoa fenixPerson = (IPessoa) sp.getIPessoaPersistente().readByOId(personTemp, true);
		//		IPessoa fenixPerson = (IPessoa) sp.getIPessoaPersistente().readByOId(personTemp, false);

		if (fenixPerson == null) {
			System.out.println("Person not Found !");
			return;
		}

		fenixPerson.setCodigoFiscal(oldPerson.getFinancialrepcode());
		fenixPerson.setCodigoPostal(oldPerson.getZipcode());
		fenixPerson.setConcelhoMorada(oldPerson.getMunicipalityofaddress());
		fenixPerson.setConcelhoNaturalidade(oldPerson.getMunicipalityofbirth());
		fenixPerson.setDataEmissaoDocumentoIdentificacao(oldPerson.getDocumentiddate());
		fenixPerson.setDataValidadeDocumentoIdentificacao(oldPerson.getDocumentidvalidation());
		fenixPerson.setDistritoMorada(oldPerson.getDistrictofaddress());
		fenixPerson.setDistritoNaturalidade(oldPerson.getDistrictofbirth());
		fenixPerson.setEstadoCivil(getMaritalStatus(oldPerson.getMaritalstatus()));
		fenixPerson.setFreguesiaMorada(oldPerson.getParishofaddress());
		fenixPerson.setFreguesiaNaturalidade(oldPerson.getParishofbirth());
		fenixPerson.setLocalEmissaoDocumentoIdentificacao(oldPerson.getDocumentidplace());
		fenixPerson.setLocalidadeCodigoPostal(oldPerson.getAddressAreaCode());
		fenixPerson.setMorada(oldPerson.getAddress());
		fenixPerson.setNascimento(oldPerson.getDateofbirth());
		fenixPerson.setNome(oldPerson.getName());
		fenixPerson.setNomeMae(oldPerson.getMothername());
		fenixPerson.setNomePai(oldPerson.getFathername());
		fenixPerson.setNumContribuinte(oldPerson.getFiscalcode());
		fenixPerson.setNumeroDocumentoIdentificacao(oldPerson.getDocumentidnumber());
		fenixPerson.setPais(getCountry(oldPerson.getCountrycode()));
		fenixPerson.setSexo(getSex(oldPerson.getSex()));
		fenixPerson.setTelefone(oldPerson.getPhone());
		fenixPerson.setTipoDocumentoIdentificacao(getDocumentIdType(oldPerson.getDocumentidtype()));

	}

	/**
	 * @param Document Id Type
	 * @return The corresponding object
	 */
	public static TipoDocumentoIdentificacao getDocumentIdType(String documentIDType) {
		return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
	}

	/**
	 * @param Sex
	 * @return The corresponding object
	 */
	public static Sexo getSex(String sex) {
		if (sex.equalsIgnoreCase("M")) {
			return new Sexo(Sexo.MASCULINO);
		} else {
			return new Sexo(Sexo.FEMININO);
		}
	}

	/**
	 * @param Country Code
	 * @return The corresponding object
	 */
	public static ICountry getCountry(Integer countryCode) {
		return PersonUtils.convertCountry(countryCode.toString());
	}

	/**
	 * @param Marital Status
	 * @return The corresponding object
	 */
	public static EstadoCivil getMaritalStatus(String string) {
		return new EstadoCivil(EstadoCivil.SOLTEIRO);
	}

	public static IPessoa createPersonFromStudent(MWStudent oldStudent, ISuportePersistente sp) throws ExcepcaoPersistencia {

		MWPerson oldPerson = oldStudent.getMiddlewarePerson();

		IPessoa person = new Pessoa();
		sp.getIPessoaPersistente().simpleLockWrite(person);

		person.setCodigoFiscal(oldPerson.getFinancialrepcode());
		person.setCodigoPostal(oldPerson.getZipcode());
		person.setConcelhoMorada(oldPerson.getMunicipalityofaddress());
		person.setConcelhoNaturalidade(oldPerson.getMunicipalityofbirth());
		person.setDataEmissaoDocumentoIdentificacao(oldPerson.getDocumentiddate());
		person.setDataValidadeDocumentoIdentificacao(oldPerson.getDocumentidvalidation());
		person.setDistritoMorada(oldPerson.getDistrictofaddress());
		person.setDistritoNaturalidade(oldPerson.getDistrictofbirth());
		person.setEstadoCivil(getMaritalStatus(oldPerson.getMaritalstatus()));
		person.setFreguesiaMorada(oldPerson.getParishofaddress());
		person.setFreguesiaNaturalidade(oldPerson.getParishofbirth());
		person.setLocalEmissaoDocumentoIdentificacao(oldPerson.getDocumentidplace());
		person.setLocalidadeCodigoPostal(oldPerson.getAddressAreaCode());
		person.setMorada(oldPerson.getAddress());
		person.setNascimento(oldPerson.getDateofbirth());
		person.setNome(oldPerson.getName());
		person.setNomeMae(oldPerson.getMothername());
		person.setNomePai(oldPerson.getFathername());
		person.setNumContribuinte(oldPerson.getFiscalcode());
		person.setNumeroDocumentoIdentificacao(oldPerson.getDocumentidnumber());
		person.setPais(getCountry(oldPerson.getCountrycode()));
		person.setSexo(getSex(oldPerson.getSex()));
		person.setTelefone(oldPerson.getPhone());
		person.setTipoDocumentoIdentificacao(getDocumentIdType(oldPerson.getDocumentidtype()));
		person.setTelemovel(oldPerson.getMobilephone());
		person.setEmail(oldPerson.getEmail());
		person.setEnderecoWeb(oldPerson.getHomepage());

		// Create the username
		person.setUsername("L" + oldStudent.getNumber());

		// Generate Password
		person.setPassword(PasswordEncryptor.encryptPassword(oldStudent.getDocumentidnumber()));

		IRole role = sp.getIPersistentRole().readByRoleType(RoleType.PERSON);
		IPersonRole personRole = new PersonRole();
		sp.getIPersistentPersonRole().simpleLockWrite(personRole);
		personRole.setPerson(person);
		personRole.setRole(role);

		role = sp.getIPersistentRole().readByRoleType(RoleType.STUDENT);
		personRole = new PersonRole();
		sp.getIPersistentPersonRole().simpleLockWrite(personRole);
		personRole.setPerson(person);
		personRole.setRole(role);

		return person;
	}

}
