/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesForExecutionYearProviderForMobilityIndividualApplicationProcess implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        final MobilityIndividualApplicationProcessBean chooseDegreeBean = (MobilityIndividualApplicationProcessBean) source;

        result.addAll(chooseDegreeBean.getPossibleDegreesFromSelectedUniversity());

        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
