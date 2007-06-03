package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.CurricularCourseEquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CurricularCourseForEquivalencePlanEntryProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final CurricularCourseEquivalencePlanEntryCreator courseEquivalencePlanEntryCreator = (CurricularCourseEquivalencePlanEntryCreator) source;
	final DegreeCurricularPlanEquivalencePlan equivalencePlan = (DegreeCurricularPlanEquivalencePlan) courseEquivalencePlanEntryCreator.getEquivalencePlan();
	final DegreeCurricularPlan degreeCurricularPlan = equivalencePlan.getSourceDegreeCurricularPlan();
	final Set<CurricularCourse> curricularCourses = degreeCurricularPlan.getAllCurricularCourses();
	curricularCourses.removeAll(courseEquivalencePlanEntryCreator.getCurricularCourses());
	return curricularCourses;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
