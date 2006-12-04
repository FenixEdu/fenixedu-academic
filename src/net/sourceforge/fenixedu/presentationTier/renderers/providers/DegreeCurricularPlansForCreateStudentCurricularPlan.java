package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.factoryExecutors.StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

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
	    result.addAll(creator.getDegree().getDegreeCurricularPlansSet());
	}
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
