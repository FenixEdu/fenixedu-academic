package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction.DegreesMergeBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object current) {

        final DegreesMergeBean degreesMergeBean = (DegreesMergeBean) source;

        List<Degree> result = Degree.readAllByDegreeCode(Degree.DEFAULT_MINISTRY_CODE);
        Collections.sort(result, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

        return result;
    }

    @Override
    public Converter getConverter() {
        // TODO Auto-generated method stub
        return null;
    }
}
