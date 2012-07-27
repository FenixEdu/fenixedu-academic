package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.credits.util.ProjectTutorialServiceBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AvailableAttendsForProjectTutorialServiceProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	ProjectTutorialServiceBean bean = (ProjectTutorialServiceBean) source;
	List<Attends> attends = new ArrayList<Attends>();
	for (Attends attend : bean.getProfessorship().getExecutionCourse().getAttends()) {
	    if (attend.getDegreeProjectTutorialService() == null
		    || attend.getDegreeProjectTutorialService().getProfessorship().equals(bean.getProfessorship())) {
		attends.add(attend);
	    }
	}
	return attends;
    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }
}
