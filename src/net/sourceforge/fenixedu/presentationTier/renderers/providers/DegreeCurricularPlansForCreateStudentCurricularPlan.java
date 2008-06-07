package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.factoryExecutors.StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DegreeCurricularPlansForCreateStudentCurricularPlan implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final StudentCurricularPlanCreator creator = (StudentCurricularPlanCreator) source;
	final SortedSet<DegreeCurricularPlan> result = new TreeSet<DegreeCurricularPlan>(
		new BeanComparator("name"));
	if (creator.getDegree() != null) {
	    Set<DegreeCurricularPlan> degreeCurricularPlans = creator.getRegistration()
		    .getDegreeCurricularPlans();
	    for (DegreeCurricularPlan degreeCurricularPlan : creator.getDegree()
		    .getDegreeCurricularPlansSet()) {
		if (!degreeCurricularPlans.contains(degreeCurricularPlan)) {
		    result.add(degreeCurricularPlan);
		}
	    }
	}
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
