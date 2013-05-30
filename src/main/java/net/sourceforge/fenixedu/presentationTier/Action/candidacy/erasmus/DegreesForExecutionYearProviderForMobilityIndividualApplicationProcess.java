/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
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
        /*    for (final Degree degree : Degree.readAllByDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
                    DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_DEGREE)) {

                if (degree.getSigla().equals("MSCIT")) {
                    continue;
                }

                if (degree.getSigla().equals("MCR")) {
                    continue;
                }

                if (degree.getSigla().equals("MEEst")) {
                    continue;
                }

                if (degree.getSigla().equals("MMM")) {
                    continue;
                }

                if (degree.getSigla().equals("MEFarm")) {
                    continue;
                }

                if (matchesExecutionYear(degree, chooseDegreeBean.getExecutionYear())) {
                    result.add(degree);
                }
            }*/

        return result;
    }

    private boolean matchesExecutionYear(Degree degree, ExecutionYear executionYear) {
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
            if (executionDegree.getDegree() == degree) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
