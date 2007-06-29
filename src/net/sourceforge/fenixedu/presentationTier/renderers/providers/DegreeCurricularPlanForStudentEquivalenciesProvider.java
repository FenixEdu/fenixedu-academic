package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeCurricularPlanForStudentEquivalenciesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
	degreeTypes.add(DegreeType.BOLONHA_DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
	degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
    	return new ArrayList<DegreeCurricularPlan>(DegreeCurricularPlan.getDegreeCurricularPlans(degreeTypes));
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
