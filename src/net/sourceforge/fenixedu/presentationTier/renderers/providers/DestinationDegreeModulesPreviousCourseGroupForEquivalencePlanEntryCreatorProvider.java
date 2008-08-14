package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry.EquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DestinationDegreeModulesPreviousCourseGroupForEquivalencePlanEntryCreatorProvider implements DataProvider {

    private static class CourseGroupPair extends GenericPair<Integer, String> {

	public CourseGroupPair(CourseGroup courseGroup, String path) {
	    super(courseGroup.getIdInternal(), path);
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof CourseGroup) {
		return getLeft().equals(((CourseGroup) obj).getIdInternal());
	    }

	    return false;
	}

    }

    public Object provide(Object source, Object currentValue) {
	final List<GenericPair<Integer, String>> result = new ArrayList<GenericPair<Integer, String>>();
	for (final List<DegreeModule> degreeModules : getDegreeCurricularPlan(source).getDcpDegreeModulesIncludingFullPath(
		CourseGroup.class, null)) {
	    result.add(new CourseGroupPair((CourseGroup) degreeModules.get(degreeModules.size() - 1), buildPath(degreeModules)));

	}

	return result;

    }

    protected DegreeCurricularPlan getDegreeCurricularPlan(final Object source) {

	final EquivalencePlanEntryCreator equivalencePlanEntryCreator = (EquivalencePlanEntryCreator) source;
	final DegreeCurricularPlanEquivalencePlan equivalencePlan = (DegreeCurricularPlanEquivalencePlan) equivalencePlanEntryCreator
		.getEquivalencePlan();

	return equivalencePlan.getDegreeCurricularPlan();

    }

    private String buildPath(final List<DegreeModule> degreeModules) {
	final StringBuilder result = new StringBuilder();

	final Iterator<DegreeModule> iterator = degreeModules.iterator();
	while (iterator.hasNext()) {
	    final DegreeModule degreeModule = iterator.next();
	    result.append(degreeModule.getName());
	    if (iterator.hasNext()) {
		result.append(" > ");
	    }
	}

	return result.toString();

    }

    public Converter getConverter() {
	return new BiDirectionalConverter() {
	    @Override
	    public Object convert(Class type, Object value) {
		if (!StringUtils.isEmpty((String) value)) {
		    final Integer courseGroupId = (Integer.valueOf((String) value));
		    return RootDomainObject.getInstance().readDegreeModuleByOID(courseGroupId);
		}

		return null;
	    }

	    @Override
	    public String deserialize(Object object) {
		if (object == null) {
		    return "";
		}

		final CourseGroupPair option = (CourseGroupPair) object;

		return option.getLeft().toString();

	    }
	};
    }
}
