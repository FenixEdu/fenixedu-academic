

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;

public class PessoaVO extends VersionedObjectsBase implements IPessoaPersistente
{

	public Person lerPessoaPorUsername(final String username) throws ExcepcaoPersistencia
	{
		for (final Person person : (List<Person>) readAll(Person.class))
		{
			if (person.getUsername().equalsIgnoreCase(username))
			{
				return person;
			}
		}
		return null;
	}

	public List findPersonByName(String name) throws ExcepcaoPersistencia
	{

		final String nameToMatch = name.replaceAll("%", ".*");
		final List persons = new ArrayList();

		for (final Person person : (List<Person>) readAll(Person.class))
		{
			if (person.getNome().toLowerCase().matches(nameToMatch.toLowerCase()))
			{
				persons.add(person);
			}
		}
		return persons;

	}

	public List findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
			throws ExcepcaoPersistencia
	{

		final ArrayList persons = new ArrayList(findPersonByName(name));

		return persons.subList(startIndex, startIndex + numberOfElementsInSpan);

	}

	public Integer countAllPersonByName(String name) throws ExcepcaoPersistencia
	{

		return new Integer(findPersonByName(name).size());

	}

	public Person lerPessoaPorNumDocIdETipoDocId(final String numeroDocumentoIdentificacao,
			final IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia
	{

		for (final Person person : (List<Person>) readAll(Person.class))
		{
			if (person.getNumeroDocumentoIdentificacao().equals(numeroDocumentoIdentificacao)
					&& person.getIdDocumentType().equals(tipoDocumentoIdentificacao))
			{
				return person;
			}
		}
		return null;

	}

	/*
	 * This method return a list with elements returned by the limited search.
	 */
	public List<Person> readActivePersonByNameAndEmailAndUsernameAndDocumentId(final String name,
			final String email, final String username, final String documentIdNumber,
			final Integer startIndex, final Integer numberOfElementsInSpan, Role role, Degree degree,
			DegreeType degreeType, Department department) throws ExcepcaoPersistencia
	{

		final String nameToMatch = name.replaceAll("%", ".*");
		final String emailToMatch = email.replaceAll("%", ".*");
		final String usernameToMatch = email.replaceAll("%", ".*");
		final String documentIdNumberToMatch = documentIdNumber.replaceAll("%", ".*");

		List<Person> filteredPersons = new ArrayList<Person>();

		for (final Person person : (List<Person>) readAll(Person.class))
		{
			if (name != null && name.length() > 0 && !person.getNome().matches(nameToMatch))
			{
				continue;
			}
			if (email != null && email.length() > 0 && !person.getEmail().matches(emailToMatch))
			{
				continue;
			}
			if (username != null && username.length() > 0)
			{
				if (!person.getUsername().matches(usernameToMatch))
				{
					continue;
				}
				if (person.getUsername().matches("INA.*"))
				{
					continue;
				}
			}
			if (documentIdNumber != null && documentIdNumber.length() > 0
					&& !person.getNumeroDocumentoIdentificacao().matches(documentIdNumberToMatch))
			{
				continue;
			}
			if (role != null && !person.getPersonRoles().contains(role))
			{
				continue;
			}

			filteredPersons.add(person);
		}

		List<Person> result = new ArrayList<Person>();
		if (startIndex == null && numberOfElementsInSpan == null)
		{
			Collections.sort(filteredPersons, new BeanComparator("nome"));
			result = filteredPersons;
		}
		else if (startIndex != null && numberOfElementsInSpan != null)
		{
			result = (filteredPersons.subList(startIndex, startIndex + numberOfElementsInSpan));
		}

		return result;
	}

	public Integer countActivePersonByNameAndEmailAndUsernameAndDocumentId(final String name,
			final String email, final String username, final String documentIdNumber,
			final Integer startIndex, Role role, Degree degree, DegreeType degreeType,
			Department department) throws ExcepcaoPersistencia
	{

		final String nameToMatch = name.replaceAll("%", ".*");
		final String emailToMatch = email.replaceAll("%", ".*");
		final String usernameToMatch = email.replaceAll("%", ".*");
		final String documentIdNumberToMatch = documentIdNumber.replaceAll("%", ".*");

		int count = 0;
		for (final Person person : (List<Person>) readAll(Person.class))
		{
			if (name != null && name.length() > 0 && !person.getNome().matches(nameToMatch))
			{
				continue;
			}
			if (email != null && email.length() > 0 && !person.getEmail().matches(emailToMatch))
			{
				continue;
			}
			if (username != null && username.length() > 0)
			{
				if (!person.getUsername().matches(usernameToMatch))
				{
					continue;
				}
				if (person.getUsername().matches("INA.*"))
				{
					continue;
				}
			}
			if (role != null && !person.getPersonRoles().contains(role))
			{
				continue;
			}
			if (documentIdNumber != null && documentIdNumber.length() > 0
					&& !person.getNumeroDocumentoIdentificacao().matches(documentIdNumberToMatch))
			{
				continue;
			}
			count++;
		}
		return new Integer(count);
	}

	public List<Person> readPersonsBySubName(String subName) throws ExcepcaoPersistencia
	{

		final List<Person> persons = (List<Person>) readAll(Person.class);

		final String stringToMatch = subName.replace("%", ".*").replace(" ", ".*");

		return (List<Person>) CollectionUtils.select(persons, new Predicate()
		{

			public boolean evaluate(Object object)
			{
				Person person = (Person) object;
				if (person.getNome().toLowerCase().matches(stringToMatch.toLowerCase()))
				{
					return true;
				}
				return false;
			}

		});
	}

	public Collection<Person> readByIdentificationDocumentNumber(
			final String identificationDocumentNumber) throws ExcepcaoPersistencia
	{

		Collection<Person> persons = readAll(Person.class);

		return CollectionUtils.select(persons, new Predicate()
		{

			public boolean evaluate(Object arg0)
			{
				Person person = (Person) arg0;
				return person.getNumeroDocumentoIdentificacao().equalsIgnoreCase(identificationDocumentNumber);
			}

		});

	}

	public Integer CountPersonByDepartment(String name, List<Teacher> teacher, Integer startIndex,
			Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
	{
		return new Integer(0);
	}

	public List<Person> PersonByDepartment(String name, List<Teacher> teacher, Integer startIndex,
			Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
	{
		return null;
	}

	public boolean emailOwnedByFenixPerson(Collection<String> emails)
	{
		boolean result = false;
		Collection<Person> persons = readAll(Person.class);
		for (String email : emails)
		{
			for (Person person : persons)
			{
				if (email.equalsIgnoreCase(person.getEmail()))
				{
					result = true;
					break;
				}
			}
		}
		return result;
	}

}
