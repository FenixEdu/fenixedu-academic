/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeModuleToEnrolKeyConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
	if (value == null) {
	    return null;
	}

	final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
	final List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	final String[] values = (String[]) value;
	for (int i = 0; i < values.length; i++) {
	    String key = values[i];

	    String[] parts = key.split(",");
	    if (parts.length < 2) {
		throw new ConversionException("invalid key format: " + key);
	    }

	    final Context context = (Context) converter.convert(type, parts[0]);
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
	    result.add(new DegreeModuleToEnrol(curriculumGroup, context));
	}

	return result;
    }
}