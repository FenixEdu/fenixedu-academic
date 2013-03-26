package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantBean;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class VigilantGroupsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        VigilantBean bean = (VigilantBean) source;
        List<VigilantGroup> groups = bean.getVigilantGroups();

        Collections.sort(groups, new BeanComparator("name"));
        return groups;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}