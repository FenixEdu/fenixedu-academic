/*
 * Created on 26/Jul/2003 by jpvl
 *  
 */
package middleware.personAndEmployee;

import java.util.Collection;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import DataBeans.util.CopyUtils;
import Dominio.ICountry;
import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.Role;
import Util.RoleType;

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
	
	public static Role descobreRole(PersistenceBroker broker, RoleType roleType) throws Exception
	{
		Role role = null;

		Criteria criteria = new Criteria();
		Query query = null;
		criteria.addEqualTo("roleType", roleType);
		query = new QueryByCriteria(Role.class, criteria);

		List result = (List) broker.getCollectionByQuery(query);
		if (result.size() == 0)
		{
			throw new Exception("Role Desconhecido !!!");
		}
		else
		{
			role = (Role) result.get(0);
		}
		return role;
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
}