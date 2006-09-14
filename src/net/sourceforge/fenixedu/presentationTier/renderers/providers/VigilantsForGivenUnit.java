package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class VigilantsForGivenUnit implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        VigilantGroupBean bean = (VigilantGroupBean) source;
        Unit unit = bean.getUnit();

        List<VigilantGroup> vigilantGroup = unit.getVigilantGroupsForGivenExecutionYear(ExecutionYear
                .readCurrentExecutionYear());
        Set<Vigilant> vigilants = new HashSet<Vigilant>();

        for (VigilantGroup group : vigilantGroup) {
            vigilants.addAll(group.getVigilants());
        }

        List vigilantsToReturn = new ArrayList<Vigilant>(vigilants);

        Collections.sort(vigilantsToReturn, new BeanComparator("person.name"));
        return vigilantsToReturn;
    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}