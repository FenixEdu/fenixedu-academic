package middleware.personAndEmployee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;
import middleware.dataClean.personFilter.LimpaNaturalidades;
import middleware.dataClean.personFilter.LimpaOutput;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.WordUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Employee;
import Dominio.IEmployee;
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
public class ServicoSeguroActualizarPessoas
{

	private String _ficheiro = null;
	private String _delimitador = new String(";");

	/** Construtor */
	public ServicoSeguroActualizarPessoas(String[] args)
	{
		_ficheiro = args[0];
	}

	/** executa a actualizacao da tabela Pessoa na Base de Dados */
	public static void main(String[] args) throws Exception
	{
		ServicoSeguroActualizarPessoas servico = new ServicoSeguroActualizarPessoas(args);

		Collection listaPessoasFromFile = null;

		try
		{
			listaPessoasFromFile =
				LeituraFicheiroPessoa.lerFicheiro(servico._ficheiro, servico._delimitador);
		}
		catch (Exception e)
		{
			throw new NotExecuteException(e.getMessage());
		}
		System.out.println("Converting Persons " + listaPessoasFromFile.size() + " ... ");

		actualizaPessoas(listaPessoasFromFile);

		desactivarPessoas(listaPessoasFromFile);

		System.out.println("  Done !");
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

	private static void actualizaPessoas(Collection listaPessoasFromFile)
	{
		int newPersons = 0;
		int newRoles = 0;

		try
		{
			if (listaPessoasFromFile != null)
			{
				//open databases
				PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
				broker.clearCache();
				broker.beginTransaction();

				DB.iniciarTransaccao();
				LimpaNaturalidades limpaNaturalidades = new LimpaNaturalidades();

				Iterator iterador = listaPessoasFromFile.iterator();
				Pessoa pessoa = null;
				// ciclo que percorre a Collection de Pessoas
				while (iterador.hasNext())
				{
					try
					{
						pessoa = (Pessoa) iterador.next();
						personFilter(limpaNaturalidades, pessoa);

						Criteria criteria = new Criteria();
						Query query = null;

						criteria.addEqualTo(
							"numeroDocumentoIdentificacao",
							pessoa.getNumeroDocumentoIdentificacao());
						criteria.addEqualTo(
							"tipoDocumentoIdentificacao",
							pessoa.getTipoDocumentoIdentificacao());
						query = new QueryByCriteria(Pessoa.class, criteria);
						List result = (List) broker.getCollectionByQuery(query);

						IPessoa person2Write = null;
						// A Pessoa nao Existe
						if (result.size() == 0)
						{
							person2Write = (IPessoa) pessoa;
							newPersons++;
						}
						else
						{
							// A Pessoa Existe
							person2Write = (IPessoa) result.get(0);
							PersonUtils.updatePerson(person2Write, pessoa);
						}

						//roles
						IPersonRole personRole =
							RoleFunctions.readPersonRole(person2Write, RoleType.PERSON, broker);
						if (personRole == null)
						{
							if (person2Write.getPersonRoles() == null)
							{
								person2Write.setPersonRoles(new ArrayList());
							}
							IRole role = RoleFunctions.readRole(RoleType.PERSON, broker);
							if (role == null)
							{
								throw new Exception("Role Desconhecido !!!");
							}
							else
							{
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
						System.out.println("Erro a converter a pessoa " + pessoa + "\n");
						continue;
					}
				}

				//close databases
				DB.confirmarTransaccao();
				broker.commitTransaction();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("New persons added : " + newPersons);
		System.out.println("New Roles added : " + newRoles);
	}

	private static void desactivarPessoas(Collection listaPessoasFromFile)
	{
		if (listaPessoasFromFile != null)
		{
			//Open databases
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			broker.beginTransaction();

			//Read all person from Data Base
			Collection listaPessoasFromDB = PersonUtils.readAllPersonsEmployee(broker);
			if (listaPessoasFromDB != null)
			{

				Iterator iterador = listaPessoasFromDB.iterator();
				IPessoa pessoaFromDB = null;
				int counter = 0;
				while (iterador.hasNext())
				{
					pessoaFromDB = (IPessoa) iterador.next();
					try
					{
						final IPessoa pessoaPredicate = pessoaFromDB;
						//Find if the person from BD exists in file with active persons.
						IPessoa pessoaFromFile =
							(IPessoa) CollectionUtils.find(listaPessoasFromFile, new Predicate()
						{
							public boolean evaluate(Object obj)
							{
								IPessoa pessoa = (IPessoa) obj;
								return pessoa.getNumeroDocumentoIdentificacao().equals(
									pessoaPredicate.getNumeroDocumentoIdentificacao())
									&& pessoa.getTipoDocumentoIdentificacao().equals(
										pessoaPredicate.getTipoDocumentoIdentificacao());
							}
						});

						if (pessoaFromFile == null)
						{
							//person is inactive, then delete roles and put password null
							pessoaFromDB.setPassword(null);
							pessoaFromDB.setUsername(
								"INA" + pessoaFromDB.getNumeroDocumentoIdentificacao());

							IRole role = RoleFunctions.readRole(RoleType.EMPLOYEE, broker);
							if (role != null
								&& pessoaFromDB.getPersonRoles() != null
								&& pessoaFromDB.getPersonRoles().size() > 0
								&& pessoaFromDB.getPersonRoles().contains(role))
							{
								pessoaFromDB.getPersonRoles().remove(role);
							}

							role = RoleFunctions.readRole(RoleType.TEACHER, broker);
							if (role != null
								&& pessoaFromDB.getPersonRoles() != null
								&& pessoaFromDB.getPersonRoles().size() > 0
								&& pessoaFromDB.getPersonRoles().contains(role))
							{
								pessoaFromDB.getPersonRoles().remove(role);
							}

							role = RoleFunctions.readRole(RoleType.PERSON, broker);
							if (role != null
								&& pessoaFromDB.getPersonRoles() != null
								&& pessoaFromDB.getPersonRoles().size() == 1
								&& pessoaFromDB.getPersonRoles().contains(role))
							{
								pessoaFromDB.getPersonRoles().remove(role);
							}

							broker.store(pessoaFromDB);

							// The employee correspont to the person
							Query query = null;
							Criteria criteria = new Criteria();
							criteria.addEqualTo("keyPerson", pessoaFromDB.getIdInternal());
							query = new QueryByCriteria(Employee.class, criteria);
							List resultEmployee = (List) broker.getCollectionByQuery(query);
							if (resultEmployee.size() != 0)
							{
								IEmployee employeePerson = (IEmployee) resultEmployee.get(0);

								employeePerson.setActive(Boolean.FALSE);

								broker.store(employeePerson);
							}

							counter++;
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
						System.out.println("Error with person: " + pessoaFromDB.getNome());
						continue;
					}
				}
				System.out.println("Inactive person: " + counter);
			}

			broker.commitTransaction();
			broker.clearCache();
		}
		System.out.println("  Done !");
	}
}
