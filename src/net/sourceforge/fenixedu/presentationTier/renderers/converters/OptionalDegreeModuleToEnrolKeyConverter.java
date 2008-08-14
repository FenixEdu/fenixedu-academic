/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class OptionalDegreeModuleToEnrolKeyConverter extends Converter {

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
	    if (parts.length < 3) {
		throw new ConversionException("invalid key format: " + key);
	    }

	    final Context context = (Context) converter.convert(type, parts[0]);
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
	    final ExecutionSemester executionSemester = (ExecutionSemester) converter.convert(type, parts[2]);
	    final CurricularCourse curricularCourse = (CurricularCourse) converter.convert(type, parts[3]);
	    result.add(new OptionalDegreeModuleToEnrol(curriculumGroup, context, executionSemester, curricularCourse));
	}

	return result;
    }
}