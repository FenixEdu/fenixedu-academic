package net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.Pair;

public class DegreeCurricularPlanRendererConfig implements Serializable {

    static private final String MAPPING = "${mapping}";
    static private final String METHOD = "${method}";
    static private final String VIEW_CC_URL_TEMPLATE = MAPPING + "?method=" + METHOD;

    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;
    private DomainReference<ExecutionYear> executionInterval;

    private OrganizeType organizeBy = OrganizeType.GROUPS;
    private boolean showRules = false;
    private boolean showCourses = true;

    private String viewCurricularCourseUrl = VIEW_CC_URL_TEMPLATE;
    private List<Pair<String, String>> viewCurricularCourseUrlParameters = new ArrayList<Pair<String, String>>();

    public DegreeCurricularPlanRendererConfig() {
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return (this.degreeCurricularPlan != null) ? this.degreeCurricularPlan.getObject() : null;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(
		degreeCurricularPlan) : null;
    }

    public ExecutionYear getExecutionInterval() {
	return (this.executionInterval != null) ? this.executionInterval.getObject() : null;
    }

    public void setExecutionInterval(ExecutionYear executionInterval) {
	this.executionInterval = (executionInterval != null) ? new DomainReference<ExecutionYear>(executionInterval) : null;
    }

    public OrganizeType getOrganizeBy() {
	return organizeBy;
    }

    public void setOrganizeBy(OrganizeType organizeBy) {
	this.organizeBy = organizeBy;
    }

    public boolean organizeByGroups() {
	return getOrganizeBy() == OrganizeType.GROUPS;
    }

    public boolean organizeByYears() {
	return getOrganizeBy() == OrganizeType.YEARS;
    }

    public boolean isShowRules() {
	return showRules;
    }

    public void setShowRules(boolean showRules) {
	this.showRules = showRules;
    }

    public boolean isShowCourses() {
	return showCourses;
    }

    public void setShowCourses(boolean showCourses) {
	this.showCourses = showCourses;
    }

    private boolean hasAnyExecutionDegree() {
	return getDegreeCurricularPlan().hasAnyExecutionDegrees();
    }

    private List<ExecutionDegree> getExecutionDegrees() {
	return getDegreeCurricularPlan().getExecutionDegrees();
    }

    public List<ExecutionYear> getExecutionYearsFromExecutionDegrees() {
	final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
	for (final ExecutionDegree executionDegree : getExecutionDegrees()) {
	    result.add(executionDegree.getExecutionYear());
	}
	return result;
    }

    private ExecutionYear getDegreeMinimumExecutionYear() {
	return getDegreeCurricularPlan().getRoot().getMinimumExecutionPeriod().getExecutionYear();
    }

    public boolean isValid() {
	return getDegreeCurricularPlan().isApproved() && hasAnyExecutionDegree();
    }

    public String getViewCurricularCourseUrl() {
	return viewCurricularCourseUrl;
    }

    public void setViewCurricularCourseUrlMapping(final String mapping) {
	viewCurricularCourseUrl = viewCurricularCourseUrl.replace(MAPPING, mapping);
    }

    public void setViewCurricularCourseUrlMethod(final String method) {
	viewCurricularCourseUrl = viewCurricularCourseUrl.replace(METHOD, method);
    }

    public List<Pair<String, String>> getViewCurricularCourseUrlParameters() {
	return viewCurricularCourseUrlParameters;
    }

    public void addViewCurricularCourseUrlParameter(final String key, final String value) {
	viewCurricularCourseUrlParameters.add(new Pair<String, String>(key, value));
    }

    static public enum OrganizeType {
	GROUPS, YEARS;
    }

    static public class ExecutionIntervalProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    final DegreeCurricularPlanRendererConfig config = (DegreeCurricularPlanRendererConfig) source;
	    return (config.hasAnyExecutionDegree()) ? config.getExecutionYearsFromExecutionDegrees() : Collections
		    .singletonList(config.getDegreeMinimumExecutionYear());

	}

    }

}
