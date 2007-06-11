package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class GroupsForUnit implements DataProvider {

	public Converter getConverter() {
		return null;
	}

	public Object provide(Object source, Object currentValue) {
		
		Unit unit = null;
		try {
			unit = (Unit) MethodUtils.invokeMethod(source, "getUnit", null);
			if (unit == null) {
				return Collections.EMPTY_LIST;
			}
			else {
				return MethodUtils.invokeMethod(unit, "getGroups", null);
			}
				
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		

	}

}
