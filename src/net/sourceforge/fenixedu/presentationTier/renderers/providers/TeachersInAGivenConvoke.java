package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TeachersInAGivenConvoke implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		ConvokeBean bean = (ConvokeBean) source;
		List<VigilantWrapper> teachersForAGivenCourse = bean.getTeachersForAGivenCourse();
		WrittenEvaluation writtenEvaluation = bean.getWrittenEvaluation();

		if (writtenEvaluation != null && writtenEvaluation.getVigilancies().size() > 0) {
			for (Vigilancy convoke : writtenEvaluation.getVigilancies()) {
				teachersForAGivenCourse.remove(convoke.getVigilantWrapper());
			}

		}
		return teachersForAGivenCourse;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyArrayConverter();
	}

}
