/*
 * Created on 26/Jul/2003 by jpvl
 *  
 */
package middleware.personAndEmployee;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Country;
import Dominio.ICountry;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author jpvl
 */
public abstract class FormatPersonUtils
{
	public static Integer formataTipoDocumentoIdentificacao(String naoFormatado)
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
	
	public static ICountry formataNacionalidadeCompleta(String naoFormatada) throws Exception
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
	public static Date formataData(String naoFormatada)
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
	public static Integer formataSexo(String naoFormatado)
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