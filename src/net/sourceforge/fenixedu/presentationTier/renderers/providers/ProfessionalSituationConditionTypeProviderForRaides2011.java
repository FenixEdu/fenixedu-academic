package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ProfessionalSituationConditionType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ProfessionalSituationConditionTypeProviderForRaides2011 implements DataProvider {

    @Override
    public Converter getConverter() {
	return new EnumConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	List<ProfessionalSituationConditionType> results = new ArrayList<ProfessionalSituationConditionType>();
	for (ProfessionalSituationConditionType type : ProfessionalSituationConditionType.values()) {
	    if (!type.equals(ProfessionalSituationConditionType.MILITARY_SERVICE)) {
		results.add(type);
	    }
	}
	return results;
    }

}
