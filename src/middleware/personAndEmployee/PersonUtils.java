/*
 * Created on 26/Jul/2003 by jpvl
 *
 */
package middleware.personAndEmployee;

import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPessoa;
import Dominio.Pessoa;

/**
 * @author jpvl
 */
public abstract class PersonUtils {
	public static void updatePerson(
		IPessoa person2Write,
		IPessoa person2Convert)
		throws Exception {

		try {
			// Password Backup
			String password = null;
			if (person2Write.getPassword() != null) {
				password = new String(person2Write.getPassword());
			}

			Integer internalCode =
				new Integer(person2Write.getIdInternal().intValue());
			String username = new String(person2Write.getUsername());
			String mobilePhone = new String(person2Write.getTelemovel());
			String email = new String(person2Write.getEmail());
			String url = new String(person2Write.getEnderecoWeb());
			Collection rolesLists = person2Write.getPersonRoles();
			List credtisLists = person2Write.getManageableDepartmentCredits();

			BeanUtils.copyProperties(person2Write, person2Convert);

			person2Write.setIdInternal(internalCode);
			person2Write.setPassword(password);
			person2Write.setUsername(username);
			person2Write.setEmail(email);
			person2Write.setTelemovel(mobilePhone);
			person2Write.setEnderecoWeb(url);
			person2Write.setPersonRoles(rolesLists);
			person2Write.setManageableDepartmentCredits(credtisLists);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
				"Erro a converter a Pessoa " + person2Convert.getNome());
			throw new Exception(e);
		}
	}

	public static IPessoa getPerson(
		String number,
		String bi,
		String usernamePrefix,
		PersistenceBroker broker)
		throws Exception {

		Criteria criteriaByBi = new Criteria();
		criteriaByBi.addEqualTo("numeroDocumentoIdentificacao", bi);
		List resultByBi = doQuery(broker, criteriaByBi, Pessoa.class);

		Criteria criteriaByUsername = new Criteria();
		criteriaByUsername.addEqualTo("username", usernamePrefix + number);
		List resultByUsername =
			doQuery(broker, criteriaByUsername, Pessoa.class);

		IPessoa person = null;
		if (resultByUsername.size() == 1) {
			IPessoa personByBi = (IPessoa) resultByBi.get(0);
			IPessoa personByUsername = (IPessoa) resultByUsername.get(0);

			if (personByBi
				.getUsername()
				.equals(personByUsername.getUsername())) {
				person = (IPessoa) resultByBi.get(0);
			} else {
				PersonUtils.updatePerson(personByUsername, personByBi);
				person = personByUsername;
				broker.delete(personByBi);
			}
		} else {
			person = (IPessoa) resultByBi.get(0);
		}
		return person;
	}

	private static List doQuery(
		PersistenceBroker broker,
		Criteria criteria,
		Class classToQuery) {
		Query query = new QueryByCriteria(classToQuery, criteria);
		return (List) broker.getCollectionByQuery(query);
	}
}