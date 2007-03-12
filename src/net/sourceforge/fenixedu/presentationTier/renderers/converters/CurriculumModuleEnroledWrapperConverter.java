/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CurriculumModuleEnroledWrapperConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
	if (value == null) {
	    return null;
	}

	final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
	final List<CurriculumModuleEnroledWrapper> result = new ArrayList<CurriculumModuleEnroledWrapper>();
	final String[] values = (String[]) value;
	for (int i = 0; i < values.length; i++) {
	    String key = values[i];

	    String[] parts = key.split(",");
	    if (parts.length < 2) {
		throw new ConversionException("invalid key format: " + key);
	    }

	    final CurriculumModule curriculumModule = (CurriculumModule) converter.convert(type, parts[0]);
	    final ExecutionPeriod executionPeriod = (ExecutionPeriod) converter.convert(type, parts[1]);
	    result.add(new CurriculumModuleEnroledWrapper(curriculumModule, executionPeriod));
	}

	return result;
    }
}