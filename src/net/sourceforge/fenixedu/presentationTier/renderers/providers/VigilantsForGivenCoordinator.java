package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.UnavailablePeriodBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class VigilantsForGivenCoordinator implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        UnavailablePeriodBean bean = (UnavailablePeriodBean) source;

        ExamCoordinator coordinator = bean.getCoordinator();

        List<Vigilant> vigilantsToReturn = new ArrayList<Vigilant>(coordinator
                .getVigilantsThatCanManage());

        Collections.sort(vigilantsToReturn, new BeanComparator("person.name"));
        return vigilantsToReturn;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}