package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class PeopleForResearchUnit implements DataProvider {

	public Converter getConverter() {
		return null;
	}

	public Object provide(Object source, Object currentValue) {
		if (source instanceof ResearchUnit) {
			return ((ResearchUnit) source).getAssociatedPeople();
		} else {
			try {
				Unit unit = (Unit) MethodUtils.invokeMethod(source, "getUnit", null);
				return ((ResearchUnit) unit).getAssociatedPeople();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
