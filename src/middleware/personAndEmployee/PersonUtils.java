/*
 * Created on 26/Jul/2003 by jpvl
 *  
 */
package middleware.personAndEmployee;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import DataBeans.util.CopyUtils;
import Dominio.Country;
import Dominio.ICountry;
import Dominio.IPessoa;
import Dominio.Pessoa;
import Util.EstadoCivil;
import Util.RoleType;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author jpvl
 */
public abstract class PersonUtils
{
	public static void updatePerson(IPessoa person2Write, IPessoa person2Convert) throws Exception
	{

		try
		{
			//Backup
			String password = null;
			if (person2Write.getPassword() != null)
			{
				password = new String(person2Write.getPassword());
			}
			Integer internalCode = new Integer(person2Write.getIdInternal().intValue());
			String username = new String(person2Write.getUsername());
			String mobilePhone = null;
			if (person2Write.getTelemovel() != null)
			{
				mobilePhone = new String(person2Write.getTelemovel());
			}
			String email = null;
			if (person2Write.getEmail() != null)
			{
				email = new String(person2Write.getEmail());
			}
			String url = null;
			if (person2Write.getEnderecoWeb() != null)
			{
				url = new String(person2Write.getEnderecoWeb());
			}
			//backup include ackOptLock, so that errors
			Integer ackOptLock = person2Write.getAckOptLock();

			ICountry country = person2Write.getPais();
			Collection rolesLists = person2Write.getPersonRoles();
			List credtisLists = person2Write.getManageableDepartmentCredits();
			List advisories = person2Write.getAdvisories();

			CopyUtils.copyProperties(person2Write, person2Convert);

			person2Write.setIdInternal(internalCode);
			person2Write.setPassword(password);
			person2Write.setUsername(username);
			if (mobilePhone != null)
			{
				person2Write.setTelemovel(mobilePhone);
			}
			if (email != null)
			{
				person2Write.setEmail(email);
			}
			if (url != null)
			{
				person2Write.setEnderecoWeb(url);
			}

			person2Write.setAckOptLock(ackOptLock);

			person2Write.setPersonRoles(rolesLists);
			person2Write.setManageableDepartmentCredits(credtisLists);
			person2Write.setAdvisories(advisories);
			if (person2Write.getPais() == null
				|| person2Write.getPais().equals(person2Convert.getPais()))
			{
				person2Write.setPais(person2Convert.getPais());
			}
			else
			{
				person2Write.setPais(country);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Erro a converter a Pessoa " + person2Convert.getNome());
			throw new Exception(e);
		}
	}

	public static IPessoa getPerson(
		String number,
		String bi,
		String usernamePrefix,
		PersistenceBroker broker)
		throws Exception
	{

		Criteria criteriaByBi = new Criteria();
		criteriaByBi.addEqualTo("numeroDocumentoIdentificacao", bi);
		List resultByBi = doQuery(broker, criteriaByBi, Pessoa.class);

		Criteria criteriaByUsername = new Criteria();
		criteriaByUsername.addEqualTo("username", usernamePrefix + number);
		List resultByUsername = doQuery(broker, criteriaByUsername, Pessoa.class);

		IPessoa person = null;
		if (resultByBi.size() == 1)
		{
			if (resultByUsername.size() == 1)
			{
				IPessoa personByBi = (IPessoa) resultByBi.get(0);
				IPessoa personByUsername = (IPessoa) resultByUsername.get(0);

				if (personByBi.getUsername().equals(personByUsername.getUsername()))
				{
					person = (IPessoa) resultByBi.get(0);
				}
				else
				{
					PersonUtils.updatePerson(personByUsername, personByBi);
					person = personByUsername;
					System.out.println(
							"PERSON username: "
							+ personByUsername.getUsername()
							+ "- bi:"
							+ personByUsername.getNumeroDocumentoIdentificacao());
					broker.delete(personByBi);
				}
			}
			else
			{
				person = (IPessoa) resultByBi.get(0);
			}
		} else {
			return null;
		}
		return person;
	}
	
	public static List readAllPersonsEmployee(PersistenceBroker broker) {
		System.out.println("Reading persons from DB");
		Criteria criteria = new Criteria();
		criteria.addEqualTo("personRoles.roleType", RoleType.EMPLOYEE);

		Query query = new QueryByCriteria(Pessoa.class, criteria);

		List result = (List) broker.getCollectionByQuery(query);

		System.out.println("Finished read persons from DB(" + result.size() + ")");
		return result;
	}

	private static List doQuery(PersistenceBroker broker, Criteria criteria, Class classToQuery)
	{
		Query query = new QueryByCriteria(classToQuery, criteria);
		return (List) broker.getCollectionByQuery(query);
	}
	
	
	private static Integer formataTipoDocumentoIdentificacao(String naoFormatado)
	{
		Integer resultado = null;

		if (naoFormatado.equals("00"))
			resultado = new Integer(TipoDocumentoIdentificacao.OUTRO);
		else if (naoFormatado.equals("01"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
		else if (naoFormatado.equals("02"))
			resultado = new Integer(TipoDocumentoIdentificacao.PASSAPORTE);
		else if (naoFormatado.equals("03"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA);
		else if (naoFormatado.equals("04"))
			resultado =
			new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO);
		else if (naoFormatado.equals("05"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM);
		else if (naoFormatado.equals("06"))
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA);
		else
			resultado = new Integer(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);

		return resultado;
	}
	
	private static ICountry formataNacionalidadeCompleta(String naoFormatada) throws Exception
	{

		if (naoFormatada == null || naoFormatada.equals(""))
		{
			naoFormatada = "1";
		}

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

		Integer chavePais = new Integer(naoFormatada);

		Criteria criteria = new Criteria();
		Query query = null;

		if (chavePais.equals(new Integer(1)))
		{
			criteria.addEqualTo("name", "PORTUGAL");
		}
		else
		{
			criteria.addEqualTo("name", "ESTRANGEIRO");
		}

		query = new QueryByCriteria(Country.class, criteria);
		List result = (List) broker.getCollectionByQuery(query);
		broker.close();

		if (result.size() == 0)
			throw new Exception("Error Reading Country");
		else
			return (ICountry) result.get(0);

	}	
	
	/**
	 * constroi uma Date a partir de uma String com o formato:0AAAAMMDD retornar uma data invalida para
	 * os casos de 000000000 data invalida -> 1/1/1900
	 */
	private static Date formataData(String naoFormatada)
	{
		Date resultado = null;
		if (naoFormatada != null && !naoFormatada.equals("") && naoFormatada.length() == 8)
		{

			Integer todaData = null;
			try
			{
				todaData = new Integer(naoFormatada);
				if (todaData.intValue() == 0) {
					return null;
				}
			} catch (NumberFormatException nfe) {
				return null;
			}

			try
			{
				Integer anoTexto = new Integer(todaData.toString().substring(0, 4));
				Integer mesTexto = new Integer(todaData.toString().substring(4, 6));
				Integer diaTexto = new Integer(todaData.toString().substring(6, 8));

				Calendar calendario = Calendar.getInstance();
				calendario.setLenient(false);
				calendario.clear();
				calendario.set(anoTexto.intValue(), mesTexto.intValue() - 1, diaTexto.intValue());
				
				resultado = new Date(calendario.getTimeInMillis());
			}
			catch (IllegalArgumentException iae)
			{
				resultado = null;
			}
		}
		return resultado;
	}

	/**
	 * retorna um Integer representando o sexo, null em caso de String invalida
	 */
	private static Integer formataSexo(String naoFormatado)
	{
		Integer resultado = null;

		//trocar estes valores 8 e 9 por constantes
		if (naoFormatado.equalsIgnoreCase(Sexo.FEMININO_STRING) || naoFormatado.equalsIgnoreCase("F"))
			resultado = new Integer(Sexo.FEMININO);
		else if (
				naoFormatado.equalsIgnoreCase(Sexo.MASCULINO_STRING) || naoFormatado.equalsIgnoreCase("M"))
			resultado = new Integer(Sexo.MASCULINO);

		return resultado;
	}

	/**
	 * retorna um Integer representando o estado civil, null em caso de String invalida
	 */
	public static Integer formataEstadoCivil(String naoFormatado)
	{
		Integer resultado = null;

		//trocar estes valores por constantes
		if (naoFormatado == null || naoFormatado.equals(""))
		{
			resultado = new Integer(EstadoCivil.SOLTEIRO);
		}
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.SOLTEIRO_STRING))
			resultado = new Integer(EstadoCivil.SOLTEIRO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.CASADO_STRING))
			resultado = new Integer(EstadoCivil.CASADO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.DIVORCIADO_STRING))
			resultado = new Integer(EstadoCivil.DIVORCIADO);
		else if (
				(naoFormatado.equalsIgnoreCase(EstadoCivil.VIUVO_STRING))
				|| (naoFormatado.equalsIgnoreCase("viuvo")))
			resultado = new Integer(EstadoCivil.VIUVO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.SEPARADO_STRING))
			resultado = new Integer(EstadoCivil.SEPARADO);
		else if (naoFormatado.equalsIgnoreCase(EstadoCivil.UNIAO_DE_FACTO_STRING))
			resultado = new Integer(EstadoCivil.UNIAO_DE_FACTO);
		else
			resultado = new Integer(EstadoCivil.DESCONHECIDO);

		return resultado;

	}
	
}