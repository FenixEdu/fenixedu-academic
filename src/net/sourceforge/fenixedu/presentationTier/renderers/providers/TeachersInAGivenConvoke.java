package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class TeachersInAGivenConvoke implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ConvokeBean bean = (ConvokeBean) source;
        List<Vigilant> teachersForAGivenCourse = bean.getTeachersForAGivenCourse();
        WrittenEvaluation writtenEvaluation = bean.getWrittenEvaluation();
        
        if(writtenEvaluation!=null && writtenEvaluation.getVigilancys().size()>0) {
			for(Vigilancy convoke : writtenEvaluation.getVigilancys()) {
				teachersForAGivenCourse.remove(convoke.getVigilant());
			}
			
		}
        return teachersForAGivenCourse;
    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
