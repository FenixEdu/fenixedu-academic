package middleware.personAndEmployee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;
import middleware.dataClean.personFilter.LimpaNaturalidades;
import middleware.dataClean.personFilter.LimpaOutput;

import org.apache.commons.lang.WordUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteFiltroPessoa.DB;
import Util.RoleType;

/**
 * @author Ivo Brandão
 */
public class ServicoSeguroTodasPessoas extends ServicoSeguroSuperMigracaoPessoas
{
	private String _ficheiro = null;
	private Collection _listaPessoas = null;
	private Pessoa _pessoa = null;
	private String _delimitador = new String(";");

	/** Construtor */
	public ServicoSeguroTodasPessoas(String[] args) throws NotExecuteException
	{
		String path;
		try
		{
			path = readPathFile();
		} catch (NotExecuteException e)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}
		_ficheiro = path.concat(args[0]);		
	}

	/** executa a actualizacao da tabela Pessoa na Base de Dados */
	public static void main(String[] args) throws Exception
	{
		ServicoSeguroTodasPessoas servico = new ServicoSeguroTodasPessoas(args);
		int newPersons = 0;
		int newRoles = 0;

		try
		{
			servico._listaPessoas =
				LeituraFicheiroPessoa.lerFicheiro(servico._ficheiro, servico._delimitador);
		}
		catch (Exception nee)
		{
			nee.printStackTrace();
			throw new NotExecuteException(nee.getMessage());
		}

		System.out.println("Converting Persons " + servico._listaPessoas.size() + " ... ");

		actualizaPessoas(servico, newPersons, newRoles);

		System.out.println("  Done !");
	}

	private static void actualizaPessoas(
		ServicoSeguroTodasPessoas servico,
		int newPersons,
		int newRoles)
		throws Exception
	{
		if (servico._listaPessoas != null)
		{
			//open databases
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			broker.beginTransaction();

			DB.iniciarTransaccao();
			LimpaNaturalidades limpaNaturalidades = new LimpaNaturalidades();

			Iterator iterador = servico._listaPessoas.iterator();
			/* ciclo que percorre a Collection de Pessoas */
			while (iterador.hasNext())
			{
				try
				{
					servico._pessoa = (Pessoa) iterador.next();
					//System.out.println("-->Le Pessoa: " + servico._pessoa.getNome());

					personFilter(limpaNaturalidades, servico._pessoa);

					Criteria criteria = new Criteria();
					Query query = null;

					criteria.addEqualTo(
						"numeroDocumentoIdentificacao",
						servico._pessoa.getNumeroDocumentoIdentificacao());
					criteria.addEqualTo(
						"tipoDocumentoIdentificacao",
						servico._pessoa.getTipoDocumentoIdentificacao());
					query = new QueryByCriteria(Pessoa.class, criteria);
					List result = (List) broker.getCollectionByQuery(query);

					IPessoa person2Write = null;
					// A Pessoa nao Existe
					if (result.size() == 0)
					{
						person2Write = (IPessoa) servico._pessoa;
						newPersons++;
					}
					else
					{
						// A Pessoa Existe
						person2Write = (IPessoa) result.get(0);
						PersonUtils.updatePerson(person2Write, servico._pessoa);
					}

					//roles
					IPersonRole personRole =
						RoleFunctions.readPersonRole(person2Write, RoleType.PERSON, broker);
					if (personRole == null)
					{
						IRole role = RoleFunctions.readRole(RoleType.PERSON, broker);
						if (role == null)
						{
							throw new Exception("Role Desconhecido !!!");
						}
						else
						{
							if (person2Write.getPersonRoles() == null)
							{
								person2Write.setPersonRoles(new ArrayList());
							}
							person2Write.getPersonRoles().add(role);
							newRoles++;
						}
					}

					if (person2Write.getEstadoCivil().getEstadoCivil().intValue() > 7)
					{
						System.out.println(
							"Erro : " + person2Write.getEstadoCivil().getEstadoCivil().intValue());
					}

					broker.store(person2Write);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.out.println("Erro a converter a pessoa " + servico._pessoa + "\n");
					//throw new Exception("Erro a converter a pessoa " + servico._pessoa + "\n" + e);
					//continue;
				}
			}

			//close databases
			DB.confirmarTransaccao();
			broker.commitTransaction();
			broker.clearCache();
		}
		System.out.println("New persons added : " + newPersons);
		System.out.println("New Roles added : " + newRoles);
	}

	private static void personFilter(LimpaNaturalidades limpaNaturalidades, Pessoa pessoa)
	{
		try
		{
			//locais da naturalidade
			LimpaOutput limpaOutput =
				limpaNaturalidades.limpaNats(
					pessoa.getUsername(),
					pessoa.getNacionalidade(),
					pessoa.getDistritoNaturalidade(),
					pessoa.getConcelhoNaturalidade(),
					pessoa.getFreguesiaNaturalidade());
			if (limpaOutput.getNomeDistrito() != null)
			{
				pessoa.setDistritoNaturalidade(WordUtils.capitalize(limpaOutput.getNomeDistrito()));
			}
			if (limpaOutput.getNomeConcelho() != null)
			{
				pessoa.setConcelhoNaturalidade(WordUtils.capitalize(limpaOutput.getNomeConcelho()));
			}
			if (limpaOutput.getNomeFreguesia() != null)
			{
				pessoa.setFreguesiaNaturalidade(WordUtils.capitalize(limpaOutput.getNomeFreguesia()));
			}

			//locais da Morada
			limpaOutput =
				limpaNaturalidades.limpaNats(
					pessoa.getUsername(),
					pessoa.getNacionalidade(),
					pessoa.getDistritoMorada(),
					pessoa.getConcelhoMorada(),
					pessoa.getFreguesiaMorada());
			if (limpaOutput.getNomeDistrito() != null)
			{
				pessoa.setDistritoMorada(WordUtils.capitalize(limpaOutput.getNomeDistrito()));
			}
			if (limpaOutput.getNomeConcelho() != null)
			{
				pessoa.setConcelhoMorada(WordUtils.capitalize(limpaOutput.getNomeConcelho()));
			}
			if (limpaOutput.getNomeFreguesia() != null)
			{
				pessoa.setFreguesiaMorada(WordUtils.capitalize(limpaOutput.getNomeFreguesia()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** retorna a lista de pessoas */
	public Collection getListaPessoas()
	{
		return _listaPessoas;
	}
}
