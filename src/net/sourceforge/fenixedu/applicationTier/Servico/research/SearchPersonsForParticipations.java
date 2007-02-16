package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchPersonsForParticipations extends Service implements AutoCompleteSearchService {

	public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {
		if (type != Person.class)
			return null;

		List result;

		String slotName = arguments.get("slot");

		if (value == null) {
			result = Person.readAllPersons();
		}

		else {
			result = new ArrayList<DomainObject> ();
			
			String[] values = StringNormalizer.normalize(value).toLowerCase().replaceAll("\\.", "").split(
					"\\p{Space}+");
			
			Role researcherRole = Role.getRoleByRoleType(RoleType.RESEARCHER);
			
			outter: for (Person person : researcherRole.getAssociatedPersons()) {
			
				if (person.hasExternalPerson()) {
					continue;
				}

				try {
					Object objectValue = (Object) PropertyUtils.getProperty(person, slotName);

					if (objectValue == null) {
						continue;
					}

					String normalizedValue = StringNormalizer.normalize(objectValue.toString()).toLowerCase();

					int lastIndexOf = -1;
					int indexOf = 0;
					for (int i = 0; i < values.length; i++) {
						String part = values[i];

						indexOf = (part.length() == 1) ? normalizedValue.indexOf(" " + part)
								: normalizedValue.indexOf(part);

						if (indexOf == -1 || indexOf < lastIndexOf) {
							continue outter;
						}
						lastIndexOf = indexOf;
					}

					result.add(person);

					if (result.size() >= limit) {
						break;
					}

				} catch (IllegalAccessException e) {
					throw new DomainException("searchObject.type.notFound", e);
				} catch (InvocationTargetException e) {
					throw new DomainException("searchObject.failed.read", e);
				} catch (NoSuchMethodException e) {
					throw new DomainException("searchObject.failed.read", e);
				}
			}
			Collections.sort(result, new BeanComparator(slotName));
		}

		return result;

	}

}
