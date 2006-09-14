package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class VigilantGroupsOfExamCoordinator implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        VigilantGroupBean bean = (VigilantGroupBean) source;
        ExamCoordinator coordinator = bean.getExamCoordinator();

        List<VigilantGroup> vigilantGroups = coordinator.getVigilantGroups();

        return vigilantGroups;

    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}