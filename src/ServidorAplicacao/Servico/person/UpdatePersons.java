/*
 * Created on 25/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import middleware.dataClean.personFilter.LimpaNaturalidades;
import middleware.dataClean.personFilter.LimpaOutput;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.WordUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteFiltroPessoa.DB;
import Util.RoleType;

/**
 * @author Tânia Pousão
 *  
 */
public class UpdatePersons implements IService
{

	public UpdatePersons()
	{
	}

	public void run(Collection personsList) throws FenixServiceException
	{
		if (personsList == null || personsList.size() <= 0)
		{
			return;
		}

		int newPersons = 0;
		int newRoles = 0;

		try
		{
			DB.iniciarTransaccao();
			LimpaNaturalidades limpaNaturalidades = new LimpaNaturalidades();

			Iterator iterador = personsList.iterator();
			while (iterador.hasNext())
			{
				IPessoa person = (IPessoa) iterador.next();
				System.out.println("-->Le Pessoa: " + person.getNome());
				try
				{
					personFilter(limpaNaturalidades, person);

					ISuportePersistente sp = SuportePersistenteOJB.getInstance();
					IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

					//Read person
					IPessoa personToUpdate =
						persistentPerson.lerPessoaPorNumDocIdETipoDocId(
							person.getNumeroDocumentoIdentificacao(),
							person.getTipoDocumentoIdentificacao());

					//Person to write(create or new)
					persistentPerson.simpleLockWrite(personToUpdate);
					if (personToUpdate == null)
					{
						personToUpdate = person;
						newPersons++;
					}
					else
					{
						updatePerson(personToUpdate, person);
					}

					//roles

					//read role person
					IPersistentRole persistentRole = sp.getIPersistentRole();
					IRole rolePerson = persistentRole.readByRoleType(RoleType.PERSON);
					if (rolePerson == null)
					{
						throw new Exception("Role Desconhecido !!!");
					}

					IPersistentPersonRole persistentPersonRole = sp.getIPersistentPersonRole();
					IPersonRole personRole =
						persistentPersonRole.readByPersonAndRole(personToUpdate, rolePerson);
					if (personRole == null)
					{
						if (personToUpdate.getPersonRoles() == null)
						{
							personToUpdate.setPersonRoles(new ArrayList());
						}
						personToUpdate.getPersonRoles().add(rolePerson);
						newRoles++;
					}

					if (personToUpdate.getEstadoCivil().getEstadoCivil().intValue() > 7)
					{
						System.out.println(
							"Erro : " +  personToUpdate.getEstadoCivil().getEstadoCivil().intValue());
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.out.println("Erro a converter a pessoa " + person + "\n");
					continue;
				}
			}
			//close databases
			DB.confirmarTransaccao();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}
	}

	private static void personFilter(LimpaNaturalidades limpaNaturalidades, IPessoa pessoa)
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

	private static void updatePerson(IPessoa person2Write, IPessoa person2Convert) throws Exception
	{

		try
		{
			//Backup
			String password = new String(person2Write.getPassword());
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

			Collection rolesLists = person2Write.getPersonRoles();
			List credtisLists = person2Write.getManageableDepartmentCredits();

			BeanUtils.copyProperties(person2Write, person2Convert);

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
			person2Write.setPersonRoles(rolesLists);
			person2Write.setManageableDepartmentCredits(credtisLists);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Erro a converter a Pessoa " + person2Convert.getNome());
			throw new Exception(e);
		}
	}
}
