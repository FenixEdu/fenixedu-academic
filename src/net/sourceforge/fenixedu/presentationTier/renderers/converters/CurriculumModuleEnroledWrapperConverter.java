/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumModuleEnroledWrapperConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
	if (value == null) {
	    return null;
	}

	final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
	final List<EnroledCurriculumModuleWrapper> result = new ArrayList<EnroledCurriculumModuleWrapper>();
	final String[] values = (String[]) value;
	for (int i = 0; i < values.length; i++) {
	    String key = values[i];

	    String[] parts = key.split(",");
	    if (parts.length < 2) {
		throw new ConversionException("invalid key format: " + key);
	    }

	    final CurriculumModule curriculumModule = (CurriculumModule) converter.convert(type, parts[0]);
	    final ExecutionSemester executionSemester = (ExecutionSemester) converter.convert(type, parts[1]);
	    result.add(new EnroledCurriculumModuleWrapper(curriculumModule, executionSemester));
	}

	return result;
    }
}