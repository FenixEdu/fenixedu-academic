package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class GraduationDegreeCurricularPlansProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
	result.addAll(DegreeCurricularPlan.readByDegreeTypesAndState(DegreeType
		.getDegreeTypesFor(AdministrativeOfficeType.DEGREE), DegreeCurricularPlanState.ACTIVE));
	result.add(DegreeCurricularPlan.readEmptyDegreeCurricularPlan());

	return result;
    }
}
