package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction.DegreesMergeBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

	final DegreesMergeBean degreesMergeBean = (DegreesMergeBean) source;

	final List<Degree> degrees = new ArrayList<Degree>(Degree.readNotEmptyDegrees());

	Collections.sort(degrees, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

	return degrees;
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}