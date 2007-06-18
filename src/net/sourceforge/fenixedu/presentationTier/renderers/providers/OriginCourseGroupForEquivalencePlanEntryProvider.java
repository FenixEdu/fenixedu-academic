package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.CourseGroupEquivalencePlanEntry.CourseGroupEquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class OriginCourseGroupForEquivalencePlanEntryProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final CourseGroupEquivalencePlanEntryCreator courseGroupEquivalencePlanEntryCreator = (CourseGroupEquivalencePlanEntryCreator) source;
	final DegreeCurricularPlanEquivalencePlan equivalencePlan = (DegreeCurricularPlanEquivalencePlan) courseGroupEquivalencePlanEntryCreator.getEquivalencePlan();
	final DegreeCurricularPlan degreeCurricularPlan = equivalencePlan.getSourceDegreeCurricularPlan();
	final Set<CourseGroup> courseGroups = degreeCurricularPlan.getAllCoursesGroups();
	return courseGroups;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
