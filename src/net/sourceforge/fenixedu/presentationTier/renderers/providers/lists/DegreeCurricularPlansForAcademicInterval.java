package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForAcademicInterval implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final SortedSet<DegreeCurricularPlan> result = new TreeSet<DegreeCurricularPlan>(
		DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

	AcademicInterval academicInterval = ((ExecutionDegreeListBean) source).getAcademicInterval();

	if (academicInterval != null) {
	    final Person person = AccessControl.getPerson();

	    if (person.isAdministrativeOfficeEmployee()) {
		final Set<Degree> degrees = person.getEmployee().getAdministrativeOffice().getAdministratedDegrees();

		for (DegreeCurricularPlan plan : DegreeCurricularPlan.readByAcademicInterval(academicInterval)) {
		    if (degrees.contains(plan.getDegree())) {
			result.add(plan);
		    }
		}
	    } else {
		result.addAll(DegreeCurricularPlan.readByAcademicInterval(academicInterval));
	    }
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
