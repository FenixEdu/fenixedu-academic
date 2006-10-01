package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class ExamCoordinatorsForGivenGroup implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        VigilantGroupBean bean = (VigilantGroupBean) source;
        VigilantGroup group = bean.getSelectedVigilantGroup();

        List<ExamCoordinator> coordinators = new ArrayList<ExamCoordinator>(group.getExamCoordinators());

        Collections.sort(coordinators, new BeanComparator("person.name"));
        return coordinators;

    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
