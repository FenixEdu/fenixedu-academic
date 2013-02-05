package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class GraduationDegreeCurricularPlansProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final PaymentsBean bean = (PaymentsBean) source;

        final Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        result.addAll(DegreeCurricularPlan.readByDegreeTypesAndStateWithExecutionDegreeForYear(
                DegreeType.getDegreeTypesFor(AdministrativeOfficeType.DEGREE), null, bean.getExecutionYear()));
        result.add(DegreeCurricularPlan.readEmptyDegreeCurricularPlan());

        return result;
    }
}
