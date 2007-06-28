package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class PeopleForUnitGroups implements DataProvider {

	public Converter getConverter() {
		return null;
	}

	public Object provide(Object source, Object currentValue) {
		Unit unit;
		if(source instanceof Unit) {
			unit = (Unit) source;
		}
		else {
			try {
			unit = (Unit) MethodUtils.invokeMethod(source, "getUnit", null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return getPeopleForUnit(unit);
	}

	private Collection<Person> getPeopleForUnit(Unit unit) {
		return unit.getPossibleGroupMembers();
	}

}
