package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class VigilantGroupsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        VigilantBean bean = (VigilantBean) source;
        List<VigilantGroup> groups = bean.getVigilantGroups();

        Collections.sort(groups, new BeanComparator("name"));
        return groups;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}