package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.IFormation;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CivilYearsProvider implements DataProvider {

	public static class CivilYearsProviderDescendingOrder extends CivilYearsProvider {

		@Override
		public Object provide(Object source, Object currentValue) {

			Set<String> years = new TreeSet<String>(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
			years.addAll((Set<String>) super.provide(source, currentValue));
			return years;
		}

	}

	@Override
	public Object provide(Object source, Object currentValue) {

		IFormation formation = (IFormation) source;
		int firstYear = formation.getFirstYear();

		int currentYear = new DateTime().year().get();
		Set<String> years = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		do {
			years.add(String.valueOf(firstYear));
		} while (++firstYear <= currentYear);

		return years;
	}

	@Override
	public Converter getConverter() {
		return null;
	}

}
